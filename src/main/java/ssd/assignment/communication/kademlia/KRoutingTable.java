package ssd.assignment.communication.kademlia;

import com.google.common.math.BigIntegerMath;
import lombok.Getter;
import ssd.assignment.communication.kademlia.util.KContactDistanceComparator;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

@Getter
public class KRoutingTable {

    private final KContact self;
    private final KBucket[] kBuckets;

    public KRoutingTable(KContact self) {
        this.self = self;

        this.kBuckets = new KBucket[Standards.KADEMLIA_ID_BIT_SIZE];
        for (int i = 0; i < Standards.KADEMLIA_ID_BIT_SIZE; i++) {
            kBuckets[i] = new KBucket(i);
        }

        insert(self);
    }

    public int getBucketNumberForContact(KContact contact) {
        return getBucketNumberForNodeId(contact.getId());
    }

    public int getBucketNumberForNodeId(byte[] nodeId) {
        BigInteger distanceToContact = Utils.getXorDistance(self.getId(), nodeId);

        if (distanceToContact.equals(BigInteger.ZERO)) {
            return 0;
        } else {
            return BigIntegerMath.log2(distanceToContact, RoundingMode.DOWN);
        }
    }

    public synchronized void insert(KContact contact) {
        kBuckets[getBucketNumberForContact(contact)].insert(contact);
    }

    public synchronized List<KContact> getNClosestContacts(byte[] nodeId, int n) {
        TreeSet<KContact> sortedContacts = new TreeSet<>(new KContactDistanceComparator(nodeId));
        int kBucketForGivenNode = getBucketNumberForNodeId(nodeId);

        /*
        Look for contacts from the kBucket de contact shouldbe in outwards
        and add them to the sorted contacts, until we have n nodes
         */
        for (int i = 0; i < Standards.KADEMLIA_ID_BIT_SIZE; i++) {
            boolean canGoPrevious = kBucketForGivenNode - i >= 0;
            boolean canGoNext = kBucketForGivenNode + i < Standards.KADEMLIA_ID_BIT_SIZE;

            if (canGoPrevious) {
                sortedContacts.addAll(kBuckets[kBucketForGivenNode - i].getAllContacts());
            }

            if (canGoNext && i > 0) {
                sortedContacts.addAll(kBuckets[kBucketForGivenNode + i].getAllContacts());
            }

            if (sortedContacts.size() >= n || !canGoPrevious && !canGoNext) {
                break;
            }

        }

        List<KContact> nClosestContacts = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!sortedContacts.isEmpty())
                nClosestContacts.add(sortedContacts.pollFirst());
        }

        return nClosestContacts;
    }

    public void warnUnresponsiveContacts(List<KContact> contacts) {
        if (!contacts.isEmpty()) {
            for (KContact c : contacts) {
                warnUnresponsiveContact(c);
            }
        }
    }

    public void warnUnresponsiveContact(KContact c) {
        kBuckets[getBucketNumberForNodeId(c.getId())].remove(c);
    }

    public List<KContact> getAllContacts() {
        List<KContact> contacts = new ArrayList<>();
        for (KBucket bucket : kBuckets) {
            contacts.addAll(bucket.getAllContacts());
        }
        return contacts;
    }

}
