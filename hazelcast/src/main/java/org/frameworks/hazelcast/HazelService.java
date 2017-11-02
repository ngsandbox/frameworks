package org.frameworks.hazelcast;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.map.eviction.LRUEvictionPolicy;
import lombok.extern.slf4j.Slf4j;
import org.frameworks.common.nao.entities.ContextInfo;
import org.frameworks.common.nao.entities.FileInfo;

@Slf4j
public class HazelService implements AutoCloseable {

    private static final String CONTEXTS_INFO_MAP = "CONTEXTS_INFO_MAP";
    private static final String FILES_INFO_MAP = "FILES_INFO_MAP";

    private final HazelcastInstance hz;

    public HazelService() {
        log.trace("Initialize hazelcast instance");
        Config hazelCfg = new Config().setInstanceName("ng-hazeazelcast")
                .setNetworkConfig(new NetworkConfig().setJoin(new JoinConfig().setTcpIpConfig(new TcpIpConfig().addMember("127.0.0.1"))))
                .addMapConfig(new MapConfig().setName(CONTEXTS_INFO_MAP)
                        .setMapEvictionPolicy(new LRUEvictionPolicy())
                        .setMaxSizeConfig(new MaxSizeConfig().setSize(500))
                        .setMaxIdleSeconds(3000)
                        .setTimeToLiveSeconds(300))
                .addMapConfig(new MapConfig().setName(FILES_INFO_MAP)
                        .setMapEvictionPolicy(new LRUEvictionPolicy())
                        .setMaxSizeConfig(new MaxSizeConfig().setSize(500))
                        .setMaxIdleSeconds(3000)
                        .setTimeToLiveSeconds(300));
        hz = Hazelcast.newHazelcastInstance(hazelCfg);
    }

    IMap<String, ContextInfo> getContextsMap() {
        return hz.getMap(CONTEXTS_INFO_MAP);
    }

    IMap<String, FileInfo> getFilesMap() {
        return hz.getMap(FILES_INFO_MAP);
    }

    @Override
    public void close() throws Exception {
        log.trace("Shutdown");
        hz.shutdown();
    }
}
