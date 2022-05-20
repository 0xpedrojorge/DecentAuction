package ssd.assignment.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import ssd.assignment.blockchain.transactions.Transaction;

public class CustomExclusionStrategy implements ExclusionStrategy {

    public boolean shouldSkipField(FieldAttributes f) {
        return (f.getDeclaringClass() == Transaction.class && f.getName().equals("id"));
    }

    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

}
