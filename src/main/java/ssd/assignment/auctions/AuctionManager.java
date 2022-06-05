package ssd.assignment.auctions;

import lombok.Getter;
import ssd.assignment.DecentAuctionLedger;
import ssd.assignment.blockchain.Wallet;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.MessageManager;
import ssd.assignment.communication.messages.types.AuctionMessage;

import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.logging.Logger;

@Getter
public class AuctionManager implements Runnable{

    /**
     * Entity responsible for managing Auctions
     * */

    private static final Logger logger = Logger.getLogger(AuctionManager.class.getName());

    private static Scanner stdin=new Scanner(System.in);

    private static final HashMap<String,LiveAuction> LiveAuctions = new HashMap<>();

    //TODO decide if its util
    private static final HashMap<String,Long> Transactions = new HashMap<>();

    private Thread treadAuctionRunning;

    public AuctionManager(){

    }


    //TODO Respond with all Auctions in Live Auction
    public void sendAuctionsTo(KContact contact) {
        for(LiveAuction liveauction : LiveAuctions.values()){
            AuctionMessage message = new AuctionMessage(liveauction.getAuction());
            DecentAuctionLedger.getMessageManager().sendMessage(message, contact);
        }
    }

    public void addLiveAuction(Auction auction){
        if(LiveAuctions.containsValue(auction)){
            logger.info(" Auction: "+auction.getItemID()+" already added! ");
        }
        LiveAuction newLiveAuction = new LiveAuction(auction);
        LiveAuctions.put(auction.getItemID(),newLiveAuction);
    }

    public static boolean hasAuctionItem(String Itemid){
        return LiveAuctions.containsKey(Itemid);
    }

    public void addBid(Bid bid){
        LiveAuction liveAuction = LiveAuctions.get(bid.getItemId());
        Bid lastBid=liveAuction.getLastBid();
        if(lastBid==null){
            LiveAuctions.get(bid.getItemId()).put(bid);
        }
    }

    public static TreeSet<Bid> getLiveAuctionBids(String LiveAuctionID){
        LiveAuction liveAuction= LiveAuctions.get(LiveAuctionID);
        if(liveAuction!=null){return liveAuction.getBids();}
        else{return null;}
    }


    //TODO Getting Network Auctions
    public void getNetworkAuctions(){
        //AuctionMessage message = new AuctionMessage(liveauction.getAuction());
        //DecentAuctionLedger.getMessageManager().publishMessage(message);
    }

    public static void pritnLastBidForAuction(String ItemID){
        LiveAuction liveAuction=LiveAuctions.get(ItemID);
        if(liveAuction.getLastBid()!=null)
            liveAuction.printLastBid();
        else System.out.println("There are no Bids in place!");

    }

    public static void pritnLiveAuctions(){
        for(LiveAuction liveaustion:LiveAuctions.values()){
            liveaustion.printLiveAuction();
        }
    }

    /*public static Auction getLiveAuction(String ItemID){
        LiveAuction liveAuction=LiveAuctions.get(ItemID);
        if(liveAuction.auction!=null) return liveAuction.getAuction();
        else return null;
    }*/

    public static LiveAuction getLiveAuction(String ItemID){
        LiveAuction liveAuction=LiveAuctions.get(ItemID);
        if(liveAuction.auction!=null) return liveAuction;
        else return null;
    }

    @Override
    public void run() {

        /*   Getting Network Auctions   */
        getNetworkAuctions();

        /*   Set my Auctions  */
        Wallet wallet =  new Wallet();

        Auction auction = new Auction(wallet,"banana", "José", 1L, 0.5F, (long) 0.1, 1);
        Auction auction2 = new Auction(wallet,"laranja", "Quim", 1L, 0.5F, (long) 0.1, 1);

        LiveAuction liveAuction = new LiveAuction(auction);
        LiveAuction liveAuction2 = new LiveAuction(auction2);

        LiveAuctions.put(auction.getItemID(),liveAuction);
        LiveAuctions.put(auction2.getItemID(),liveAuction2);

        for(LiveAuction liveaustion:LiveAuctions.values()){
            liveaustion.printLiveAuction();
        }

        /*   Start Auction   */
        System.out.print(" Starting Auction!");

        //logger.info(auction.toString());

        System.out.print(" Auction Ended!");
    }

}
