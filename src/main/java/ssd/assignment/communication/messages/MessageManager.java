package ssd.assignment.communication.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ssd.assignment.blockchain.blocks.Block;
import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.adapters.MessageAdaptar;
import ssd.assignment.communication.messages.types.BlockMessage;
import ssd.assignment.communication.messages.types.TestMessage;
import ssd.assignment.communication.messages.types.TransactionMessage;
import ssd.assignment.communication.operations.BroadcastMessageOperation;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.CustomExclusionStrategy;
import ssd.assignment.util.Utils;

import java.lang.reflect.Modifier;

public class MessageManager {

    private final NetworkNode localNode;
    private final BlockChain blockChain;

    private final Gson gson;

    public MessageManager(NetworkNode localNode, BlockChain blockChain) {
        this.localNode = localNode;
        this.blockChain = blockChain;
        this.gson = new GsonBuilder()
                .setExclusionStrategies(new CustomExclusionStrategy())
                .registerTypeAdapter(Message.class, new MessageAdaptar())
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
        }
    }

}
