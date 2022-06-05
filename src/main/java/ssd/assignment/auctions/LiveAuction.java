package ssd.assignment.auctions;

import lombok.Getter;

import java.io.Serializable;
import java.util.*;

@Getter
public class LiveAuction implements Serializable {

    protected final Auction auction;
    protected TreeSet<Bid> bids = new TreeSet<>();

    public LiveAuction(Auction auction) {
        this.auction = auction;
    }

    public Bid getLastBid(){
        if (bids.isEmpty()) return null;
        else return bids.last();
    }


    public void printLiveAuction(){
        System.out.println("LiveAuciton: " + auction.toSimplifiedString());
        Bid latestBid = this.getLastBid();

        if(latestBid!=null){ System.out.println("Latest bid has the value: " + latestBid.getAmount() );}
        else{System.out.println("There are no Bids in place!");}
    }

    public void printLastBid(){
        Bid latestBid = bids.last();

        if(latestBid!=null){ System.out.println("Latest Bid has the current Info: " + latestBid);}
        else{System.out.println("There are no Bids in place!");}
    }

    public boolean checkForBid(Bid bid){
        return bids.contains(bid);
    }

    public void put(Bid bid) {
        if (bid !=null) {
            bids.add(bid);
        }
    }
}
