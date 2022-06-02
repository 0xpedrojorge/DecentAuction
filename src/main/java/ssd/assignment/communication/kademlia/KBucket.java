package ssd.assignment.communication.kademlia;

import ssd.assignment.util.Standards;

import java.util.LinkedList;
import java.util.List;

public class KBucket {

    private final int depth;
    private final LinkedList<KNode> nodes;
    private final LinkedList<KNode> replacementCache;

    public KBucket(int depth) {
        this.depth = depth;
        this.nodes = new LinkedList<>();
        this.replacementCache = new LinkedList<>();
    }

    public KBucket(int depth, List<KNode> nodes) {
        this.depth = depth;
        this.nodes = (LinkedList<KNode>) nodes;
        this.replacementCache = new LinkedList<>();
    }

    public synchronized void insert(KNode newNode) {

        /*
        If the sending node already exists in the recipient’s k-bucket,
        the recipient moves it to the tail of the list
         */
        if (nodes.contains(newNode)) {
            nodes.remove(newNode);
            newNode.setLastSeen(System.currentTimeMillis());
            newNode.resetStaleCount();
            nodes.addLast(newNode);
        } else {

            /*
            If the node is not already in the appropriate k-bucket and the bucket
            has fewer than k entries, then the recipient just inserts the new
            sender at the tail of the list.
             */
            if (nodes.size() < Standards.K) {
                nodes.addLast(newNode);
            } else {
                /*
                If the kbucket for a contact is already full with k entries, add new node to
                the replacement cache and check for stale nodes in the kbucket
                 */
                replacementCache.addLast(newNode);

                KNode stalest = null;
                for (KNode n : nodes) {
                    if (n.getStaleCount() >= 1) {
                        if (stalest == null) {
                            stalest = n;
                        } else if (n.getStaleCount() > stalest.getStaleCount()){
                            stalest = n;
                        }
                    }
                }
                /*
                If bucket has stale nodes, replace the stalest with the first knode in the
                replacement cache
                 */
                if(stalest != null) {
                    nodes.remove(stalest);
                    nodes.addLast(replacementCache.removeFirst());
                }
            }
        }

    }

    private KNode remove(KNode n) {
        return null;
    }

    public boolean contains(KNode n) {
        return nodes.contains(n);
    }
}
