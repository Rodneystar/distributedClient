package com.example.jdog.distributedClient;

import org.apache.curator.CuratorZookeeperClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestingCurator {

    @Test
    public void curator() throws Exception {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework cff = CuratorFrameworkFactory.newClient("192.168.99.100:2181", retryPolicy);
        cff.start();
        System.out.println(cff.getState().toString());
        cff.blockUntilConnected();

        try {
            String result = cff.create().forPath("/testpath", "testData".getBytes());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void clientCapabilities() {
        CuratorFramework client = setupClient();
        client.;
    }


    @Test
    public void testEphemeralNodeWatch() throws Exception {
        CuratorFramework client = setupClient();
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Optional<byte[]> stat = Optional.ofNullable(
                    client.getData()
                        .usingWatcher((CuratorWatcher) e -> {
                            System.out.println("event type: " + e.getType());
                            latch.countDown();
                        } )
                        .forPath("/testpath"));

            stat.ifPresent( s ->System.out.printf("data: %s\n", new String(s)));
        } catch (Exception e) {
            e.printStackTrace();
            latch.countDown();
        }

        latch.await();

        Optional<Stat> stat2 = Optional.ofNullable(client.checkExists().forPath("/testpath/etest"));
        stat2.ifPresent( s -> {

            System.out.printf("owner: %d\n", s.getEphemeralOwner());
        });

    }

    @Test
    public void getFromEnode() throws Exception {
        CuratorFramework client = setupClient();
        byte[] data = client.getData().forPath("/testpath/etest");
        System.out.printf("initialData: %s\n", new String(data));
    }

    @Test
    public void testwatcher() throws Exception {
        CuratorFramework client = setupClient();
        CountDownLatch latch = new CountDownLatch(1);
        byte[] data = client.getData().usingWatcher((CuratorWatcher) e -> {
            latch.countDown();
        })
                .forPath("/testpath");
        System.out.printf("initialData: %s\n", new String(data));

        latch.await(20, TimeUnit.SECONDS);

        data = client.getData().forPath("/testpath");
        System.out.printf("newData: %s\n", new String(data));

    }

    private CuratorFramework setupClient() {
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework cff = CuratorFrameworkFactory.newClient("192.168.99.100:2181", retryPolicy);
        cff.start();
        System.out.println(cff.getState().toString());
        try {
            cff.blockUntilConnected();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return cff;
    }
}
