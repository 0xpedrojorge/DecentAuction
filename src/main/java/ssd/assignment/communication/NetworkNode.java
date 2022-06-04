package ssd.assignment.communication;

import lombok.Getter;
import ssd.assignment.communication.grpc.DecentAuctionClientManager;
import ssd.assignment.communication.grpc.DecentAuctionServer;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.KDistributedHashTable;
import ssd.assignment.communication.kademlia.KRoutingTable;
import ssd.assignment.communication.operations.LookupOperation;
import ssd.assignment.util.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class NetworkNode {

    private final KContact self;

    private final DecentAuctionServer server;
    private final DecentAuctionClientManager clientManager;

    private final KRoutingTable routingTable;
    private final KDistributedHashTable dht;
    private final List<byte[]> seenMessages;

    public NetworkNode(byte[] nodeId, int port) {
        this.self = new KContact(Utils.getLocalAddress(), port, nodeId, System.currentTimeMillis());

        server = new DecentAuctionServer();
        Thread serverBlockedThread = new Thread(() -> {
            try {
                server.start(this, port);
                server.blockUntilShutdown();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
        serverBlockedThread.start();
        this.clientManager = new DecentAuctionClientManager();

        try {
            this.routingTable = new KRoutingTable(
                    new KContact(InetAddress.getLocalHost(), port, nodeId, System.currentTimeMillis())
            );
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        this.dht = new KDistributedHashTable();
        this.seenMessages = new ArrayList<>();
    }

    public void bootstrap(KContact contact) {
        /*
        Add bootstrap node to respective bucket
        and use it to lookup self
         */
        routingTable.insert(contact);
        new LookupOperation(this, getNodeId()).execute();
    }

    public byte[] getNodeId() {
        return self.getId();
    }

    public int getPort() {
        return self.getPort();
    }

    public boolean addToSeenMessages(byte[] messageId) {
        for (byte[] m : seenMessages) {
            if (Arrays.equals(m, messageId)) {
                return false;
            }
        }
        seenMessages.add(messageId);
        return true;
    }
}
