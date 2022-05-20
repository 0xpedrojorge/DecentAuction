package ssd.assignment.blockchain.blocks;

import lombok.Getter;
import ssd.assignment.util.Helper;

@Getter
public class Block {

    private final BlockHeader header;
    private String transactions;

    public Block() {
        this.header = new BlockHeader();
        this.transactions = "";
    }

    public byte[] mineBlock() {
        String prefix = new String(new char[this.header.getDifficulty()]).replace('\0', '0');
        this.header.setTimestamp(System.currentTimeMillis());
        while(!Helper.toHexString(this.header.getHash()).substring(0, this.header.getDifficulty()).equals(prefix)) {
            this.header.setNonce(this.header.getNonce()+1);
        }
        return this.header.getHash();
    }



    public void setParentHash(byte[] parentHash) {
        this.header.setParentHash(parentHash);
    }

    public byte[] getHash() {
        return this.header.getHash();
    }

    public String toString() {
        return this.header.toString() + this.transactions;
    }
}
