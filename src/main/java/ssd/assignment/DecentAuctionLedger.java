package ssd.assignment;

import lombok.Getter;
import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.blockchain.miners.MiningManager;
import ssd.assignment.communication.NetworkNode;

public class DecentAuctionLedger {
    @Getter
    private static BlockChain blockchain;
    private MiningManager miningManager;

    private NetworkNode networkNode;

    public DecentAuctionLedger(String[] args) {

        startNetwork();
        //startBlockchain();

    }

    private void startBlockchain() {
        blockchain = new BlockChain();
        miningManager = new MiningManager(blockchain);
    }

    private void startNetwork() {

    }

    public static void main(String[] args) {
        new DecentAuctionLedger(args);
    }
}
