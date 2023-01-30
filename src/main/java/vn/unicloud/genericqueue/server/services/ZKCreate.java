package vn.unicloud.genericqueue.server.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ZKCreate {
    private static ZooKeeper zooKeeper;
    private static ZooKeeperConnection connection;

    @Value("${zookeeper.host}")
    private String host;
    @Value("${zookeeper.port}")
    private int port;

    public void afterPropertiesSet() throws Exception {
        // znode path
        String path = "/MyFirstZnode"; // Assign path to znode

        // data in byte array
        byte[] data = "My first zookeeper app”.getBytes()".getBytes(); // Declare data

        try {
            connection = new ZooKeeperConnection();
            zooKeeper = connection.connect(host, port);
            create(path, data);
            Stat stat = zooKeeper.exists(path, true);
            log.info("state {}", stat);
            if (stat != null) {
                byte[] bytes = zooKeeper.getData(path, new Watcher() {
                    @Override
                    public void process(WatchedEvent event) {
                        if (event.getType() == Event.EventType.None) {
                            switch (event.getState()) {
                                case Expired:
                                    connection.connectedSignal.countDown();
                                    break;
                            }
                        } else {
                            connection.connectedSignal.countDown();
                        }
                    }
                }, null);

                String d = new String(bytes, "UTF-8");
                log.info(d);
                connection.connectedSignal.await();
            } else {
                log.error("Node does not exists");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage()); //Catch error message
        }
    }

    public static void create(String path, byte[] data) throws InterruptedException, KeeperException {
        zooKeeper.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

}
