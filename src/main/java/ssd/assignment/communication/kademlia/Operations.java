package ssd.assignment.communication.kademlia;

import ssd.assignment.communication.grpc.DecentAuctionServer;

import java.io.IOException;
import java.util.logging.Logger;

public class Operations {

    private static final Logger logger = Logger.getLogger(DecentAuctionServer.class.getName());
    private final NetworkNode bootstrapNode;
    private final NetworkNode localNode;
    private final DecentAuctionServer server;

    private int attempts;

    public Operations( DecentAuctionServer server,NetworkNode bootstrapNode, NetworkNode localNode) {
        this.bootstrapNode = bootstrapNode;
        this.localNode = localNode;
        this.server = server;
    }

    public synchronized void execute() throws IOException {

        this.attempts=0;
        logger.info("Started Bootstrap Node");

        /*
        * -Check if our server is on
        * -Check if our server to receive is not null
        * -Setup the receiver to handle message response
        * -send the message
        * */





    }



}
