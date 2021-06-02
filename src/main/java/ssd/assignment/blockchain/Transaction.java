package ssd.assignment.blockchain;

import java.util.Dictionary;
import java.util.List;

public class Transaction {

    private short version;
    private int id;
    private TxType type;
    private List<TxIn> inputs;
    private List<TxOut> outputs;
    private String hash;

    public Transaction(String input, String output) {
        this.version = 1;
        this.type = TxType.NORMAL;

    }

    class TxIn {
        private String outpoint; //Store corresponding output block num, tx id and input id
        private String signatureScript;
        private int id;

        TxIn() {

        }
    }

    class TxOut {
        private int value;
        private String pubkeyScript;

        TxOut() {

        }
    }

}