package ssd.assignment.communication.messages;

import ssd.assignment.blockchain.blocks.BlockChain;
import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.operations.BroadcastMessageOperation;
import ssd.assignment.util.Crypto;
import ssd.assignment.util.Utils;

public class MessageManager {

    private final NetworkNode localNode;
    private final BlockChain blockChain;

    public MessageManager(NetworkNode localNode, BlockChain blockChain) {
        this.localNode = localNode;
        this.blockChain = blockChain;
    }

    public void publishMessage(Message message) {
        byte[] messageId = Utils.toByteArray(Crypto.hash(message.toString()));
        byte[] messageAsBytes = Utils.toByteArray(message.toString());

        new BroadcastMessageOperation(localNode, 0, messageId, messageAsBytes).execute();
    }

    public void receiveMessage(KContact sendingContact, byte[] message) {
        //TODO deal with incoming messages
    }

}
