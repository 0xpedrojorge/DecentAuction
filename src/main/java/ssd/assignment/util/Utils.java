package ssd.assignment.util;

import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.communication.kademlia.KContact;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;

public class Utils {

    public static String toHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        assert bytes != null;
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();

    }

    public static byte[] toByteArray(String string) {
        return string.getBytes(StandardCharsets.UTF_8);
    }

    /*
    Blockchain auxiliar functions
     */

    public static String getStringFromKey(Key key) {
        return toHexString(key.getEncoded());
    }

    public static String getMerkleRoot(ArrayList<Transaction> transactions) {
        int count = transactions.size();

        ArrayList<String> previousTreeLayer = new ArrayList<>();
        for(Transaction transaction : transactions) {
            previousTreeLayer.add(transaction.getId());
        }

        ArrayList<String> treeLayer = previousTreeLayer;

        while(count > 1) {
            treeLayer = new ArrayList<>();
            for(int i=1; i < previousTreeLayer.size(); i++) {
                treeLayer.add(Crypto.hash(previousTreeLayer.get(i-1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    }

    public static String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    /*
    Kademlia auxiliar functions
     */

    public static BigInteger getXorDistance(byte[] node1, byte[] node2) {
        BigInteger distance = new BigInteger(1, node1);
        distance = distance.xor(new BigInteger(1, node2));

        return distance;
    }

    public static InetAddress getLocalAddress() {
        InetAddress localIpAddress;
        try {
            localIpAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return localIpAddress;
    }

    public static String getLocalAddressAsString() {
        String localIpAddress;
        try {
            localIpAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return localIpAddress;
    }

    public static InetAddress getAddressFromString(String string) {
        InetAddress address;
        try {
            address = InetAddress.getByName(string);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return address;
    }
}
