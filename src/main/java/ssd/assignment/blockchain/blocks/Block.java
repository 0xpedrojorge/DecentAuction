package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.Helper;

@Getter
public class Block {

    public final BlockHeader header;
    private final String transactions;

    public Block(long blockNumber, short version, String parentHash, String transactions) {
        this.header = new BlockHeader(blockNumber, version, parentHash);
        this.transactions = transactions;

        this.header.hash = calculateHash();
    }

    public String calculateHash() {
        return Helper.toHexString(Crypto.hash(this.toString()));
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');

        while(!header.hash.substring( 0, difficulty).equals(target)) {
            header.setNonce(header.getNonce() + 1);
            header.setHash(calculateHash());
        }
        System.out.println("Block Mined!!! : " + header.getHash());
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
