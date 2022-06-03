import org.junit.Test;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.StoredData;
import ssd.assignment.communication.operations.PingOperation;
import ssd.assignment.communication.operations.StoreOperation;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

public class KademliaTests {

    @Test
    public void testBootstrap() {

        Random random = new Random();
        byte[] node1Id = new byte[Standards.KADEMLIA_ID_BIT_SIZE / Byte.SIZE];
        random.nextBytes(node1Id);
        byte[] node2Id = new byte[Standards.KADEMLIA_ID_BIT_SIZE / Byte.SIZE];
        random.nextBytes(node2Id);

        System.out.println(Utils.toHexString(node1Id));
        System.out.println(Utils.toHexString(node2Id));

        NetworkNode node1 = new NetworkNode(node1Id, 50050);
        NetworkNode node2 = new NetworkNode(node2Id, 50051);
        KContact node2AsContact;

        try {
            node2AsContact = new KContact(InetAddress.getLocalHost(), node2.getPort(), node2.getNodeId(), System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(2);
            node1.bootstrap(node2AsContact);
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Node1: " + node1.getRoutingTable().getAllContacts());
        System.out.println("Node2: " + node2.getRoutingTable().getAllContacts());


        PingOperation op1 = new PingOperation(node1, node2Id);
        op1.execute();

        PingOperation op2 = new PingOperation(node2, node1Id);
        op2.execute();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testStore() {

        Random random = new Random();
        byte[] node1Id = new byte[Standards.KADEMLIA_ID_BIT_SIZE / Byte.SIZE];
        random.nextBytes(node1Id);
        byte[] node2Id = new byte[Standards.KADEMLIA_ID_BIT_SIZE / Byte.SIZE];
        random.nextBytes(node2Id);

        System.out.println(Utils.toHexString(node1Id));
        System.out.println(Utils.toHexString(node2Id));

        NetworkNode node1 = new NetworkNode(node1Id, 50050);
        NetworkNode node2 = new NetworkNode(node2Id, 50051);
        KContact node2AsContact;

        try {
            node2AsContact = new KContact(InetAddress.getLocalHost(), node2.getPort(), node2.getNodeId(), System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(2);
            node1.bootstrap(node2AsContact);
            TimeUnit.SECONDS.sleep(6);
            System.out.println("Bootstrap is over");
            StoredData someData = new StoredData(Utils.toByteArray("A KEY"), Utils.toByteArray("A VALUE"), node1.getNodeId());
            System.out.println("Stored key: " + Utils.toHexString(someData.getKey()));
            StoreOperation op1 = new StoreOperation(node1, someData);
            op1.execute();
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException |
        UnknownHostException e) {
            throw new RuntimeException(e);
        }

        System.out.println(node2.getDht().getStoredPairs().size());

        for(String key : node2.getDht().getStoredPairs().keySet()) {
            System.out.println(key);
            System.out.println(Utils.toHexString(Utils.toByteArray(key)));
            System.out.println(Utils.toHexString(node2.getDht().getValueByKey(Utils.toByteArray(key))));
        }
        /*
        for(String key : node2.getDht().getStoredPairs().keySet()) {
            System.out.println(Utils.toHexString(node2.getDht().getValueByKey(Utils.toByteArray(key))));
        }

         */
    }
}
