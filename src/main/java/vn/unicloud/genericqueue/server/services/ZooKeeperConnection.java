package vn.unicloud.genericqueue.server.services;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperConnection {
    private ZooKeeper zoo;
    final CountDownLatch connectedSignal = new CountDownLatch(1);

    public ZooKeeper connect(String host, int port) throws IOException, InterruptedException {
        zoo = new ZooKeeper(host, port, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }

            ;
        });
        connectedSignal.await();
        return zoo;
    }

    public void close() throws InterruptedException {
        zoo.close();
    }
}
