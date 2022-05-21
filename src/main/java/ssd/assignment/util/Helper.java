package ssd.assignment.util;

import ssd.assignment.blockchain.transactions.Transaction;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;

public class Helper {

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
        String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
        return merkleRoot;
    }

    public static String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }
}
