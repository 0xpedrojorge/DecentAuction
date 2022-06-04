package ssd.assignment.auctions;

import ssd.assignment.blockchain.Wallet;

import java.security.PublicKey;
import java.util.logging.Logger;

public class Bid {

    private static final Logger logger = Logger.getLogger(Bid.class.getName());

    private final String ItemId;
    private final String SellerId;
    private String BuyerID;
    private long Amount;
    //private long Fee;
    PublicKey BuyerPublicKey;
    private String Hash;
    //private byte[]  Signature;

    public Bid(String itemId, String sellerId, String buyerID, long amount, long fee, PublicKey buyerPublicKey, String hash, byte[] signature) {
        ItemId = itemId;
        SellerId = sellerId;
        BuyerID = buyerID;
        Amount = amount;
        //Fee = fee;
        BuyerPublicKey = buyerPublicKey;
        Hash = hash;
        //Signature = signature;
    }

    public Bid(Auction auction, Wallet buyer, long amount){
        ItemId=auction.getItemID();
        SellerId = auction.getSellerID();
        BuyerID = buyer.walletOwner;
        Amount = amount;
        //Fee = fee;
        BuyerPublicKey = buyer.publicKey;
        //Hash = hash;
        //Signature = signature;

    }

    public String getItemId() {
        return ItemId;
    }

    public String getSellerId() {
        return SellerId;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public long getAmount() {
        return Amount;
    }

    //public long getFee() {return Fee;}

    public PublicKey getBuyerPublicKey() {
        return BuyerPublicKey;
    }

    public String getHash() {
        return Hash;
    }

    //public byte[] getSignature() {return Signature;}

    @Override
    public String toString() {
        return "Bid{" +
                "ItemId='" + ItemId + '\'' +
                ", SellerId='" + SellerId + '\'' +
                ", BuyerID='" + BuyerID + '\'' +
                ", Amount=" + Amount +
                '}';
    }

}
