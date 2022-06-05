package ssd.assignment.communication.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ssd.assignment.auctions.Auction;
import ssd.assignment.auctions.AuctionManager;
import ssd.assignment.auctions.Bid;
import ssd.assignment.blockchain.blocks.Block;
import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.adapters.MessageAdapter;
import ssd.assignment.communication.messages.types.*;
import ssd.assignment.communication.operations.BroadcastMessageOperation;
import ssd.assignment.communication.operations.SendMessageOperation;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.CustomExclusionStrategy;
import ssd.assignment.util.Utils;

import java.lang.reflect.Modifier;

public class MessageManager {

    private final NetworkNode localNode;
    private final BlockChain blockChain;
    private final AuctionManager auctionManager;

    private final Gson gson;

    public MessageManager(NetworkNode localNode, BlockChain blockChain, AuctionManager auctionManager) {
        this.localNode = localNode;
        this.blockChain = blockChain;
        this.auctionManager = auctionManager;

        this.gson = new GsonBuilder()
                .setExclusionStrategies(new CustomExclusionStrategy())
                .registerTypeAdapter(Message.class, new MessageAdapter())
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();
    }

    public void publishMessage(MessageData messageData) {

        Message message = new Message(messageData);

        String jsonMessage = gson.toJson(message);

        System.out.println("Broadcasting " + jsonMessage);

        byte[] messageId = Utils.toByteArray(Crypto.hash(jsonMessage));

        new BroadcastMessageOperation(localNode, 0, messageId, jsonMessage.getBytes()).execute();
    }

    public void sendMessage(MessageData messageData, KContact contact) {

        Message message = new Message(messageData);

        String jsonMessage = gson.toJson(message);

        System.out.println("Sending " + jsonMessage);

        new SendMessageOperation(localNode, contact.getId(), jsonMessage.getBytes()).execute();
    }

    public void receiveMessage(KContact sendingContact, byte[] message) {
        //TODO deal with incoming messages
        //String jsonMessage = new String(message);
        //System.out.println("Received " + jsonMessage);

        Message parsedMessage = gson.fromJson(new String(message), Message.class);

        switch (parsedMessage.getData().getType()) {
            case BRADCAST_TEST_MESSAGE: {
                String stuff = ((TestMessage) parsedMessage.getData()).getStuff();
                System.out.println("Received a test message: " + stuff);
                break;
            }
            case BROADCAST_TRANSACTION: {
                Transaction transaction = ((TransactionMessage) parsedMessage.getData()).getTransaction();
                System.out.println("Received a transacion:" + transaction);
                blockChain.getTransactionPool().addTransaction(transaction);
                break;
            }
            case BROADCAST_BLOCK: {
                Block block = ((BlockMessage) parsedMessage.getData()).getBlock();
                System.out.println("Received a block : " + block);
                blockChain.addBlock(block);
                break;
            }
            case BROADCAST_AUCTION: {
                Auction auction = ((AuctionMessage) parsedMessage.getData()).getAuction();
                System.out.println("Received an auction");
                //TODO deal with incoming live auction
                auctionManager.addLiveAuction(auction);
                break;
            }
            case BROADCAST_BID: {
                Bid bid = ((BidMessage) parsedMessage.getData()).getBid();
                System.out.println("Received a bid");
                //TODO deal with incoming bid
                auctionManager.addBid(bid);
                break;
            }
            case REQUEST_LIVE_AUCTIONS: {
                System.out.println("Request for Auctions in the Network");
                //TODO send auctions
                auctionManager.sendAuctionsTo(sendingContact);
                break;
            }
        }
    }

}
