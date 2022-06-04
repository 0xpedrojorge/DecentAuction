package ssd.assignment.auctions;

import ssd.assignment.blockchain.Wallet;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.logging.Logger;

public class Auction {

    /**
     * Entity to represent an Aucions
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
    private final byte[] signature;

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
        this.signature = new byte[160]; //TODO hash signature!
    }

    public String getItemID() {
        return itemID;
    }

    public String getSellerID() {
        return sellerID;
    }

    public long getMinAmount() {
        return minAmount;
    }

    public float getMinIncrement() {
        return minIncrement;
    }

    public long getFee() {
        return fee;
    }

    public long getTimeout() {
        return timeout;
    }

    public PublicKey getSellerPublicKey() {
        return sellerPublicKey;
    }

    public String getHash() {
        return hash;
    }

    public byte[] getSignature() {
        return signature;
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

}
