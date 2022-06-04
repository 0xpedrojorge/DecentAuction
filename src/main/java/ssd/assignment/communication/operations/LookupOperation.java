package ssd.assignment.communication.operations;

import ssd.assignment.communication.NetworkNode;
import ssd.assignment.communication.kademlia.KContact;
import ssd.assignment.communication.kademlia.util.KContactDistanceComparator;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class LookupOperation implements Operation {

    private final NetworkNode localNode;
    private final byte[] idToLookup;

    private final Map<KContact, Status> operations;
    private final Map<KContact, Long> awatingResponse;

    public LookupOperation(NetworkNode localNode, byte[] idToLookup) {
        this.localNode = localNode;
        this.idToLookup = idToLookup;

        this.operations = new TreeMap<>(new KContactDistanceComparator(idToLookup));
        this.awatingResponse = new HashMap<>();

        operations.put(localNode.getSelf(), Status.ASKED_AND_RESPONDED);
        /*
        Adding all known contacts because the k closest may be offline
         */
        List<KContact> contacts = localNode.getRoutingTable().getAllContacts();
        for (KContact c : contacts) {
            addContactToOperations(c);
        }
    }

    @Override
    public void execute() {
        Thread t = new Thread(() -> {
            int totalTimeWaited = 0;
            int timeInterval = 20;

            while (true) {
                if (!checkContacts()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(totalTimeWaited);
                        totalTimeWaited += timeInterval;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    break;
                }
            }
        });
        t.start();
    }

    public void handleFailedRequest(KContact contact) {
        awatingResponse.remove(contact);
        operations.put(contact, Status.FAILED);
        localNode.getRoutingTable().warnUnresponsiveContact(contact);
        checkContacts();
    }

    public void handleSuccessfulRequest(KContact contact, List<KContact> newContacts) {
        System.out.println("Got a successful response from node " + Utils.toHexString(contact.getId()));
        awatingResponse.remove(contact);
        operations.put(contact, Status.ASKED_AND_RESPONDED);
        /*
        Update info on contact, move to end of bucket
        and add new nodes to next operations if it is
        not already there, to not lose status
         */
        localNode.getRoutingTable().insert(contact);
        for(KContact newContact : newContacts) {
            operations.putIfAbsent(newContact, Status.NOT_ASKED);
        }
        checkContacts();
    }

    private void addContactToOperations(KContact contact) {
        if (!operations.containsKey(contact)) {
            operations.put(contact, Status.NOT_ASKED);
        }
    }

    private boolean checkContacts() {
        if (awatingResponse.size() >= Standards.KADEMLIA_ALPHA) return false;

        List<KContact> nextContactsToQuery = getKClosestContactsByStatus(Status.NOT_ASKED);

        if (!nextContactsToQuery.isEmpty() || !awatingResponse.isEmpty() ) {
            for (KContact contact : nextContactsToQuery) {

                operations.put(contact, Status.AWAITING_RESPONSE);
                awatingResponse.put(contact, System.currentTimeMillis());
                localNode.getClientManager().lookup(localNode, contact, this, idToLookup);
                if (awatingResponse.size() >= Standards.KADEMLIA_ALPHA)  {
                    break;
                }

            }
            return false;
        }

        return true;
    }

    public List<KContact> getKClosestContactsByStatus(Status status) {
        List<KContact> toReturn = new ArrayList<>();
        int nodesGathered = 0;

        for (KContact contact : operations.keySet()) {
            if (operations.get(contact).equals(status)) {
                toReturn.add(contact);
                nodesGathered++;
                if (nodesGathered == Standards.KADEMLIA_K) break;
            }
        }
        return toReturn;
    }
}
