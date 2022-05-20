package ssd.assignment.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class Crypto {

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(Standards.KEYGEN_ALGORITHM);
        generator.initialize(2048, new SecureRandom());
        return generator.generateKeyPair();
    }

    public static String sign(String message, PrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance(Standards.SIGNING_ALGORITHM);
        sign.initSign(privateKey);
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    public static boolean verifySignature(String message, String signature, PublicKey publicKey) throws Exception {
        Signature sign = Signature.getInstance(Standards.SIGNING_ALGORITHM);
        sign.initVerify(publicKey);
        sign.update(message.getBytes(StandardCharsets.UTF_8));

        return sign.verify(Base64.getDecoder().decode(signature));
    }

    public byte[] hash(String data) {
        MessageDigest digest;
        byte[] bytes = null;
        try {
            digest = MessageDigest.getInstance(Standards.DIGEST_ALGORITHM);
            bytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return bytes;
    }


}
