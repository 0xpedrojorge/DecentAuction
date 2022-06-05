package ssd.assignment.auctions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.Wallet;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.security.PublicKey;

@Getter
public class Bid implements Serializable, Comparable {

    private final String itemId;
    //private final String sellerId;
    private final String buyerId;
    private final float amount;
    private transient final PublicKey buyerPublicKey;
    private String buyerPublicKeyHash;

    public Bid(String itemId, String sellerId, String buyerId, float amount, PublicKey buyerPublicKey, String buyerPublicKeyHash) {
        this.itemId = itemId;
        //this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.amount = amount;
        this.buyerPublicKey = buyerPublicKey;
        this.buyerPublicKeyHash = buyerPublicKeyHash;
    }

    public Bid(Auction auction, Wallet buyer, float amount){
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

    @Override
    public int compareTo(Object o) {
        return Float.compare(this.getAmount(), ((Bid) o).getAmount());
    }
}
