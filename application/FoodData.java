package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {

    // List of all the food items.
    private List<FoodItem> foodItemList;

    // Map of nutrients and their corresponding index
    // TODO: remake this private once it gets fixed
    public HashMap<String, BPTree<Double, FoodItem>> indexes;

    /**
     * Public constructor
     * 
     */
    public FoodData() {
        this.foodItemList = new ArrayList<>();
        this.indexes = new HashMap<>();
        int BRANCHING_FACTOR = 10;
        this.indexes.put("calories", new BPTree<>(BRANCHING_FACTOR));
        this.indexes.put("fat", new BPTree<>(BRANCHING_FACTOR));
        this.indexes.put("carbohydrate", new BPTree<>(BRANCHING_FACTOR));
        this.indexes.put("fiber", new BPTree<>(BRANCHING_FACTOR));
        this.indexes.put("protein", new BPTree<>(BRANCHING_FACTOR));
    }

    
    /*
     * (non-Javadoc)
     * @see skeleton.application.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
        try {
            this.foodItemList = Files.lines(new File(filePath).toPath())
                    .map(x -> x.split(","))
                    .filter(x -> x.length == 12)
                    .map(x -> {
                        FoodItem food = new FoodItem(x[0], x[1]);
                        for (int i = 2; i < x.length; i+=2) {
                            food.addNutrient(x[i].toLowerCase(), Double.parseDouble(x[i + 1]));
                        }
                        return food;
                    })
                    .collect(Collectors.toList());
            this.sortFoodItems(this.foodItemList);
            this.foodItemList.forEach((FoodItem foodItem) -> {
                foodItem.getNutrients().forEach((String nutrient, Double value) -> {
                    this.indexes.get(nutrient).insert(value, foodItem);
                });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sortFoodItems(List<FoodItem> list) {
        list.sort(Comparator.comparing((FoodItem x) -> x.getName().toLowerCase()));
    }
    /*
     * (non-Javadoc)
     * @see skeleton.application.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        return this.foodItemList
                .stream()
                .filter(x -> x.getName().toLowerCase().contains(substring.toLowerCase()))
                .collect(Collectors.toList());
    }

    /*
     * (non-Javadoc)
     * @see skeleton.application.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
        if (rules.size() == 0) {
            return this.foodItemList;
        }
        List<List<FoodItem>> matches = rules.stream()
                .map(rule -> rule.split(" "))
                .filter(rule -> rule.length == 3)
                .map(rule -> this.indexes.get(rule[0]).rangeSearch(Double.parseDouble(rule[2]), rule[1]))
                .collect(Collectors.toList());
        if (matches.size() > 0) {
            matches.forEach(list -> matches.get(0).retainAll(list));
            sortFoodItems(matches.get(0));
            return matches.get(0);
        } else {
            return new ArrayList<FoodItem>();
        }
    }

    /*
     * (non-Javadoc)
     * @see skeleton.application.FoodDataADT#addFoodItem(skeleton.application.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        foodItem.getNutrients().forEach((String nutrient, Double value) -> {
            this.indexes.get(nutrient).insert(value, foodItem);
        });
        this.foodItemList.add(foodItem);
        this.sortFoodItems(this.foodItemList);
    }

    /*
     * (non-Javadoc)
     * @see skeleton.application.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return this.foodItemList;
    }

    /**
     * Save the list of food items in ascending order by name
     *
     * @param filename name of the file where the data needs to be saved
     */
    @Override
    public void saveFoodItems(String filename) {
        try {
            Files.write(new File(filename).toPath(),
                    this.foodItemList
                        .stream()
                        .map(item -> {
                            ArrayList<String> nutrients = new ArrayList<>();
                            nutrients.add("calories");
                            nutrients.add("" + item.getNutrientValue("calories"));
                            nutrients.add("fat");
                            nutrients.add("" + item.getNutrientValue("fat"));nutrients.add("fat");
                            nutrients.add("carbohydrate");
                            nutrients.add("" + item.getNutrientValue("carbohydrate"));
                            nutrients.add("fiber");
                            nutrients.add("" + item.getNutrientValue("fiber"));
                            nutrients.add("protein");
                            nutrients.add("" + item.getNutrientValue("protein"));
                            return item.getID() + "," + item.getName() + "," + String.join(",", nutrients);
                        })
                        .collect(Collectors.toList())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
