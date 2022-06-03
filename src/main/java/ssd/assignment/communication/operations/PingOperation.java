package ssd.assignment.communication.operations;

import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.util.Standards;

import java.util.Arrays;
import java.util.List;

public class PingOperation implements Operation {

    private final NetworkNode localNode;
    private final byte[] destinationNodeId;

    public PingOperation(NetworkNode localNode, byte[] destinationNodeId) {
        this.localNode = localNode;
        this.destinationNodeId = destinationNodeId;
    }

    @Override
    public void execute() {
        List<KContact> kClosestNodes =
                localNode.getKRoutingTable().getNClosestContacts(destinationNodeId, Standards.KADEMLIA_K);

        for (KContact contact : kClosestNodes) {
            if (Arrays.equals(contact.getId(), destinationNodeId)) {
                localNode.getClientManager().ping(localNode, contact);
                return;
            }
        }
    }
}
