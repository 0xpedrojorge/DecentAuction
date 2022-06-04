package ssd.assignment.communication.operations;

import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.util.Standards;

import java.util.Arrays;
import java.util.List;

public class SendMessageOperation implements Operation {

    private final NetworkNode localNode;
    private final byte[] destinationId;
    private final byte[] message;

    public SendMessageOperation(NetworkNode localNode, byte[] destinationId, byte[] message) {
        this.localNode = localNode;
        this.destinationId = destinationId;
        this.message = message;
    }

    @Override
    public void execute() {
        List<KContact> closestContacts =
                localNode.getRoutingTable().getNClosestContacts(destinationId, Standards.KADEMLIA_K);

        for (KContact contact : closestContacts) {
            if (Arrays.equals(destinationId, contact.getId())) {
                localNode.getClientManager().sendMessage(localNode, contact, this, message);
                return;
            }
        }

        LookupOperation lookupOperation = new LookupOperation(localNode, destinationId);
        lookupOperation.execute();

        List<KContact> closestContactsToDestiny= lookupOperation.getKClosestContactsByStatus(Status.ASKED_AND_RESPONDED);

        for (KContact contact : closestContactsToDestiny) {
            if (Arrays.equals(contact.getId(), destinationId)) {
                localNode.getClientManager().sendMessage(localNode, contact, this, message);
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
