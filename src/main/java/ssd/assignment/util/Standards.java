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
    Sets the ID byte size for Kademlia
     */
    public static final int KADEMLIA_ID_BIT_SIZE = 160;

    /*
    Sets the replication parameter for Kademlia
     */
    public static final int KADEMLIA_K = 20;

    /*
    Sets the bit size ID
     */
    public static final int B = 160;

    /*
    Sets the concurrency parameter for Kademlia
     */
    public static final int KADEMLIA_ALPHA = 3;

    /*
    Sets the max amount of nodes to broadcast to for each depth
     */
    public static final int KADEMLIA_MAX_BROADCAST_PER_DEPTH = 3;

    /*
    Sets the staleness limit for Kademlia
     */
    public static final int KADEMLIA_STALENESS_LIMIT = 5;

    /*
    Sets the maximun number os atempts to bootstrap a node
     */
    public static final int BOOTSTAP_ATEMPTS = 5;

    /*
    Sets the default node port
     */
    public static final int DEFAULT_PORT = 50050;

    /*
    Sets the maximun number os atempts to bootstrap a node
    */
    //public static final int BOOTSTAP_ATEMPTS = 5;

}
