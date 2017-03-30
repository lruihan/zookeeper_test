package test.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZkConnect {

    private ZooKeeper zk;
    private CountDownLatch connSignal = new CountDownLatch(0);

    public ZooKeeper connect(String host) throws IOException, InterruptedException{
        zk = new ZooKeeper(host, 1200000, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    connSignal.countDown();
                }
            }
        });
        connSignal.await();
        return zk;
    }

    public void close() throws InterruptedException {
        zk.close();
    }

    public void createNode(String path, byte[] data) throws Exception {
        zk.create(path, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    public void updateNode(String path, byte[] data) throws Exception {
        zk.setData(path, data, zk.exists(path, true).getVersion());
    }

    public void deleteNode(String path) throws Exception {
        zk.delete(path, zk.exists(path, true).getVersion());
    }
    public static void main( String[] args ) throws Exception {
        ZkConnect zkConnect = new ZkConnect();
        ZooKeeper zooKeeper = zkConnect.connect("127.0.0.1");
        String newNode = "/testChange";
        zkConnect.createNode(newNode, new Date().toString().getBytes());
//        List<String> zNodes = zooKeeper.getChildren("/", true);
//        for(String zNode : zNodes) {
//            System.out.println("ChildrenNode: " + zNode);
//        }
//        byte[] data = zooKeeper.getData(newNode, true, zooKeeper.exists(newNode, true));
//        System.out.println("GetData before setting: " + new String(data));
//
//        zkConnect.updateNode(newNode, "Modified data".getBytes());
//        data = zooKeeper.getData(newNode, true, zooKeeper.exists(newNode, true));
//
//        System.out.println("GetData after setting: " + new String(data));
//
//        zkConnect.deleteNode(newNode);

        zkConnect.close();
    }
}
