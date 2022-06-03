package ssd.assignment.communication;

import lombok.Getter;
import ssd.assignment.communication.grpc.DecentAuctionClientManager;
import ssd.assignment.communication.grpc.DecentAuctionServer;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.KRoutingTable;
import ssd.assignment.communication.operations.LookupOperation;
import ssd.assignment.util.Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Getter
public class NetworkNode {

    private final KContact self;

    private final DecentAuctionServer server;
    private final DecentAuctionClientManager clientManager;

    private final KRoutingTable routingTable;

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
}
