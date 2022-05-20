package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;

public class TxOutput {

    private final float value;
    private final String scriptPubKey;

    public TxOutput(float value, String scriptPubKey) {
        this.value = value;
        this.scriptPubKey = scriptPubKey;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
