package ssd.assignment.communication.kademlia;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import ssd.assignment.util.Standards;
import ssd.assignment.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class KBucket {

    @Getter
    private final int depth;
    private final ConcurrentLinkedDeque<KContact> contacts;
    private final ConcurrentLinkedDeque<KContact> replacementCache;

    public KBucket(int depth) {
        this.depth = depth;
        this.contacts = new ConcurrentLinkedDeque<>();
        this.replacementCache = new ConcurrentLinkedDeque<>();
    }

    /**
     * Tries to insert a contact into a KBucket.
     * Makes use of Optimized Contact Accounting.
     *
     * @param newContact the contact to be inserted
     */
    public synchronized void insert(KContact newContact) {
        //System.out.println("Trying to insert " + Utils.toHexString(newContact.getId()) + " at depth " + depth);
        /*
        If the sending node already exists in the recipient’s k-bucket,
        the recipient moves it to the tail of the list
         */
        if (contacts.contains(newContact)) {
            //System.out.println(Utils.toHexString(newContact.getId()) + "is already in bucket, moving to tail");
            contacts.remove(newContact);
            newContact.setLastSeen(System.currentTimeMillis());
            newContact.resetStaleCount();
            contacts.addLast(newContact);
        } else {

            /*
            If the node is not already in the appropriate k-bucket and the bucket
            has fewer than k entries, then the recipient just inserts the new
            sender at the tail of the list.
             */
            if (contacts.size() < Standards.KADEMLIA_K) {
                contacts.addLast(newContact);
            } else {
                /*
                If the kbucket for a contact is already full with k entries, add new node to
                the replacement cache and check for stale nodes in the kbucket
                 */
                insertIntoReplacementCache(newContact);

                KContact stalest = null;
                for (KContact n : contacts) {
                    if (n.getStaleCount() >= Standards.KADEMLIA_STALENESS_LIMIT) {
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
                    contacts.remove(stalest);
                    contacts.addLast(replacementCache.removeFirst());
                }
            }
        }
    }

    /**
     * Tries to remove a contact from a KBucket.
     * Makes use of Optimized Contact Accounting.
     *
     * @param toRemove the contact to be removed
     */
    public synchronized void remove(KContact toRemove) {

        if (contacts.contains(toRemove)) {

            /*
            If a k-bucket is not full or its replacement cache is empty, Kademlia
            merely flags stale contacts rather than remove them. This ensures, among
            other things, that if a node’s own network connection goes down teporarily,
            the node won’t completely void all of its k-buckets.
             */
            if (replacementCache.isEmpty() || contacts.size() < Standards.KADEMLIA_K) {
                for (KContact c : contacts) {
                    if (c.equals(toRemove)) c.incrementStaleCount();
                }
            } else {
                contacts.remove(toRemove);
                contacts.addLast(replacementCache.removeFirst());
            }

        }
    }

    public boolean contains(KContact contact) {
        return contacts.contains(contact);
    }

    public int getSize() {
        return contacts.size();
    }

    public List<KContact> getAllContacts() {
        return new ArrayList<>(contacts);
    }

    private synchronized void insertIntoReplacementCache(KContact contact) {

        if (replacementCache.contains(contact)) {
            /*
            If the contact is already in the replacement cache, we update its data
            and move it to the end of the queue
            */
            replacementCache.remove(contact);
            contact.setLastSeen(System.currentTimeMillis());
            contact.resetStaleCount();
            replacementCache.addLast(contact);
        } else {
            /*
            Otherwise, we just add it to the queue, if there is space
             */
            if (replacementCache.size() < Standards.KADEMLIA_K) {
                replacementCache.addLast(contact);
            }
        }

    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

}
