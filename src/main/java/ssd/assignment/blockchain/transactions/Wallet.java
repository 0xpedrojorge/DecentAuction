package ssd.assignment.blockchain.transactions;

import ssd.assignment.util.Crypto;

import java.security.*;

public class Wallet {

    public PrivateKey privateKey;
    public PublicKey publicKey;

    public Wallet() {
        generateKeyPair();
    }

    public void generateKeyPair() {
        KeyPair keyPair = Crypto.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public byte[] getPublicKeyHash() {
        return Crypto.hash(publicKey.toString());
    }

}
