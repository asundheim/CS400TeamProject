package test;
import application.FoodItem;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FoodItemTest {
    private FoodItem foodItem;
    @Before
    public void setUp() {
        this.foodItem = new FoodItem("1", "apple");
    }
    @Test
    public void testName() {
        assertEquals("apple", this.foodItem.getName());
    }
    @Test
    public void testId() {
        assertEquals("1", this.foodItem.getID());
    }
    @Test
    public void testNutrients() {
        this.foodItem.addNutrient("v", 10);
        this.foodItem.addNutrient("c", 5);
        assertEquals(10, this.foodItem.getNutrientValue("v"), 0.0);
        this.foodItem.addNutrient("c", 7);
        assertEquals(7, this.foodItem.getNutrientValue("c"), 0.0);
        this.foodItem.addNutrient("x", 10);
        Arrays.stream(new String[] {"v", "c", "x"}).forEach(x -> {
            assertTrue(this.foodItem.getNutrients().containsKey(x));
        });
        assertEquals(0, this.foodItem.getNutrientValue("p"), 0);
    }
}
