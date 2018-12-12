package application;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * application.BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;
    
    // Branching factor is the number of children nodes 
    // for internal nodes of the tree
    private int branchingFactor;
    
    
    /**
     * Public constructor
     * 
     * @param branchingFactor 
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        this.branchingFactor = branchingFactor;
        this.root = new LeafNode();
    }
    
    /*
     * (non-Javadoc)
     * @see application.BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {
        root.insert(key, value);
    }

    /*
     * (non-Javadoc)
     * @see application.BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        return root.rangeSearch(key, comparator);
    }
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }
    
    
    /**
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
     */
    private abstract class Node {
        
        // List of keys
        List<K> keys;

        /**
         * Package constructor
         */
        Node() {
            this.keys = new ArrayList<K>();
        }
        
        /**
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see application.BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String toString() {
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;
        
        /**
         * Package constructor
         */
        InternalNode() {
            super();
            this.children = new ArrayList<Node>();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return children.get(0).getFirstLeafKey();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return this.keys.size() == branchingFactor;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
            Node leaf = findNodeToInsertTo(key);
            leaf.insert(key, value);
            
            // Moves node if node's branching factor is exceeded
            if (leaf.isOverflow()) {
                Node sibling = leaf.split();
                insert(sibling.getFirstLeafKey(), sibling);
            }
            if (root.isOverflow()) {
                Node sibling = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.add(sibling.getFirstLeafKey());
                newRoot.children.add(this);
                newRoot.children.add(sibling);
                root = newRoot;
            }
        }

        /**
         * Finds node to insert
         * @param key key to insert
         * @return node that is inserted
         */
        Node findNodeToInsertTo(K key) {
            int foundPos = Collections.binarySearch(keys, key);
            int nodePos = foundPos >= 0 ? foundPos + 1 : -foundPos - 1;
            return children.get(nodePos);
        }

        /**
         * inserts key into a node
         * @param key key to be inserted
         * @param node node to have key inserted
         */
        void insert(K key, Node node) {
            int foundPos = Collections.binarySearch(keys, key);
            int insertPos = foundPos >= 0 ? foundPos + 1 : -foundPos - 1;
            keys.add(insertPos, key);
            children.add(insertPos + 1, node);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        InternalNode split() {
            int start = this.keys.size() / 2 + 1;
            int end = this.keys.size();
            InternalNode newNode = new InternalNode();
            for (int i = start; i < end; i++) {
                newNode.keys.add(this.keys.get(i));
                newNode.children.add(this.children.get(i));
            }
            newNode.children.add(this.children.get(end));

            // Remove nodes, including middle node to add to parent
            keys.subList(start - 1, end).clear();
            children.subList(start, end + 1).clear();

            return newNode;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
        	if (key.compareTo(this.keys.get(0)) < 0) {
        	    return this.children.get(0).rangeSearch(key, comparator);
            }
            for (int i = 0; i < this.keys.size() - 1; i++) {
                K current = this.keys.get(i);
                K next = this.keys.get(i + 1);
                if (key.compareTo(current) > 0 && key.compareTo(next) < 0) {
                    return this.children.get(i + 1).rangeSearch(key, comparator);
                }
            }
            return this.children.get(this.children.size() - 1).rangeSearch(key, comparator);
        }
    
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
     */
    private class LeafNode extends Node {
        
        // List of values
        List<V> values;
        
        // Reference to the next leaf node
        LeafNode next;
        
        // Reference to the previous leaf node
        LeafNode previous;
        
        /**
         * Package constructor
         */
        LeafNode() {
            super();
            this.values = new ArrayList<V>();
        }
        
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return this.keys.get(0);
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return this.keys.size() == branchingFactor;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
            // Finds position to insert into
            int searchPos = Collections.binarySearch(keys, key);
            int insertionPt = searchPos >= 0 ? searchPos : -searchPos - 1;
            keys.add(insertionPt, key);
            values.add(insertionPt, value);
            if (root.isOverflow()) {
                Node newNode = split();
                InternalNode newRoot = new InternalNode();
                newRoot.keys.add(newNode.getFirstLeafKey());
                newRoot.children.add(this);
                newRoot.children.add(newNode);
                root = newRoot;
            }
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        LeafNode split() {
            LeafNode splitNode = new LeafNode();
            int start = (this.keys.size() + 1) / 2;
            int end = this.keys.size();
            for (int i = start; i < end; i++) {
                splitNode.keys.add(keys.get(i));
                splitNode.values.add(values.get(i));
            }

            keys.subList(start, end).clear();
            values.subList(start, end).clear();

            if (this.next != null) {
                this.next.previous = splitNode;
            }
            splitNode.previous = this;
            splitNode.next = this.next;
            this.next = splitNode;

            return splitNode;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
        	List<V> toReturn = new ArrayList<V>();
        	if (key == null || comparator == null) {
        		return toReturn;
        	}
            switch (comparator) {
                case "==":
                    for (int i = 0; i < keys.size(); i++) {
                        if (keys.get(i).compareTo(key) == 0) {
                            toReturn.add(values.get(i));
                        }
                    }
                    break;
                case ">=": {
                    LeafNode node = this;
                    if (node.previous != null) {
                        node = node.previous;
                    }
                    while (node != null) {
                        for (int i = 0; i < node.keys.size(); i++) {
                            if (node.keys.get(i).compareTo(key) >= 0) {
                                toReturn.add(node.values.get(i));
                            }
                        }
                        node = node.next;
                    }
                    break;
                }
                case "<=": {
                    LeafNode node = this;
                    if (node.next != null) {
                        node = node.next;
                    }
                    while (node != null) {
                        for (int i = 0; i < node.keys.size(); i++) {
                            if (node.keys.get(i).compareTo(key) <= 0) {
                                toReturn.add(node.values.get(i));
                            }
                        }
                        node = node.previous;
                    }
                    break;
                }
            }
        	return toReturn;
        }
        
    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a application.BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
        // create empty application.BPTree with branching factor of 3
        BPTree<Double, Double> bpTree = new BPTree<>(3);

        // create a pseudo random number generator
        Random rnd1 = new Random();

        // some value to add to the application.BPTree
        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};

        // build an ArrayList of those value and add to application.BPTree also
        // allows for comparing the contents of the ArrayList 
        // against the contents and functionality of the application.BPTree
        // does not ensure application.BPTree is implemented correctly
        // just that it functions as a data structure with
        // insert, rangeSearch, and toString() working.
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < 400; i++) {
            Double j = dd[rnd1.nextInt(4)];
            list.add(j);
            bpTree.insert(j, j);
            System.out.println("\n\nTree structure:\n" + bpTree.toString());
        }
        List<Double> filteredValues = bpTree.rangeSearch(0.2d, ">=");
        System.out.println("Filtered values: " + filteredValues.toString());
    }

} // End of class application.BPTree
