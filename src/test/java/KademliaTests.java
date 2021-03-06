import org.junit.Test;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.StoredData;
import ssd.assignment.communication.operations.*;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class KademliaTests {

    @Test
    public void testBootstrap() {

        NetworkNode node1 = new NetworkNode(Standards.DEFAULT_NODE_ID, 50050);
        NetworkNode node2 = new NetworkNode(null, 50051);
        KContact node2AsContact;

        try {
            node2AsContact = new KContact(InetAddress.getLocalHost(), node2.getPort(), node2.getNodeId(), System.currentTimeMillis());
            node1.bootstrap(node2AsContact);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        PingOperation op1 = new PingOperation(node1, node2.getNodeId());
        op1.execute();

        PingOperation op2 = new PingOperation(node2, node1.getNodeId());
        op2.execute();

        System.out.println("Node1: " + node1.getRoutingTable().getAllContacts());
        System.out.println("Node2: " + node2.getRoutingTable().getAllContacts());
    }

    @Test
    public void testStore() {

        NetworkNode node1 = new NetworkNode(Standards.DEFAULT_NODE_ID, 50050);
        NetworkNode node2 = new NetworkNode(null, 50051);
        KContact node2AsContact;

        try {
            node2AsContact = new KContact(InetAddress.getLocalHost(), node2.getPort(), node2.getNodeId(), System.currentTimeMillis());
            node1.bootstrap(node2AsContact);
            System.out.println("Bootstrap is over");
            System.out.println("Node1: " + node1.getRoutingTable().getAllContacts());
            System.out.println("Node2: " + node2.getRoutingTable().getAllContacts());

            StoredData someData = new StoredData(Utils.toByteArray("A KEY"), Utils.toByteArray("A VALUE"), node1.getNodeId());
            System.out.println("Stored key: " + Utils.toHexString(someData.getKey()));
            System.out.println("Stored value: " + Utils.toHexString(someData.getValue()));

            StoreOperation op1 = new StoreOperation(node1, someData);
            op1.execute();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Pairs stored in node 1");
        for(byte[] key : node1.getDht().getStoredPairs().keySet()) {
            System.out.print("- Key content: " + new String(key) +", ");
            System.out.println("Value content: " + new String(node1.getDht().getValueByKey(key).getValue()));
        }
        System.out.println("Pairs stored in node 2");
        for(byte[] key : node2.getDht().getStoredPairs().keySet()) {
            System.out.print("- Key content: " + new String(key) +", ");
            System.out.println("Value content: " + new String(node1.getDht().getValueByKey(key).getValue()));
        }
    }

    @Test
    public void testContentLookup() {

        NetworkNode node1 = new NetworkNode(Standards.DEFAULT_NODE_ID, 50050);
        NetworkNode node2 = new NetworkNode(null, 50051);
        KContact node2AsContact;

        try {
            node2AsContact = new KContact(InetAddress.getLocalHost(), node2.getPort(), node2.getNodeId(), System.currentTimeMillis());
            node1.bootstrap(node2AsContact);
            System.out.println("Bootstrap is over");
            System.out.println("Node1: " + node1.getRoutingTable().getAllContacts());
            System.out.println("Node2: " + node2.getRoutingTable().getAllContacts());

            StoredData someData = new StoredData(Utils.toByteArray("A KEY"), Utils.toByteArray("A VALUE"), node1.getNodeId());
            System.out.println("Stored key: " + Utils.toHexString(someData.getKey()));
            System.out.println("Stored value: " + Utils.toHexString(someData.getValue()));

            node1.getDht().storePair(Utils.toByteArray("A KEY"), Utils.toByteArray("A VALUE"), node1.getNodeId());
            ContentLookupOperation op2 = new ContentLookupOperation(node2, someData.getKey());
            op2.execute();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Pairs stored in node 1");
        for(byte[] key : node1.getDht().getStoredPairs().keySet()) {
            System.out.print("- Key content: " + new String(key) +", ");
            System.out.println("Value content: " + new String(node1.getDht().getValueByKey(key).getValue()));
        }
        System.out.println("Pairs stored in node 2");
        for(byte[] key : node2.getDht().getStoredPairs().keySet()) {
            System.out.print("- Key content: " + new String(key) +", ");
            System.out.println("Value content: " + new String(node1.getDht().getValueByKey(key).getValue()));
        }
    }

    @Test
    public void testSendMessage() {

        NetworkNode node1 = new NetworkNode(Standards.DEFAULT_NODE_ID, 50050);
        NetworkNode node2 = new NetworkNode(null, 50051);
        KContact node2AsContact;

        try {
            node2AsContact = new KContact(InetAddress.getLocalHost(), node2.getPort(), node2.getNodeId(), System.currentTimeMillis());
            node1.bootstrap(node2AsContact);
            System.out.println("Bootstrap is over");
            System.out.println("Node1: " + node1.getRoutingTable().getAllContacts());
            System.out.println("Node2: " + node2.getRoutingTable().getAllContacts());

            String testMessage = "\nOi, fish face!\n*falls down stairs*\nLook what I got\n(Singing) I got a jar of sand";

            SendMessageOperation op3 = new SendMessageOperation(node1, node2.getNodeId(), Utils.toByteArray(testMessage));
            op3.execute();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testBroadcastMessage() {

        NetworkNode node1 = new NetworkNode(Standards.DEFAULT_NODE_ID, 50050);
        NetworkNode node2 = new NetworkNode(null, 50051);
        NetworkNode node3 = new NetworkNode(null, 50052);
        KContact node2AsContact;
        KContact node3AsContact;

        try {
            node2AsContact = new KContact(InetAddress.getLocalHost(), node2.getPort(), node2.getNodeId(), System.currentTimeMillis());
            node3AsContact = new KContact(InetAddress.getLocalHost(), node3.getPort(), node3.getNodeId(), System.currentTimeMillis());
            node1.bootstrap(node2AsContact);
            node1.bootstrap(node3AsContact);
            System.out.println("Bootstrap is over");
            System.out.println("Node1: " + node1.getRoutingTable().getAllContacts());
            System.out.println("Node2: " + node2.getRoutingTable().getAllContacts());
            System.out.println("Node3: " + node3.getRoutingTable().getAllContacts());

            String testMessage = "\nOi, fish face!\n*falls down stairs*\nLook what I got\n(Singing) I got a jar of dirt!";

            BroadcastMessageOperation op3 =
                    new BroadcastMessageOperation(node1, 0, Utils.toByteArray(Crypto.hash(testMessage)),  Utils.toByteArray(testMessage));

            op3.execute();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

    }
}
