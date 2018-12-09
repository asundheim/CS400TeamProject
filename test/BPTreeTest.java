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
        this.tree = new BPTree<Integer, Integer>(3);
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
        System.out.println(this.tree.toString());
        this.tree.insert(2, 2);
        System.out.println(this.tree.toString());
        this.tree.insert(3, 3);
        System.out.println(this.tree.toString());
        this.tree.insert(4, 4);
        System.out.println(this.tree.toString());
        this.tree.insert(7, 7);
        System.out.println(this.tree.toString());
        this.tree.insert(5, 5);
        System.out.println(this.tree.toString());
        this.tree.insert(8, 8);
        System.out.println(this.tree.toString());
        this.tree.insert(0, 0);
        System.out.println(this.tree.toString());
        assertTrue(this.tree.rangeSearch(0, ">=").contains(0));
        assertTrue(this.tree.rangeSearch(2, ">=").contains(2));
        assertTrue(this.tree.rangeSearch(2, ">=").contains(4));
        assertFalse(this.tree.rangeSearch(2, ">=").contains(1));
    }
}
