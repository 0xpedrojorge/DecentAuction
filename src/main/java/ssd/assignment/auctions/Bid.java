package ssd.assignment.auctions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.Wallet;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.security.PublicKey;
import java.util.logging.Logger;

@Getter
public class Bid implements Serializable {

    private final String itemId;
    //private final String sellerId;
    private final String buyerId;
    private final long amount;
    private transient final PublicKey buyerPublicKey;
    private String bidHash;

    public Bid(String itemId, String sellerId, String buyerId, long amount, PublicKey buyerPublicKey, String bidHash) {
        this.itemId = itemId;
        //this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.buyerPublicKey = buyerPublicKey;
        this.bidHash = bidHash;
    }

    public Bid(Auction auction, Wallet buyer, long amount){
        this.itemId =auction.getItemID();
        //this.sellerId = auction.getSellerID();
        this.buyerId = buyer.walletOwner;
        this.amount = amount;
        this.buyerPublicKey = buyer.publicKey;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().toJson(this);
    }

    public String toSimplifiedString() {
        return "Bid{" +
                "ItemId='" + itemId + '\'' +
                //", SellerId='" + sellerId + '\'' +
                ", BuyerID='" + buyerId + '\'' +
                ", Amount=" + amount +
                '}';
    }

}
