package ssd.assignment;

import lombok.Getter;
import ssd.assignment.auctions.Auction;
import ssd.assignment.auctions.AuctionManager;
import ssd.assignment.auctions.Client;
import ssd.assignment.auctions.LiveAuction;
import ssd.assignment.blockchain.Wallet;
import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.blockchain.miners.MiningManager;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.blockchain.transactions.TxOutput;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.MessageManager;
import ssd.assignment.communication.messages.types.BlockMessage;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.util.concurrent.TimeUnit;


public class DecentAuctionLedger {

    @Getter
    private static BlockChain blockchain;
    @Getter
    private static MiningManager miningManager;
    @Getter
    private static NetworkNode networkNode;
    @Getter
    public static AuctionManager auctionManager;
    @Getter
    private static MessageManager messageManager;
    @Getter
    private static Wallet wallet;

    public DecentAuctionLedger(String[] args) {
        if (args.length == 0) {
            System.out.println("Arguments not found");
            return;
        }

        startNetwork(Integer.parseInt(args[0]));
        startBlockchain();
        startAuctionManager();
        startMessageManager();

        startLocalAuctionsDemo(Integer.parseInt(args[0]));
        //testTransactionsAndBlockBroadcast(Integer.parseInt(args[0]));
    }

    private void startNetwork(int portDelta) {
        if (portDelta == 0 ) {
            networkNode = new NetworkNode(Standards.DEFAULT_NODE_ID, Standards.DEFAULT_PORT + portDelta);
        } else {
            networkNode = new NetworkNode(null, Standards.DEFAULT_PORT + portDelta);
            KContact bootstrapNode =
                    new KContact(Utils.getLocalAddress(), Standards.DEFAULT_PORT, Standards.DEFAULT_NODE_ID, System.currentTimeMillis());
            Thread thread = new Thread(() -> networkNode.bootstrap(bootstrapNode));
            thread.start();
        }
        System.out.println("Started the network");
    }

    private void startBlockchain() {
        blockchain = new BlockChain();
        miningManager = new MiningManager(blockchain);
        System.out.println("Started the blockchain");
    }

    private void startMessageManager() {
        messageManager = new MessageManager(networkNode, blockchain, auctionManager);

        networkNode.getServer().registerMessageConsumer(messageManager::receiveMessage);

        miningManager.registerBlockConsumer((block) -> {
            BlockMessage blockMessage = new BlockMessage(block);
            messageManager.publishMessage(blockMessage);
        });
        System.out.println("Started message manager");
    }

    private void startAuctionManager() {
        auctionManager = new AuctionManager();
        System.out.println("Started AuctionManager");
    }

    private void transactionsAndBlockBroadcastDemo(int portDelta) {
        if (portDelta == 1) {
            simulateFewTransactions();
        } else if (portDelta == 0) {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(blockchain.toPrettyString());
            System.out.print("Is blockchain valid? " + blockchain.isValid());
        }
    }

    private void simulateFewTransactions() {
        //Create wallets:
        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet coinbase = new Wallet();

        //create genesis transaction, which sends 100 NoobCoin to walletA:
        Transaction genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
        genesisTransaction.generateSignature(coinbase.privateKey);
        genesisTransaction.id = "0";
        genesisTransaction.getOutputs().add(new TxOutput(genesisTransaction.reciepient, genesisTransaction.amount, genesisTransaction.id));
        blockchain.getUTXOs().put(genesisTransaction.getOutputs().get(0).id, genesisTransaction.getOutputs().get(0));
        blockchain.getTransactionPool().addTransaction(genesisTransaction);

        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("\nWalletA is Attempting to send funds (40) to WalletB...");

        blockchain.getTransactionPool().addTransaction(walletA.createTransaction(walletB.publicKey, 40f));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        System.out.println("\nWalletA Attempting to send more funds (1000) than it has...");
        blockchain.getTransactionPool().addTransaction(walletA.createTransaction(walletB.publicKey, 1000f));
        System.out.println("Pending transactions: " + blockchain.getTransactionPool().getPoolSize());
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        System.out.println("\nWalletB is Attempting to send funds (20) to WalletA...");
        blockchain.getTransactionPool().addTransaction(walletA.createTransaction(walletB.publicKey, 20f));
        System.out.println("\nWalletA's balance is: " + walletA.getBalance());
        System.out.println("WalletB's balance is: " + walletB.getBalance());

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.print("Is blockchain valid? " + blockchain.isValid());
    }

    private void startLocalAuctionsDemo(int portDelta){
        wallet =  new Wallet();

        if (portDelta == 0) {

            Auction auction1 = new Auction(wallet,"banana", "José", 1L, 0.5F, 0, 1);
            Auction auction2 = new Auction(wallet,"laranja", "Quim", 1L, 0.5F, 0, 1);
            auctionManager.addLiveAuction(new LiveAuction(auction1));
            auctionManager.addLiveAuction(new LiveAuction(auction2));

            for(LiveAuction liveAuction: auctionManager.getLiveAuctions().values()){
                liveAuction.printLiveAuction();
            }

            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            auctionManager.endLiveAuction(auctionManager.getLiveAuction("banana"));

        } else {
            auctionManager.requestNetworkAuctions();
            System.out.println("Requesting auctions to the network...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Runnable run = new Client();
        run.run();
    }

    public static void main(String[] args) {
        new DecentAuctionLedger(args);
    }
}
