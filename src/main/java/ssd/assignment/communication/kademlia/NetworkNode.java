package ssd.assignment.communication.kademlia;

import lombok.Getter;
import ssd.assignment.communication.grpc.DecentAuctionClientManager;
import ssd.assignment.communication.grpc.DecentAuctionServer;
import ssd.assignment.util.Standards;

import java.io.IOException;

@Getter
public class NetworkNode {

    private final byte[] nodeId;
    private final int port;

    private final DecentAuctionServer server;
    private final DecentAuctionClientManager clientManager;

    private final KRoutingTable KRoutingTable;

    public NetworkNode(byte[] nodeId, int port) {
        this.nodeId = nodeId;
        this.port = port;

        server = new DecentAuctionServer();
        Thread serverBlockedThread = new Thread(() -> {
            try {
                server.start(this);
                server.blockUntilShutdown();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        serverBlockedThread.start();
        this.clientManager = new DecentAuctionClientManager();

        //this.KRoutingTable = new KRoutingTable(this);
        this.KRoutingTable = new KRoutingTable(this);

    }

    public final void bootstrap(NetworkNode n) throws IOException{

        /* create operation that connects Nodes */

        Operations op = new Operations(this.server, this, n);
        /*op.execute();*/

    }

    public int getDistance(byte[] nodeId){
        return Standards.B - this.xor(nodeId).getFirstSetBitIndex();
    }


    public NetworkNode xor(byte[] nid)
    {
        byte[] result = new byte[Standards.B / 8];
        byte[] nidBytes = nid;

        for (int i = 0; i < Standards.B / 8; i++)
        {
            result[i] = (byte) (this.nodeId[i] ^ nidBytes[i]);
        }

        NetworkNode resNid = new NetworkNode(result,Standards.DEFAULT_PORT);

        return resNid;
    }


    //TODO change this!
    /**
     * Counts the number of leading 0's in this NodeId
     *
     * @return Integer The number of leading 0's
     */
    public int getFirstSetBitIndex()
    {
        int prefixLength = 0;

        for (byte b : this.nodeId)
        {
            if (b == 0)
            {
                prefixLength += 8;
            }
            else
            {
                /* If the byte is not 0, we need to count how many MSBs are 0 */
                int count = 0;
                for (int i = 7; i >= 0; i--)
                {
                    boolean a = (b & (1 << i)) == 0;
                    if (a)
                    {
                        count++;
                    }
                    else
                    {
                        break;   // Reset the count if we encounter a non-zero number
                    }
                }

                /* Add the count of MSB 0s to the prefix length */
                prefixLength += count;

                /* Break here since we've now covered the MSB 0s */
                break;
            }
        }
        return prefixLength;
    }

}
