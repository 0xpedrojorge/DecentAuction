package ssd.assignment.auctions;

import ssd.assignment.blockchain.Wallet;

import java.util.TreeSet;
import java.util.logging.Logger;

public class Manager implements Runnable{

    /**
     * Entity responsible for managing Auctions
     * */

    private static final Logger logger = Logger.getLogger(Manager.class.getName());

    private Auction auction;
    private Wallet wallet;
    private TreeSet<Bid> StatusBids;
    private Thread treadAuction;

    public Manager(Auction auction){
        this.auction=auction;
    }


    @Override
    public void run() {

        /*   Start Auction   */
        logger.info(" Starting Auction!");

        logger.info(auction.toString());

        logger.info(" Auction Ended!");
    }
}
