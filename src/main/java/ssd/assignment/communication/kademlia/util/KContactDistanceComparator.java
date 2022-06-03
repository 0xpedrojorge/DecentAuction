package ssd.assignment.communication.kademlia.util;

import ssd.assignment.communication.kademlia.KContact;

import java.math.BigInteger;
import java.util.Comparator;

public class KContactDistanceComparator implements Comparator<KContact> {

    private final BigInteger startingPoint;

    public KContactDistanceComparator(byte[] nodeId) {
        this.startingPoint = new BigInteger(1, nodeId);
    }

    @Override
    public int compare(KContact contact1, KContact contact2) {
        BigInteger c1 = new BigInteger(1, contact1.getId());
        BigInteger c2 = new BigInteger(1, contact2.getId());

        BigInteger distanceToKContact1 = c1.xor(startingPoint);
        BigInteger distanceToKContact2 = c2.xor(startingPoint);

        return distanceToKContact1.compareTo(distanceToKContact2);
    }
}
