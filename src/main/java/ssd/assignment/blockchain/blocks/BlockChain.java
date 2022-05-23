package ssd.assignment.blockchain.blocks;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.blockchain.TransactionPool;
import ssd.assignment.blockchain.transactions.Transaction;
import ssd.assignment.blockchain.transactions.TxInput;
import ssd.assignment.blockchain.transactions.TxOutput;
import ssd.assignment.util.Helper;
import ssd.assignment.util.Standards;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class BlockChain {

    private final ArrayList<Block> blocks;
    private final TransactionPool transactionPool;
    private final HashMap<String, TxOutput> UTXOs;

    public BlockChain() {
        blocks = new ArrayList<>();
        transactionPool = new TransactionPool();
        UTXOs = new HashMap<>();
    }

    //TODO change this up
    public boolean isValid() {
        Block currentBlock, previousBlock;
        String target = Helper.getDificultyString(Standards.MINING_DIFFICULTY);
        HashMap<String,TxOutput> tempUTXOs = new HashMap<>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(blocks.get(0).getTransactions().get(0).getOutputs().get(0).id, blocks.get(0).getTransactions().get(0).getOutputs().get(0));

        for(int i=1; i < blocks.size(); i++) {
            currentBlock = blocks.get(i);
            previousBlock = blocks.get(i - 1);

            //check if hash is solved
            if(!currentBlock.getHeader().getHash().substring(0, Standards.MINING_DIFFICULTY).equals(target)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

            if (!currentBlock.getHeader().getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current hash mismatch");
                return false;
            }

            if (!previousBlock.getHeader().getHash().equals(currentBlock.getHeader().getParentHash())) {
                System.out.println("Parent hash mismatch");
                return false;
            }

            //loop through blockchains transactions:
            TxOutput tempOutput;
            for(int t=0; t <currentBlock.getTransactions().size(); t++) {
                Transaction currentTransaction = currentBlock.getTransactions().get(t);

                if(!currentTransaction.verifiySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }
                if(currentTransaction.getInputsAmount() != currentTransaction.getOutputsAmount()) {
                    System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
                    return false;
                }

                for(TxInput input : currentTransaction.getInputs()) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if(input.UTXO.amount != tempOutput.amount) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TxOutput output: currentTransaction.getOutputs()) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.getOutputs().get(0).reciepient != currentTransaction.reciepient) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if( currentTransaction.getOutputs().get(1).reciepient != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }
        }
        return true;
    }

    public void addBlock(Block newBlock) {
        blocks.add(newBlock);
    }

    public Block getLatesBlock() {
        if (blocks.size() == 0) return null;
        return blocks.get(blocks.size()-1);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public String toPrettyString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
