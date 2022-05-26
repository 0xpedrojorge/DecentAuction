package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;

public class TxInput {

    public String transactionOutputId; //Reference to TransactionOutputs -> transactionId
    public TxOutput UTXO; //Contains the Unspent transaction output

    public TxInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
