package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

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
        this.root = null;
    }
    
    
    /*
     * (non-Javadoc)
     * @see application.BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {
        if (this.root == null) {
            this.root = new LeafNode();
            this.root.insert(key, value);
        } else {
            LeafNode toInsert = recFindNodeToInsert(key, this.root);
            toInsert.insert(key, value);
        }
    }

    private LeafNode recFindNodeToInsert(K key, Node node) {
        if (node instanceof BPTree.LeafNode) {
            return (LeafNode)node;
        } else {
            if (key.compareTo(node.keys.get(0)) < 0) {
                return recFindNodeToInsert(key, ((InternalNode) node).children.get(0));
            }
            for (int i = 0; i < node.keys.size() - 1; i++) {
                int j = key.compareTo(node.keys.get(i));
                int k = key.compareTo(node.keys.get(i + 1));
                if (j > 0 && (k < 0 || k == 0)) {
                    return recFindNodeToInsert(key, ((InternalNode) node).children.get(i + 1));
                }
            }
            return recFindNodeToInsert(key, ((InternalNode) node).children.get(((InternalNode) node).children.size() - 1));
        }
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
        if(root == null) {
        	return new ArrayList<V>();
        }
        return recFindNodeToInsert(key, root).rangeSearch(key, comparator);
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

        InternalNode parent;
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
            return keys.get(0);
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

        }

        void insert(K key, Node node) {
            if (!this.isOverflow()) {
                if (this.keys.size() == 0) {
                    this.keys.add(key);
                    this.children.add(node);
                } else {
                    if (key.compareTo(this.getFirstLeafKey()) < 0) {
                        this.keys.add(0, key);
                        this.children.add(0, node);
                    }
                    if (key.compareTo(this.keys.get(this.keys.size() - 1)) > 0) {
                        this.keys.add(this.keys.size(), key);
                        this.children.add(this.children.size(), node);
                    }
                    for (int i = 0; i < this.keys.size() - 1; i++) {
                        if (key.compareTo(this.keys.get(i)) > 0 && key.compareTo(this.keys.get(i + 1)) < 1) {
                            this.keys.add(i + 1, key);
                            this.children.add(i + 1, node);
                        }
                    }
                }
            } else {
                // This needs to be fixed
                if (this.parent == null) {
                    this.parent = new InternalNode();
                    root = this.parent;
                }
                InternalNode newNode = split();
                newNode.parent = this.parent;
                this.parent.insert(newNode.getFirstLeafKey(), newNode);
            }
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        InternalNode split() {
            InternalNode node = new InternalNode();
            int size = children.size();
            for (int i = size - 1; i >= size / 2; i--) {
                //split right half of node into 'node'
                node.insert(this.keys.get(i), this.children.get(i));
                keys.remove(i);
                children.remove(i);
            }
            return node;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
        	List<V> toReturn = new ArrayList<V>();
        	for(Node n : children) {
                toReturn.addAll(n.rangeSearch(key, comparator));
        	}
            return toReturn;
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
        	//if node doesn't need to split
            if (!this.isOverflow()) {
                if (this.keys.size() == 0) {
                    this.keys.add(key);
                    this.values.add(value);
                } else {
                    if (key.compareTo(this.getFirstLeafKey()) < 0) {
                        this.keys.add(0, key);
                        this.values.add(0, value);
                    }
                    if (key.compareTo(this.keys.get(this.keys.size() - 1)) > 0) {
                        this.keys.add(this.keys.size(), key);
                        this.values.add(this.values.size(), value);
                    }
                    for (int i = 0; i < this.keys.size() - 1; i++) {
                        if (key.compareTo(this.keys.get(i)) > 0 && key.compareTo(this.keys.get(i + 1)) < 1) {
                            this.keys.add(i + 1, key);
                            this.values.add(i + 1, value);
                        }
                    }
                }

            //if node needs to split
            } else {
                if (this.parent == null) {
                    this.parent = new InternalNode();
                    this.parent.children.add(this);
                    root = this.parent;
                }
                LeafNode newNode = split();
                newNode.previous = this;
                newNode.parent = this.parent;
                this.next = newNode;
                this.parent.insert(newNode.getFirstLeafKey(), newNode);
            }

        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        LeafNode split() {
            LeafNode node = new LeafNode();
            int size = values.size();
            for (int i = size - 1; i >= size / 2; i--) {
            	//split right half of node into 'node'
                node.insert(this.keys.get(i), this.values.get(i));
                keys.remove(i);
                values.remove(i);
            }
            return node;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
        	List<V> toReturn = new ArrayList<V>();
        	if(key == null || comparator == null) {
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
                    while (node != null) {
                        for (int i = 0; i < node.keys.size(); i++) {
                            if (node.keys.get(i).compareTo(key) >= 0) {
                                toReturn.add(values.get(i));
                            }
                        }
                        node = node.next;
                    }
                    break;
                }
                case "<=": {
                    LeafNode node = this;
                    while (node != null) {
                        for (int i = 0; i < keys.size(); i++) {
                            if (keys.get(i).compareTo(key) <= 0) {
                                toReturn.add(values.get(i));
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
