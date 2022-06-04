package ssd.assignment.blockchain.transactions;

import com.google.gson.GsonBuilder;
import ssd.assignment.util.Crypto;

import java.security.PublicKey;

public class TxOutput {

    public String id;
    public transient PublicKey reciepient; //also known as the new owner of these coins.
    public float amount; //the amount of coins they own
    public String parentTransactionId; //the id of the transaction this output was created in

    public TxOutput(PublicKey reciepient, float amount, String parentTransactionId) {
        this.reciepient = reciepient;
        this.amount = amount;
        this.parentTransactionId = parentTransactionId;
        this.id = Crypto.hash(this.toString());
    }

    public boolean isMine(PublicKey publicKey) {
        return (publicKey == reciepient);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
