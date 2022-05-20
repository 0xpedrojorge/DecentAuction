package ssd.assignment.util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

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

    public static String sign(String message, PrivateKey privateKey) {
        String signature = null;
        try {
            Signature sign = Signature.getInstance(Standards.SIGNING_ALGORITHM);
            sign.initSign(privateKey);
            sign.update(Helper.toByteArray(message));
            signature = Helper.toHexString(sign.sign());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return signature;
    }

    public static boolean verifySignature(String message, String signature, PublicKey publicKey) {
        boolean isVerified = false;
        try {
            Signature sign = Signature.getInstance(Standards.SIGNING_ALGORITHM);
            sign.initVerify(publicKey);
            sign.update(Helper.toByteArray(message));
            sign.verify(Helper.toByteArray(signature));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return isVerified;
    }

    public static byte[] hash(String data) {
        byte[] bytes = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(Standards.DIGEST_ALGORITHM);
            bytes = digest.digest(Helper.toByteArray(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }


}
