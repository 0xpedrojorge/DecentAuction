package ssd.assignment.auctions;

import java.util.*;
import java.util.logging.Logger;

public class LiveAuctions {

    private static final Logger logger = Logger.getLogger(LiveAuctions.class.getName());

    private static final HashMap<String,LiveAuction> LIVE_AUCTION_MAP = new HashMap<>();

    private static final HashMap<String,Long> Transactions = new HashMap<>();

    // add auction
    // updateBid
    // CheckAuctionGoing
    // CheckWinnigbid
    // verifyBidIInChain
    // verifyBidInAuction
    // addTotalWalletTrans
    // removeTotalWalletTrans
    // updateTotalWalletTrans
    // updateLiveAuction

    // isNameValid


    //TODO Test!
    public static ArrayList<LiveAuction> getLiveAuctions(){
        return new ArrayList<>(LIVE_AUCTION_MAP.values());
    }

    //TODO Test!
    public static TreeSet<Bid> getLiveAuctionBids(String LiveAuctionID){
        LiveAuction liveAuction= LIVE_AUCTION_MAP.get(LiveAuctionID);
        if(liveAuction!=null){return liveAuction.getBids();}
        else{return null;}
    }

    //TODO Test!
    public static void pritnLiveAuctions(){
        for(LiveAuction liveaustion:LIVE_AUCTION_MAP.values()){
            liveaustion.printLiveAuction(logger);
        }
    }



}

class LiveAuction{

    Auction auction;
    TreeSet<Bid> bids = new TreeSet<>();

    public LiveAuction(Auction auction) {
        this.auction = auction;
    }

    public Auction getAuction() {return auction;}

    public TreeSet<Bid> getBids() {return bids;}

    public Bid getLastBid(){
        if(bids.isEmpty()) return null;
        else return bids.last();
    }

    //TODO test!
    public void printLiveAuction(Logger logger){
        logger.info("LiveAuciton: "+auction.getItemID());
        Bid latestBid = this.getLastBid();

        if(latestBid!=null){ logger.info("Latest Bid has the value: " + latestBid.getAmount() );}
        else{logger.info("There are no Bids in place!");}
    }

    //checkUnique chack for a bid in the list
    //Bid Compare   class for compare Bids


}
