package ssd.assignment.auctions;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.Wallet;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.security.PublicKey;
import java.util.logging.Logger;

@Getter
public class Auction implements Serializable {

    /**
     * Entity to represent an Aucion
     * */
    private final String itemID;
    private final String sellerID;
    private final long minAmount;
    private final float minIncrement;
    private final long fee;
    private final long timeout;
    private final transient PublicKey sellerPublicKey;
    private final String hash;

    public Auction(String itemID, String sellerID, long minAmount, float minIncrement, long fee, long timeout, PublicKey sellerPublicKey, String hash) {
        this.itemID = itemID;
        this.sellerID = sellerID;
        this.minAmount = minAmount;
        this.minIncrement = minIncrement;
        this.fee = fee;
        this.timeout = timeout;
        this.sellerPublicKey = sellerPublicKey;
        this.hash = hash;
    }

    public Auction(String itemID, String sellerID, long minAmount, float minIncrement, long timeout, PublicKey sellerPublicKey, String hash) {
        this.itemID = itemID;
        this.sellerID = sellerID;
        this.minAmount = minAmount;
        this.minIncrement = minIncrement;
        this.fee = 0;
        this.timeout = timeout;
        this.sellerPublicKey = sellerPublicKey;
        this.hash = hash;
    }

    public Auction(Wallet wallet ,String itemID, String sellerID, long minAmount, float minIncrement, long fee, long timeout) {
        this.itemID = itemID;
        this.sellerID = sellerID;
        this.minAmount = minAmount;
        this.minIncrement = minIncrement;
        this.fee = fee;
        this.timeout = timeout;
        this.sellerPublicKey = wallet.publicKey;
        this.hash = wallet.getPublicKeyHash();
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).create().toJson(this);
    }

    public String toSimplifiedString() {
        return "Auction{" +
                "itemID='" + itemID + '\'' +
                ", sellerID='" + sellerID + '\'' +
                ", minAmount=" + minAmount +"€" +
                ", minIncrement=" + minIncrement +"€" +
                ", timeout=" + timeout +
                '}';
    }

}
