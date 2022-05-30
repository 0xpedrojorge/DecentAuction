package ssd.assignment.communication.kademlia;

import ssd.assignment.util.Helper;

import java.math.BigInteger;
import java.util.Arrays;

public class KademliaUtils {

    public static BigInteger getDistanceBetweenNodes(byte[] node1, byte[] node2) {
        BigInteger xor1 = new BigInteger(1, node1);
        BigInteger xor2 = new BigInteger(1, node2);
        return xor1.xor(xor2);
    }
}
