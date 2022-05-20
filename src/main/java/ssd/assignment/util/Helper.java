package ssd.assignment.util;

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
        return null;
    }
}
