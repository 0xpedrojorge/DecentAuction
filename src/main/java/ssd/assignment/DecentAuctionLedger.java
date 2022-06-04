package ssd.assignment;

import lombok.Getter;
import ssd.assignment.blockchain.Wallet;
import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.blockchain.miners.MiningManager;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.blockchain.transactions.TxOutput;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.TransactionMessage;
import ssd.assignment.communication.operations.BroadcastMessageOperation;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class DecentAuctionLedger {

    @Getter
    private static BlockChain blockchain;
    @Getter
    private static MiningManager miningManager;
    @Getter
    private static NetworkNode networkNode;

    public DecentAuctionLedger(String[] args) {
        if (args.length == 0) {
            System.out.println("Arguments not found");
            return;
        }

        startNetwork(Integer.parseInt(args[0]));
        startBlockchain();
    }

    private void startBlockchain() {
        blockchain = new BlockChain();
        miningManager = new MiningManager(blockchain);
    }

    private void startNetwork(int portDelta) {
        System.out.println(portDelta);
    }

    public static void main(String[] args) {
        new DecentAuctionLedger(args);
    }
}
