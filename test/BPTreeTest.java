package test;

import application.BPTree;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

    @Test
    public void rangeSearchTest() {
        this.tree.insert(1, 1);
        this.tree.insert(2, 2);
        this.tree.insert(3, 3);
        this.tree.insert(4, 4);
        assertTrue(this.tree.rangeSearch(2, ">=").contains(2));
        assertTrue(this.tree.rangeSearch(2, ">=").contains(3));
        assertTrue(this.tree.rangeSearch(2, ">=").contains(4));
        assertFalse(this.tree.rangeSearch(2, ">=").contains(1));
    }
}
