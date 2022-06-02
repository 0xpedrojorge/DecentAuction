package ssd.assignment.util;

public class Standards {

    /*
    Sets the digest algorithm
     */
    public static final String DIGEST_ALGORITHM = "SHA-256";

    /*
    Sets the key generating algorithm
     */
    public static final String KEYGEN_ALGORITHM = "RSA";

    /*
    Sets the signing algorithm
     */
    public static final String SIGNING_ALGORITHM = "SHA256withRSA";

    /*
    Sets the amount of zeros at the start of the hash when mining a block
     */
    public static final int MINING_DIFFICULTY = 3;

    /*
    Bounds the amount of transactions per block
     */
    public static final int MIN_TRANSACTIONS_PER_BLOCK = 3;
    public static final int MAX_TRANSACTIONS_PER_BLOCK = 10;

    /*
    Sets the number of miners
     */
    public static final int NUMBER_OF_MINERS = 1;

    /*
    Sets the replication parameter for Kademlia
     */
    public static final int K = 20;

    /*
    Sets the concurrency parameter for Kademlia
     */
    public static final int ALPHA = 3;

    /*
    Sets the default node port
     */
    public static final int DEFAULT_PORT = 50051;

}
