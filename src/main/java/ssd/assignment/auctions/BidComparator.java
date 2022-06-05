package ssd.assignment.auctions;

import java.util.Comparator;

public class BidComparator implements Comparator<Bid> {

    @Override
    public int compare(Bid bid1, Bid bid2){
        return Float.compare(bid1.getAmount(), bid2.getAmount());
    }
}
