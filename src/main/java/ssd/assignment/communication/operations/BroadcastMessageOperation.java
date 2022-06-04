package ssd.assignment.communication.operations;

import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.util.*;

public class BroadcastMessageOperation implements Operation {

    private final NetworkNode localNode;
    private final int depth;
    private final byte[] messageId;
    private final byte[] message;

    public BroadcastMessageOperation(NetworkNode localNode, int depth, byte[] messageId, byte[] message) {
        this.localNode = localNode;
        this.depth = depth;
        this.messageId = messageId;
        this.message = message;
    }

    @Override
    public void execute() {
        Random random = new Random();

        localNode.addToSeenMessages(messageId);

        for (int i = depth; i < Standards.KADEMLIA_ID_BIT_SIZE; i++) {

            List<KContact> kBucket = localNode.getRoutingTable().getKBuckets()[i].getAllContacts();
            List<KContact> kBucketCopy = new ArrayList<>(kBucket);

            if (!kBucket.isEmpty()) {
                for (int j = 0; j < Standards.KADEMLIA_MAX_BROADCAST_PER_DEPTH && !kBucketCopy.isEmpty(); j++) {
                    KContact toBroadcast = kBucketCopy.remove(random.nextInt(kBucketCopy.size()));
                    localNode.getClientManager().broadcastMessage(localNode, toBroadcast, this, depth + 1, messageId, message);
                }
            }
        }
    }
    public void handleFailedRequest(KContact contact){
        localNode.getRoutingTable().warnUnresponsiveContact(contact);
    }

    public void handleSuccessfulRequest(KContact contact) {
        localNode.getRoutingTable().insert(contact);
    }

}
