package ssd.assignment.communication.operations;

import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.StoredData;
import ssd.assignment.util.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StoreOperation implements Operation {

    private final NetworkNode localNode;
    private final StoredData data;


    public StoreOperation(NetworkNode localNode, StoredData data) {
        System.out.println("Here I started the store operation");
        this.localNode = localNode;
        this.data = data;
    }

    @Override
    public void execute() {
        localNode.getDht().storePair(data.getKey(), data.getValue(), data.getOriginalPublisherId());

        LookupOperation lookupOperation = new LookupOperation(localNode, data.getKey());
        lookupOperation.execute();

        List<KContact> closestContactsToKey = lookupOperation.getKClosestContactsByStatus(Status.ASKED_AND_RESPONDED);

        for (KContact contact : closestContactsToKey) {
            if (!Arrays.equals(contact.getId(), localNode.getNodeId())) {
                localNode.getClientManager().store(localNode, contact, this, data);
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
