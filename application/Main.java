package application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application{
	
	// Fields
	private static FoodData foodData;
	private static ArrayList<String> rules;

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
		
		/*  PAGE NAVIGATION BUTTONS -- NOT NEEDED RIGHT NOW
		// Menu on left side
		Button makeMeal = new Button("MAKE MEAL");
		Button loadList = new Button("LOAD LIST");
		Button addFood = new Button("ADD FOOD");
		makeMeal.setMinWidth(150);
		makeMeal.setBackground(new Background(new BackgroundFill(Color.rgb(13, 44, 94), null, null)));
		makeMeal.setFont(Font.font("Times", 18));
		makeMeal.setTextFill(Color.WHITE);
		makeMeal.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				makeMeal.setBackground(new Background(new BackgroundFill(Color.rgb(42, 94, 178), null, null)));
				
			}
		});
		makeMeal.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				makeMeal.setBackground(new Background(new BackgroundFill(Color.rgb(13, 44, 94), null, null)));
				
			}
		});
		
		loadList.setMinWidth(150);
		loadList.setBackground(new Background(new BackgroundFill(Color.rgb(114, 170, 255), null, null)));
		loadList.setFont(Font.font("Times", 18));
		loadList.setTextFill(Color.WHITE);
		loadList.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				loadList.setBackground(new Background(new BackgroundFill(Color.rgb(42, 94, 178), null, null)));
				
			}
		});
		loadList.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				loadList.setBackground(new Background(new BackgroundFill(Color.rgb(114, 170, 255), null, null)));
				
			}
		});
		
		// Add Food button
		addFood.setMinWidth(150);
		addFood.setBackground(new Background(new BackgroundFill(Color.rgb(114, 170, 255), null, null)));
		addFood.setFont(Font.font("Times", 18));
		addFood.setTextFill(Color.WHITE);
		addFood.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				addFood.setBackground(new Background(new BackgroundFill(Color.rgb(42, 94, 178), null, null)));
				
			}
		});
		addFood.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				addFood.setBackground(new Background(new BackgroundFill(Color.rgb(114, 170, 255), null, null)));
				
			}
		});
		
		// List of pages to navigate to //
		VBox sceneList = new VBox(5);
		sceneList.setPadding(new Insets(40, 0, 0, 5));
		sceneList.setMinWidth(225);
		sceneList.getChildren().addAll(makeMeal, loadList, addFood);
		/////////////////////////////////
		 * 
		 */
		
		
		// Rules to filter the list //
		VBox ruleList = new VBox(15);
		ruleList.setPadding(new Insets(0, 0, 0, 25));
		
		Label rules = new Label("RULES:");
		rules.setId("Header");
		rules.setFont(Font.font("Times", 20));
		
		ComboBox<String> nutrientList = new ComboBox<String>();
		nutrientList.setMinWidth(100);
		nutrientList.getItems().addAll("calories", "fat", "protein", "carbohydrate", "fiber");
		
		ComboBox<String> comparison = new ComboBox<String>();
		comparison.setMinWidth(100);
		comparison.getItems().addAll("<=", ">=", "=");
		
		TextField nutrientVal = new TextField();
		nutrientVal.setMaxWidth(100);
		
		Button updateButton = new Button("UPDATE LIST");

		ruleList.getChildren().addAll(rules, nutrientList, comparison, nutrientVal, updateButton);
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
		Label calories = new Label("Calories: ");	TextField caloriesField = new TextField();
		Label fiber = new Label("Fiber(g): ");		TextField fiberField = new TextField();
		Label fat = new Label("Fat(g)");			TextField fatField = new TextField();
		Label carbs = new Label("Carbs(g): ");		TextField carbsField = new TextField();
		Button addFoodButton = new Button("ADD FOOD");
		
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
		
		// Center Pane ///////////
		HBox centerPane = new HBox(20);
		centerPane.setId("centerpane");
		
		///////// FOOD LIST ////////////
		VBox foodListBox = new VBox(5);
		Label foodListLabel = new Label("FOOD LIST:");
		foodListLabel.setId("Header");
		
		ScrollPane foodScroll = new ScrollPane();
		ObservableList<String> foodList = FXCollections.observableList(new ArrayList<String>());

		ListView<String> list = new ListView<String>(foodList);
		foodScroll.setContent(list);
		foodScroll.setPrefHeight(400);
		foodScroll.setPrefWidth(230);
		
		HBox slButtonBox = new HBox(80);
		Button saveButton = new Button("SAVE");
		Button loadButton = new Button("LOAD");
		loadButton.setOnAction(event -> {
            Stage dialog = new Stage();
            VBox dialogVbox = new VBox(20);
            TextField fileNameField = new TextField();
            Button submitButton = new Button("LOAD");
            dialogVbox.getChildren().addAll(fileNameField, submitButton);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
            submitButton.setOnAction(action -> {
                foodData.loadFoodItems(fileNameField.getCharacters().toString());
                foodList.addAll(foodData.getAllFoodItems().stream().map(FoodItem::getName).collect(Collectors.toList()));
            });
        });
		saveButton.setOnAction(event -> {
            Stage dialog = new Stage();
            VBox dialogVbox = new VBox(20);
            TextField fileNameField = new TextField();
            Button submitButton = new Button("SAVE");
            dialogVbox.getChildren().addAll(fileNameField, submitButton);
            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();
            submitButton.setOnAction(action -> foodData.saveFoodItems(fileNameField.getCharacters().toString()));
        });
        updateButton.setOnAction(event -> {
            foodList.clear();
            String queryString = nutrientList.getValue() + " " + comparison.getValue() + " " + nutrientVal.getCharacters().toString();
            ArrayList<String> queryList = new ArrayList<>();
            queryList.add(queryString);
            foodList.addAll(foodData.filterByNutrients(queryList).stream().map(FoodItem::getName).collect(Collectors.toList()));
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
		
		arg0.setScene(scene1);
		arg0.show();
	}
	
	public static void main(String[] args) {
		foodData = new FoodData();
		rules = new ArrayList<String>();
		launch(args);
	}
}
