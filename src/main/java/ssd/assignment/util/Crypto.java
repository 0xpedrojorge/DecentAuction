package ssd.assignment.util;

import java.security.*;

public abstract class Crypto {

    public static KeyPair generateKeyPair() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance(Standards.KEYGEN_ALGORITHM);
            generator.initialize(2048, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert generator != null;
        return generator.generateKeyPair();
    }

    public static byte[] sign(String message, PrivateKey privateKey) {
        byte[] s = null;
        try {
            Signature sign = Signature.getInstance(Standards.SIGNING_ALGORITHM);
            sign.initSign(privateKey);
            sign.update(Helper.toByteArray(message));
            s = sign.sign();
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static boolean verifySignature(String message, byte[] signature, PublicKey publicKey) {
        boolean isVerified = false;
        try {
            Signature sign = Signature.getInstance(Standards.SIGNING_ALGORITHM);
            sign.initVerify(publicKey);
            sign.update(Helper.toByteArray(message));
            isVerified = sign.verify(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return isVerified;
    }

    public static String hash(String data) {
        byte[] bytes = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(Standards.DIGEST_ALGORITHM);
            bytes = digest.digest(Helper.toByteArray(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert bytes != null;
        return Helper.toHexString(bytes);
    }


}
