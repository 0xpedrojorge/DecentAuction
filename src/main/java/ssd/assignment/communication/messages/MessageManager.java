package ssd.assignment.communication.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.messages.adapters.MessageAdaptar;
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
        String jsonMessage = new String(message);
        System.out.println("Received " + jsonMessage);

        Message parsedMessage = gson.fromJson(jsonMessage, Message.class);

        switch (parsedMessage.getData().getType()) {
            case BRADCAST_TEST_MESSAGE: {
                System.out.println("Received a test message");
                break;
            }
            case BROADCAST_TRANSACTION: {
                System.out.println("Received a transaction");
                break;
            }
            case BROADCAST_BLOCK: {
                System.out.println("Received a block");
                break;
            }
        }
    }

}
