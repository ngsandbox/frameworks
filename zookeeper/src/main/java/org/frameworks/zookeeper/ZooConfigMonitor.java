package org.frameworks.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CreateBuilder;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.shared.SharedCount;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.curator.x.async.modeled.*;
import org.apache.curator.x.async.modeled.cached.CachedModeledFramework;
import org.apache.curator.x.async.modeled.typed.TypedModeledFramework0;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZooConfigMonitor<T> implements AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(ZooConfigMonitor.class);

    private final CuratorFramework client;
    private final AsyncCuratorFramework asyncFramework;
    private ModelSpecBuilder<T> specBuilder;

    private String znode;

    private boolean closed;

    private JacksonModelSerializer<T> serializer;

    private CreateMode mode;

    private final List<CachedModeledFramework<T>> subscribers = new ArrayList<>();

    /**
     * @param connectString comma separated host:port pairs, each corresponding to a zk
     *                      server. e.g. "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002" If
     *                      the optional chroot suffix is used the example would look
     *                      like: "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002/app/a"
     *                      where the client would be rooted at "/app/a" and all paths
     *                      would be relative to this root - ie getting/setting/etc...
     *                      "/foo/bar" would result in operations being run on
     *                      "/app/a/foo/bar" (from the server perspective).
     * @throws Exception in cases of network failure
     */
    public ZooConfigMonitor(String connectString, String znode, Class<T> clazz, CreateMode mode) throws Exception {
        this.znode = znode;
        this.mode = mode;
        serializer = JacksonModelSerializer.build(clazz);
        client = CuratorFrameworkFactory.newClient(connectString, new RetryNTimes(10, 500));
        asyncFramework = AsyncCuratorFramework.wrap(client);
        // build a model specification - you can pre-build all the model specifications for your app at startup
        specBuilder = ModelSpec.builder(ZPath.parse(znode), serializer)
                .withCreateMode(mode);
        client.start();
    }

    public ZooConfigMonitor(String connectString, String znode, Class<T> clazz) throws Exception {
        this(connectString, znode, clazz, CreateMode.PERSISTENT);
    }

    public void createNode() throws Exception {
        try {
            if (!isNodeExist(znode)) {
                CreateBuilder createBuilder = client.create();
                createBuilder.withMode(mode);
                createBuilder.creatingParentContainersIfNeeded();
                createBuilder.creatingParentsIfNeeded();
                createBuilder.forPath(znode);
            }
        } catch (Exception e) {
            LOG.error("Error establish connection with the Zookeeper's node " + znode, e);
            throw e;
        }
    }

    public SharedCount getSharedCount() {
        return new SharedCount(client, znode + "/counter", 0);
    }

    public InterProcessMutex getMutex() {
        return new InterProcessMutex(client, znode + "/locker");
    }

    public DistributedBarrier getBarrier() {
        return new DistributedBarrier(client, znode + "/barrier");
    }


    public void startSubscriber(Listener<T> listener) {
        Objects.requireNonNull(listener, "Listener instance shuld be specified");
        TypedModeledFramework0<T> typedClient = TypedModeledFramework0.from(ModeledFramework.builder(), specBuilder, znode + "/{id}");
        CachedModeledFramework<T> cache = typedClient.resolved(asyncFramework).cached();
        cache.start();
        cache.listenable().addListener((type, path, stat, model) -> {
            switch (type) {
                case NODE_REMOVED:
                    listener.removed(path.nodeName());
                    break;
                case NODE_ADDED:
                    listener.added(path.nodeName(), model);
                    break;
                case NODE_UPDATED:
                    listener.updated(path.nodeName(), model);
                    break;
            }

            LOG.trace("Subscribed name: %s ; path: %s ; version: %s", model.getClass().getSimpleName(), path, stat.getVersion());
        });

        subscribers.add(cache);
    }

    public Result<T> save(Object id, T model) {
        Objects.requireNonNull(id, "id instance should be specified");
        Objects.requireNonNull(model, "model instance should be specified");
        String nodePath = znode + "/" + id;
        return saveNode(model, nodePath);
    }

    public Result<T> save(T model) {
        Objects.requireNonNull(model, "model instance should be specified");
        return saveNode(model, znode);
    }

    private Result<T> saveNode(T model, String nodePath) {
        try {
            byte[] data = serializer.serialize(model);
            if (isNodeExist(nodePath)) {
                client.setData().forPath(nodePath, data);
            } else {
                CreateBuilder createBuilder = client.create();
                createBuilder.creatingParentContainersIfNeeded();
                createBuilder.creatingParentsIfNeeded();
                createBuilder.withMode(mode).forPath(nodePath, data);
            }

            return new Result<>(model);
        } catch (Exception e) {
            LOG.error("Error to run transaction save operation for node " + nodePath, e);
            return new Result<>("Error to save node " + nodePath, e);
        }
    }

    public boolean isNodeExist() throws Exception {
        return isNodeExist(znode);
    }

    public boolean isNodeExist(String nodePath) throws Exception {
        return client.checkExists().forPath(nodePath) != null;
    }

    public Result<T> read() {
        return readNode(znode);
    }

    public Result<T> read(String id) {
        Objects.requireNonNull(id, "id instance could not be null");
        String childNode = znode + "/" + id;
        return readNode(childNode);
    }

    private Result<T> readNode(String childNode) {
        Result<T> result;
        try {
            T model = serializer.deserialize(client.getData().forPath(childNode));
            result = new Result<>(model);
        } catch (Exception e) {
            result = new Result<>("Error to collect all children datas for node " + znode, e);
            LOG.error(result.getErrorMessage(), e);
            e.printStackTrace();
        }

        return result;
    }

    public List<String> getChildrenNodes() throws Exception {
        return client.getChildren().forPath(znode);
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
            subscribers.forEach(CloseableUtils::closeQuietly);
            CloseableUtils.closeQuietly(asyncFramework.unwrap());
            CloseableUtils.closeQuietly(client);
        }
    }
}
