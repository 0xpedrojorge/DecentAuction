package ssd.assignment.blockchain.transaction;

public class TxOut {

    private final float value;
    private final String scriptPubKey;

    public TxOut(float value, String scriptPubKey) {
        this.value = value;
        this.scriptPubKey = scriptPubKey;
    }

    public String hash() {
        return "";
    }

    public String sign() {
        return "";
    }

    @Override
    public String toString() {
        return "Output: {pubKey=" + scriptPubKey +
                ", value=" + value + "}";
    }

}
