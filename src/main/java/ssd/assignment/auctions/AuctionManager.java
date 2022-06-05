package ssd.assignment.auctions;

import lombok.Getter;
import ssd.assignment.DecentAuctionLedger;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.types.AuctionMessage;
import ssd.assignment.communication.messages.types.BidMessage;
import ssd.assignment.communication.messages.types.RequestLiveAuctionMessage;

import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Logger;

@Getter
public class AuctionManager {

    /**
     * Entity responsible for managing Auctions
     * */

    private static final Logger logger = Logger.getLogger(AuctionManager.class.getName());

    private static final Scanner stdin = new Scanner(System.in);

    private final HashMap<String, LiveAuction> liveAuctions;

    private Thread treadAuctionRunning;

    public AuctionManager(){
        liveAuctions = new HashMap<>();
    }

    public void sendAuctionsTo(KContact contact) {
        for(LiveAuction liveAuction : liveAuctions.values()){
            AuctionMessage message = new AuctionMessage(liveAuction);
            DecentAuctionLedger.getMessageManager().sendMessage(message, contact);
        }
    }

    public void addLiveAuction(LiveAuction liveAuction){
        if(liveAuctions.containsValue(liveAuction)){
            logger.info("Auction: " + liveAuction.getAuction().getItemID() + " already stored, failing to add!");
        }
        liveAuctions.put(liveAuction.getAuction().getItemID(), liveAuction);
    }

    public boolean hasAuctionItem(String Itemid){
        return liveAuctions.containsKey(Itemid);
    }

    public void addBid(Bid bid){
        /*
        Always had the bid. Allows to save second highest, in case highest doesn't pay
         */
        liveAuctions.get(bid.getItemId()).put(bid);
    }

    public void addLocalBid(Bid bid){
        addBid(bid);
        BidMessage message = new BidMessage(bid);
        DecentAuctionLedger.getMessageManager().publishMessage(message);
    }

    public void addLocalAuction(Auction auction){
        LiveAuction liveAuction=new LiveAuction(auction);
        addLiveAuction(liveAuction);
        AuctionMessage message=new AuctionMessage(liveAuction);
        DecentAuctionLedger.getMessageManager().publishMessage(message);
    }

    public TreeSet<Bid> getLiveAuctionBids(String LiveAuctionID){
        LiveAuction liveAuction = liveAuctions.get(LiveAuctionID);
        if(liveAuction!=null){return liveAuction.getBids();}
        else{return null;}
    }

    public void requestNetworkAuctions(){
        RequestLiveAuctionMessage request = new RequestLiveAuctionMessage();
        DecentAuctionLedger.getMessageManager().publishMessage(request);
    }

    public void pritnLastBidForAuction(String ItemID){
        LiveAuction liveAuction= liveAuctions.get(ItemID);
        if(liveAuction.getLastBid()!=null)
            liveAuction.printLastBid();
        else System.out.println("There are no Bids in place!");

    }

    public void pritnLiveAuctions(){
        for(LiveAuction liveaustion: liveAuctions.values()){
            liveaustion.printLiveAuction();
        }
    }

    /*public Auction getLiveAuction(String ItemID){
        LiveAuction liveAuction=LiveAuctions.get(ItemID);
        if(liveAuction.auction!=null) return liveAuction.getAuction();
        else return null;
    }*/

    public LiveAuction getLiveAuction(String ItemID){
        LiveAuction liveAuction = liveAuctions.get(ItemID);
        if(liveAuction.getAuction() != null) return liveAuction;
        else return null;
    }
}
