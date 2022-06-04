package ssd.assignment.auctions;

import lombok.Getter;
import ssd.assignment.blockchain.Wallet;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.logging.Logger;

@Getter
public class Auction {

    /**
     * Entity to represent an Aucion
     * */

    private static final Logger logger = Logger.getLogger(Auction.class.getName());
    private final String itemID;
    private final String sellerID;
    private long minAmount;
    private float minIncrement;
    private long fee;
    private long timeout;
    private final PublicKey sellerPublicKey;
    private final String hash;
    private final byte[] signature; // TODO Não vai ser preciso

    public Auction(String itemID, String sellerID, long minAmount, float minIncrement, long fee, long timeout, PublicKey sellerPublicKey, String hash, byte[] signature) {
        this.itemID = itemID;
        this.sellerID = sellerID;
        this.minAmount = minAmount;
        this.minIncrement = minIncrement;
        this.fee = fee;
        this.timeout = timeout;
        this.sellerPublicKey = sellerPublicKey;
        this.hash = hash;
        this.signature = signature;
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
        this.signature = new byte[160]; //TODO hash signature! Not needed
    }

    @Override
    public String toString() {
        return "Auction{" +
                "itemID='" + itemID + '\'' +
                ", sellerID='" + sellerID + '\'' +
                ", minAmount=" + minAmount +
                ", minIncrement=" + minIncrement +
                ", fee=" + fee +
                ", timeout=" + timeout +
                ", sellerPublicKey=" + sellerPublicKey +
                ", hash='" + hash + '\'' +
                ", signature=" + Arrays.toString(signature) +
                '}';
    }

    public String toString2() {
        return "Auction{" +
                "itemID='" + itemID + '\'' +
                ", sellerID='" + sellerID + '\'' +
                ", minAmount=" + minAmount +"€" +
                ", minIncrement=" + minIncrement +"€" +
                ", timeout=" + timeout +
                '}';
    }

}
