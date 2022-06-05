package ssd.assignment.auctions;

import lombok.Getter;

import java.util.*;

@Getter
public class LiveAuction {

    Auction auction;
    TreeSet<Bid> bids = new TreeSet<>();

    public LiveAuction(Auction auction) {
        this.auction = auction;
    }

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

    public void put(Bid bid) {
        if(bid !=null){
            bids.add(bid);
        }
    }

    static class compareBid implements Comparator<Bid>{
        public int compare(Bid bid1, Bid bid2){
            return Long.compare(bid1.getAmount(), bid2.getAmount());
        }
    }





}
