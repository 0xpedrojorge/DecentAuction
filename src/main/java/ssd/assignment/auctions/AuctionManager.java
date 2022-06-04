package ssd.assignment.auctions;

import ssd.assignment.DecentAuctionLedger;
import ssd.assignment.blockchain.Wallet;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.MessageManager;
import ssd.assignment.communication.messages.types.AuctionMessage;

import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Logger;

public class AuctionManager implements Runnable{

    /**
     * Entity responsible for managing Auctions
     * */

    private static final Logger logger = Logger.getLogger(AuctionManager.class.getName());

    private static Scanner stdin=new Scanner(System.in);

    private Auction auction;
    private Wallet wallet;
    private TreeSet<Bid> StatusBids;

    private Thread treadAuctionRunning;

    public AuctionManager(){
        this.auction=auction;
        LiveAuctions.addAuction(auction);
        this.StatusBids=LiveAuctions.getLiveAuctionBids(auction.getItemID());
    }


    @Override
    public void run() {

        /*   Start Auction   */
        logger.info(" Starting Auction!");

        logger.info(auction.toString());

        logger.info(" Auction Ended!");
    }

    public void sendAuctionsTo(KContact contact) {
        //for every auction in live auctions:
            AuctionMessage message = new AuctionMessage(auction);
            DecentAuctionLedger.getMessageManager().sendMessage(message, contact);
    }


    public Auction getAuction() {
        return auction;
    }

    public Thread getTreadAuctionRunning() {
        return treadAuctionRunning;
    }
}
