package ssd.assignment.blockchain;

import java.util.List;

public class Transaction {

    private short version;
    private TxType type;
    private List<String> input;
    private List<String> output;
    private String hash;
    private int blockNumber;

    public Transaction(String input, String output) {
        this.version = 1;
        this.type = TxType.NORMAL;

    }

}