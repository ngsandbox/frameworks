package org.ngsandbox.zookeeper.test;

import org.apache.curator.test.InstanceSpec;
import org.apache.curator.test.TestingServer;
import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;

public abstract class BaseZooTest {

    private TestingServer zkTestServer;
    private String connectString;

    public String getConnectString() {
        return connectString;
    }

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();


    @BeforeEach
    void init() throws Exception {
        tmpFolder.create();
        InstanceSpec newInstanceSpec = new InstanceSpec(tmpFolder.getRoot(), 2181, -1, -1, true, -1);
        zkTestServer = new TestingServer(newInstanceSpec, true);
        zkTestServer.start();
        connectString = zkTestServer.getConnectString();
    }


    @AfterEach
    void close() {
        try {
            zkTestServer.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tmpFolder.delete();
    }

}
