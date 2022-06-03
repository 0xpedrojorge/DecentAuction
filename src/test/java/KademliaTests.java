import org.junit.Test;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.operations.PingOperation;
import ssd.assignment.util.Standards;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class KademliaTests {

    @Test
    public void testBootstrap() {
        Random random = new Random();
        byte[] node1Id = new byte[Standards.KADEMLIA_ID_BIT_SIZE / Byte.SIZE];
        random.nextBytes(node1Id);
        byte[] node2Id = new byte[Standards.KADEMLIA_ID_BIT_SIZE / Byte.SIZE];
        random.nextBytes(node2Id);

        NetworkNode node1 = new NetworkNode(node1Id, 50050);
        NetworkNode node2 = new NetworkNode(node2Id, 50051);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        PingOperation op1 = new PingOperation(node1, node1.getNodeId());
        op1.execute();

        PingOperation op2 = new PingOperation(node2, node2.getNodeId());
        op2.execute();

        //TODO setup NodeLookupOperation and bootstrap node1 with node2

    }
}
