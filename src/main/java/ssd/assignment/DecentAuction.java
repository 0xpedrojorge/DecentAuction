package ssd.assignment;

import ssd.assignment.blockchain.Block;
import ssd.assignment.blockchain.Blockchain;

public class DecentAuction {

    public static void main(String[] args) {

        Blockchain chain = new Blockchain();
        chain.append(new Block(chain.getRootBlock().getIndex()+1, "Second block", ""));
        chain.append(new Block(chain.getRootBlock().getIndex()+1, "Third block", ""));

        for (Block b : chain.getChain()) {
            System.out.println("Block " + b.getIndex() + ": " + b.getHash());
        }
    }
}
