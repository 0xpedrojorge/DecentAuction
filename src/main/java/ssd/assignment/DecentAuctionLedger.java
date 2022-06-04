package ssd.assignment;

import lombok.Getter;
import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.blockchain.miners.MiningManager;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.MessageManager;
import ssd.assignment.communication.messages.types.BlockMessage;
import ssd.assignment.communication.operations.BroadcastMessageOperation;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;


public class DecentAuctionLedger {

    @Getter
    private static BlockChain blockchain;
    @Getter
    private static MiningManager miningManager;
    @Getter
    private static NetworkNode networkNode;
    @Getter
    private static MessageManager messageManager;

    public DecentAuctionLedger(String[] args) {
        if (args.length == 0) {
            System.out.println("Arguments not found");
            return;
        }

        startNetwork(Integer.parseInt(args[0]));
        startBlockchain();
        startMessageManager();


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

            new BroadcastMessageOperation(networkNode, 0, Utils.toByteArray("1"), Utils.toByteArray("Content")).execute();
        }
        System.out.println("Started the network");
    }

    private void startBlockchain() {
        blockchain = new BlockChain();
        miningManager = new MiningManager(blockchain);
        System.out.println("Started the blockchain");
    }

    private void startMessageManager() {
        messageManager = new MessageManager(networkNode, blockchain);

        networkNode.getServer().registerMessageConsumer(messageManager::receiveMessage);

        miningManager.registerBlockConsumer((block) -> {
            BlockMessage blockMessage = new BlockMessage(block);
            messageManager.publishMessage(blockMessage);
        });
        System.out.println("Started message manager");
    }

    public static void main(String[] args) {
        new DecentAuctionLedger(args);
    }
}
