package ssd.assignment.blockchain.wallet;

import ssd.assignment.util.Crypto;

import java.security.*;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        Crypto crypto = new Crypto();
        try {
            KeyPair keyPair = crypto.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public byte[] getPublicKeyHash() {
        Crypto crypto = new Crypto();
        return crypto.hash(publicKey.toString());
    }

    public static void main(String args[]) {
        Wallet wallet = new Wallet();
        System.out.println("Wallet has been created.");
    }
}
