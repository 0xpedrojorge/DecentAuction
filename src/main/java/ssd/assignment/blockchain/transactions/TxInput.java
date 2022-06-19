package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;

public class TxInput {

    public String transactionOutputId;
    public TxOutput unspentTransactionOutput;

    public TxInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
