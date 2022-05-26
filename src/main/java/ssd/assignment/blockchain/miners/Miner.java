package ssd.assignment.blockchain.miners;

import ssd.assignment.blockchain.blocks.Block;
import ssd.assignment.util.Helper;
import ssd.assignment.util.Standards;

public class Miner extends Thread {

    private final MiningManager manager;
    private Block block;

    public Miner(MiningManager manager, Block block) {
        this.manager = manager;
        try {
            this.block = (Block) block.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        block.getHeader().merkleRoot = Helper.getMerkleRoot(block.getTransactions());
        String target = Helper.getDificultyString(Standards.MINING_DIFFICULTY);
        while(!block.getHeader().hash.substring( 0, Standards.MINING_DIFFICULTY).equals(target)) {
            if (isInterrupted()) break;

            block.getHeader().nonce ++;
            block.getHeader().hash = block.calculateHash();
        }

        if (!isInterrupted()) {
            System.out.println("Block Mined!!! : " + block.getHeader().hash);

            manager.notifyMinedBlock(block);
        }
    }
}
