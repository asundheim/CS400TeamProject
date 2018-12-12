package test;

import application.FoodData;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class FoodDataTest {
    @Test
    public void placeholderTest() {
        assertEquals(1, 1);
    }

    @Test
    public void testLoad() {
        FoodData foodData = new FoodData();
        foodData.loadFoodItems("foodItemsTestData.csv");
        assertEquals(15, foodData.getAllFoodItems().size());
        ArrayList<String> rules = new ArrayList<>();
        rules.add("calories <= 90.0");
        assertEquals(3, foodData.filterByNutrients(rules).size());
    }
}
