package ssd.assignment.auctions;

import java.util.*;
import java.util.logging.Logger;

public class LiveAuctions {

    private static final Logger logger = Logger.getLogger(LiveAuctions.class.getName());

    private static final HashMap<String,LiveAuction> LIVE_AUCTION_MAP = new HashMap<>();

    private static final HashMap<String,Long> Transactions = new HashMap<>();

    public static void addAuction(Auction auction){
        //TODO verify if auction is valid

        if(LIVE_AUCTION_MAP.containsKey(auction.getItemID())){
            System.out.println(" Auction: "+auction.getItemID() +" was already added to LiveAuctions! ");
            return;
        }
        LiveAuction newliveAuction = new LiveAuction(auction);
        LIVE_AUCTION_MAP.put(auction.getItemID(), newliveAuction );
        System.out.println(" New Auction: "+auction.getItemID() +" was added to LiveAuctions! ");
    }

    public static boolean hasAuctionItem(String Itemid){
        return LIVE_AUCTION_MAP.containsKey(Itemid);
    }


    /*public static boolean updateBid(Bid bid){

        // check if LiveAuctions are up to date
        // check if Auction is still live
        //



        return true;
    }*/
    // CheckAuctionGoing
    // CheckWinnigbid
    // verifyBidIInChain
    // verifyBidInAuction
    // addTotalWalletTrans
    // removeTotalWalletTrans
    // updateTotalWalletTrans
    // updateLiveAuction

    // isNameValid


    public static Auction getLiveAuction(String ItemID){
        LiveAuction liveAuction=LIVE_AUCTION_MAP.get(ItemID);
        if(liveAuction.auction!=null) return liveAuction.getAuction();
        else return null;
    }


    public static ArrayList<LiveAuction> getLiveAuctions(){
        return new ArrayList<>(LIVE_AUCTION_MAP.values());
    }


    public static TreeSet<Bid> getLiveAuctionBids(String LiveAuctionID){
        LiveAuction liveAuction= LIVE_AUCTION_MAP.get(LiveAuctionID);
        if(liveAuction!=null){return liveAuction.getBids();}
        else{return null;}
    }


    public static void pritnLiveAuctions(){
        for(LiveAuction liveaustion:LIVE_AUCTION_MAP.values()){
            liveaustion.printLiveAuction();
        }
    }

    public static void pritnLastBidForAuction(String ItemID){
        LiveAuction liveAuction=LIVE_AUCTION_MAP.get(ItemID);
        if(liveAuction.getLastBid()!=null)
            liveAuction.printLastBid();
        else System.out.println("There are no Bids in place!");

    }


}

class LiveAuction {

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


    public void printLiveAuction(){
        System.out.println("LiveAuciton: "+auction.toString2());
        Bid latestBid = this.getLastBid();

        if(latestBid!=null){ System.out.println("Latest Bid has the value: " + latestBid.getAmount() );}
        else{System.out.println("There are no Bids in place!");}
    }

    public void printLastBid(){
        Bid latestBid = bids.last();
        //System.out.println("teste last bid: " + latestBid.toString() );

        if(latestBid!=null){ System.out.println("Latest Bid has the current Info: " + latestBid);}
        else{System.out.println("There are no Bids in place!");}
    }

    public boolean checkForBid(Bid bid){
        return bids.contains(bid);
    }

    static class compareBid implements Comparator<Bid>{
        public int compare(Bid bid1, Bid bid2){
            return Long.compare(bid1.getAmount(), bid2.getAmount());
        }
    }





}
