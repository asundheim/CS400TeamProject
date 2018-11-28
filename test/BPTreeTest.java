package test;

import application.BPTree;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BPTreeTest {
    BPTree<Integer, Integer> tree;
    @Before
    public void beforeEach() {
        this.tree = new BPTree<Integer, Integer>(4);
    }
    @Test
    public void placeholderTest() {
        assertEquals(1, 1);
    }

    @Test
    public void insertTest() {
        this.tree.insert(1, 1);
        assertEquals(1, this.tree.rangeSearch(1, "==").size());
    }
}
