package org.ngsandbox.zookeeper.test;

import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;
import org.ngsandbox.zookeeper.Listener;
import org.ngsandbox.zookeeper.Result;
import org.ngsandbox.zookeeper.ZooConfigMonitor;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.TemporaryFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@DisplayName("Zookeeper config test")
class ZooConfigMonitorTest extends BaseZooTest {

    private static final Logger LOG = LoggerFactory.getLogger(ZooConfigMonitorTest.class);

    private String connectString = "127.0.0.1:2181";
    private String znode = "/testmessages";

    private TestingServer zkTestServer;

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    @Test
    void testEphemeralNodes() throws Exception {
        try (ZooConfigMonitor<Integer> monitor = new ZooConfigMonitor<>(connectString, znode, Integer.class, CreateMode.EPHEMERAL)) {
            monitor.startSubscriber(new Listener<Integer>() {
                @Override
                public void removed(String s) {
                    LOG.trace("Removed from " + s);
                }

                @Override
                public void added(String s, Integer model) {
                    LOG.trace("Added to " + s + " value " + model);
                }

                @Override
                public void updated(String s, Integer model) {
                    LOG.trace("Updated into " + s + " value " + model);
                }
            });

            for (int i = 0; i < 100; i++) {
                try (ZooConfigMonitor<Integer> tMonitor = new ZooConfigMonitor<>(connectString, znode, Integer.class, CreateMode.EPHEMERAL)) {
                    tMonitor.save(String.valueOf(i), i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Assert.assertEquals(monitor.getChildrenNodes().size(), 0);
        }
    }


    @Test
    void testSaveMessage() throws Exception {
        String testMessage = "ISO1/ISO2";
        try (ZooConfigMonitor<String> monitor = new ZooConfigMonitor<>(connectString, znode, String.class)) {
            String key = testMessage.replaceAll("/", "");
            Result<String> result = monitor.save(key, testMessage);
            LOG.trace(result.toString());
            Assert.assertNull(result.getException());

            testMessage += " test";
            result = monitor.save(key, testMessage);
            Assert.assertNull(result.getException());

            result = monitor.read(key);
            Assert.assertNull(result.getException());

            Assert.assertEquals("Outcome and saved messages are not the same", testMessage, result.getData());
        }
    }

    @Test
    void testSaveRoot() throws Exception {
        String testMessage = "ISO1/ISO2";
        try (ZooConfigMonitor<String> monitor = new ZooConfigMonitor<>(connectString, znode, String.class)) {
            Result<String> result = monitor.save(testMessage);
            LOG.trace(result.toString());
            Assert.assertNull(result.getException());

            result = monitor.read();
            Assert.assertNull(result.getException());
            Assert.assertEquals("Outcome and saved messages are not the same", testMessage, result.getData());
        }
    }
}
