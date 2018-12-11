package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Main extends Application{

	/**
	 * TODO:
	 * View a list of current food items available. *
	 * Scroll existing food list alphabetical by name *
	 * Load new foods from a data file containing new food list information (replaces existing food list) *
	 * Save existing foods to data file containing current food list information *
	 *
	 * Select (add/remove) food items to a meal list x
	 * View meal summary - (show the nutritional analysis for the food items in the meal) x
	 *
	 * Add new food items to the existing food list ?
	 *
	 * Add/remove rules to a food query (list of filter rules) x
	 * Query the food list using the food query rules x
	 *
	 * Exit the program x
	 */

	// Fields
	private static FoodData foodData;
	private static ArrayList<String> queryList;
	private static int foodCounter;

	@Override
	public void start(Stage arg0) throws Exception {
	    foodData = new FoodData();
		// Set up window
		BorderPane root = new BorderPane();
		Scene scene1 = new Scene(root, 975, 700);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		// Banner at top of window
		VBox topBox = new VBox();
		topBox.setId("topbox");
		topBox.setBackground(new Background(new BackgroundFill(Color.rgb(13, 44, 94), null, null)));
		Label title = new Label("MEAL MAKER 9000");
		title.setId("Title");
		topBox.setMinHeight(50);
		topBox.getChildren().add(title);
		root.setTop(topBox);
		
		// Center Pane ///////////
		HBox centerPane = new HBox(20);
		centerPane.setId("centerpane");

		
		///////// FOOD LIST ////////////
		VBox foodListBox = new VBox(5);
		Label foodListLabel = new Label("FOOD LIST:");
		foodListLabel.setId("Header");

		Pane foodScroll = new Pane();
		ObservableList<String> foodList = FXCollections.observableList(new ArrayList<String>());

		ListView<String> list = new ListView<String>(foodList);
		foodScroll.getChildren().addAll(list);
		foodScroll.setPrefHeight(400);
		foodScroll.setPrefWidth(230);

		HBox slButtonBox = new HBox(80);
		Button saveButton = new Button("SAVE");
		Button loadButton = new Button("LOAD");
		loadButton.setOnAction(event -> {
			Stage dialog = new Stage();
			VBox dialogVBox = new VBox(20);
			TextField fileNameField = new TextField();
			Button submitButton = new Button("LOAD");
			dialogVBox.getChildren().addAll(fileNameField, submitButton);
			Scene dialogScene = new Scene(dialogVBox, 300, 200);
			dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			dialog.setScene(dialogScene);
			dialog.show();
			submitButton.setOnAction(action -> {
				foodData.loadFoodItems(fileNameField.getCharacters().toString());
				foodList.addAll(foodData.getAllFoodItems().stream().map(FoodItem::getName).collect(Collectors.toList()));
			});
		});
		saveButton.setOnAction(event -> {
			Stage dialog = new Stage();
			VBox dialogVBox = new VBox(20);
			TextField fileNameField = new TextField();
			Button submitButton = new Button("SAVE");
			dialogVBox.getChildren().addAll(fileNameField, submitButton);
			Scene dialogScene = new Scene(dialogVBox, 300, 200);
			dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			dialog.setScene(dialogScene);
			dialog.show();
			submitButton.setOnAction(action -> foodData.saveFoodItems(fileNameField.getCharacters().toString()));
		});
		slButtonBox.getChildren().addAll(saveButton, loadButton);
		foodListBox.getChildren().addAll(foodListLabel, foodScroll, slButtonBox);
		////////////////////////////////
		
		///////// ADD BUTTON ///////////
		VBox addBox = new VBox();
		addBox.setPadding(new Insets(150,0,0,0));
		Button addButton = new Button("ADD");
		addBox.getChildren().add(addButton);
		////////////////////////////////
		
		////////// MEAL LIST ///////////
		VBox mealListBox = new VBox(5);
		Label mealListLabel = new Label("MEAL LIST:");
		mealListLabel.setId("Header");
		
		ScrollPane mealScroll = new ScrollPane();
		ObservableList<String> mealList = FXCollections.observableList(new ArrayList<String>());
		ListView<String> list2 = new ListView<String>(mealList);
		//TODO: DELETE WHEN DONE
		mealList.add("Los dulces");
		mealList.add("La galleta");
		mealList.add("El flan");
		mealScroll.setContent(list2);
		mealScroll.setPrefHeight(400);
		mealScroll.setMaxWidth(230);
		
		HBox raButtonBox = new HBox(10);
		Button removeButton = new Button("REMOVE");
		Button analyzeButton = new Button("ANALYZE");
		raButtonBox.getChildren().addAll(removeButton, analyzeButton);
		mealListBox.getChildren().addAll(mealListLabel, mealScroll, raButtonBox);
		////////////////////////////////
		
		centerPane.getChildren().addAll(foodListBox, addBox, mealListBox);
		root.setCenter(centerPane);
		//////////////////////////
		
		
		
		// Rules to filter the list //
		VBox ruleList = new VBox(15);
				
		Label rules = new Label("RULES:");
		rules.setId("Header");
				
		HBox settings = new HBox(3);
		ComboBox<String> nutrientList = new ComboBox<String>();
		nutrientList.setMaxWidth(100);
		nutrientList.getItems().addAll("calories", "fat", "protein", "carbohydrate", "fiber");
				
		ComboBox<String> comparison = new ComboBox<String>();
		comparison.setMinWidth(40);
		comparison.getItems().addAll("<=", ">=", "==");
				
		TextField nutrientVal = new TextField();
		nutrientVal.setMaxWidth(70);
        nutrientVal.textProperty().addListener((observable, oldValue, newValue) -> {
            nutrientVal.setText(newValue.replaceAll("[^\\d.]", ""));
        });
		settings.getChildren().addAll(nutrientList, comparison, nutrientVal);

		HBox ruleButtons = new HBox(30);
		Button updateButton = new Button("ADD RULE");
		updateButton.setOnAction(event -> {
            foodList.clear();
            String queryString = nutrientList.getValue() + " " + comparison.getValue() + " " + nutrientVal.getCharacters().toString();
            queryList.add(queryString);
            foodList.addAll(foodData.filterByNutrients(queryList).stream().map(FoodItem::getName).collect(Collectors.toList()));
        });
		Button resetButton = new Button("RESET");
		ruleButtons.getChildren().addAll(updateButton, resetButton);
				
		Button seeRules = new Button("SEE RULES");
		seeRules.setOnAction(event -> {
			Stage dialog = new Stage();
			VBox dialogVBox = new VBox(20);
			ObservableList<String> ruleListDialog = FXCollections.observableList(queryList);				
			ListView<String> ruleListView = new ListView<String>(ruleListDialog);
			ScrollPane ruleScroll = new ScrollPane();
			ruleScroll.setMaxWidth(250);
			ruleScroll.setMaxHeight(300);
			ruleScroll.setContent(ruleListView);

			Button removeRule = new Button("REMOVE");
			removeRule.setOnAction(eventRemove -> {
				String selectedItem = ruleListView.getSelectionModel().getSelectedItem();
				queryList.remove(selectedItem);
				ListView<String> newRuleListView = new ListView<String>(ruleListDialog);
				ruleScroll.setContent(newRuleListView);
				foodList.addAll(foodData.filterByNutrients(queryList).stream().map(FoodItem::getName).collect(Collectors.toList()));
				
			});
			Button OK = new Button("OK");
			OK.setOnAction(eventOK -> dialog.close());
			HBox dialogButtonBox = new HBox(80);
			dialogButtonBox.setPadding(new Insets(0, 0, 0, 30));
			dialogButtonBox.getChildren().addAll(removeRule, OK);
					
			dialogVBox.getChildren().addAll(ruleScroll, dialogButtonBox);
			Scene dialogScene = new Scene(dialogVBox, 250, 375);
			dialog.setScene(dialogScene);
			dialog.show();
		});

		ruleList.getChildren().addAll(rules, settings, ruleButtons, seeRules);
		/////////////////////////////
				
		// ADD TO FOOD LIST SECTION //
		VBox addFoodBox = new VBox(5);
		VBox labelBox = new VBox(14);
		VBox fieldBox = new VBox(5);
		HBox labelFieldBox = new HBox(3);
		Label addFood = new Label("ADD TO FOOD LIST:");
		addFood.setId("Header");
		Label name = new Label("Name of Food: ");	TextField nameField = new TextField();
		Label protein = new Label("Protein(g): ");	TextField proteinField = new TextField();
        proteinField.textProperty().addListener((observable, oldValue, newValue) -> {
            proteinField.setText(newValue.replaceAll("[^\\d.]", ""));
        });
		Label calories = new Label("Calories: ");	TextField caloriesField = new TextField();
        caloriesField.textProperty().addListener((observable, oldValue, newValue) -> {
            caloriesField.setText(newValue.replaceAll("[^\\d.]", ""));
        });
		Label fiber = new Label("Fiber(g): ");		TextField fiberField = new TextField();
        fiberField.textProperty().addListener((observable, oldValue, newValue) -> {
            fiberField.setText(newValue.replaceAll("[^\\d.]", ""));
        });
		Label fat = new Label("Fat(g)");			TextField fatField = new TextField();
        fatField.textProperty().addListener((observable, oldValue, newValue) -> {
            fatField.setText(newValue.replaceAll("[^\\d.]", ""));
        });
		Label carbs = new Label("Carbs(g): ");		TextField carbsField = new TextField();
        carbsField.textProperty().addListener((observable, oldValue, newValue) -> {
            carbsField.setText(newValue.replaceAll("[^\\d.]", ""));
        });
		Button addFoodButton = new Button("ADD FOOD");
		addFoodButton.setOnAction(eventAddFood -> {
			String nameVal = nameField.getCharacters().toString();
			double proteinVal = Double.parseDouble(proteinField.getCharacters().toString());
			double caloriesVal = Double.parseDouble(caloriesField.getCharacters().toString());
			double fiberVal = Double.parseDouble(fiberField.getCharacters().toString());
			double fatVal = Double.parseDouble(fatField.getCharacters().toString());
			double carbsVal = Double.parseDouble(carbsField.getCharacters().toString());
			String idVal = "" + foodCounter++;

			FoodItem newFood = new FoodItem(idVal, nameVal);
			newFood.addNutrient("protein", proteinVal);
			newFood.addNutrient("calories", caloriesVal);
			newFood.addNutrient("fiber", fiberVal);
			newFood.addNutrient("fat", fatVal);
			newFood.addNutrient("carbs", carbsVal);
			
			foodData.addFoodItem(newFood);
		});
				
		labelBox.getChildren().addAll(name, protein, calories, fiber, fat, carbs);
		fieldBox.getChildren().addAll(nameField, proteinField, caloriesField, 
				fiberField, fatField, carbsField);
		
		labelFieldBox.getChildren().addAll(labelBox, fieldBox);
		addFoodBox.getChildren().addAll(addFood, labelFieldBox, addFoodButton);
		//////////////////////////////
				
				
		//////// Left Pane //////////
		VBox leftPane = new VBox(90);
		leftPane.setId("leftpane");
		leftPane.getChildren().addAll(ruleList, addFoodBox);
		root.setLeft(leftPane);
		////////////////////////////
				
		arg0.setScene(scene1);
		arg0.show();
	}
	
	public static void main(String[] args) {
		foodData = new FoodData();
		queryList = new ArrayList<String>();
		foodCounter = 1;
		launch(args);
	}
}
