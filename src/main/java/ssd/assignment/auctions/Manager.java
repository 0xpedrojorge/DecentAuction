package ssd.assignment.auctions;

import ssd.assignment.blockchain.Wallet;

import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Logger;

public class Manager implements Runnable{

    /**
     * Entity responsible for managing Auctions
     * */

    private static final Logger logger = Logger.getLogger(Manager.class.getName());

    private static Scanner stdin=new Scanner(System.in);

    private Auction auction;
    private Wallet wallet;
    private TreeSet<Bid> StatusBids;

    private Thread treadAuctionRunning;

    public Manager(Auction auction){
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


    public Auction getAuction() {
        return auction;
    }

    public Thread getTreadAuctionRunning() {
        return treadAuctionRunning;
    }
}
