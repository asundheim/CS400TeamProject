package application;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
		// Set up window
		BorderPane root = new BorderPane();
		Scene scene1 = new Scene(root, 900, 900);
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
		nutrientList.getItems().addAll(
					"calories",
			        "fat",
			        "protein",
			        "carbs",
			        "fiber");
		
		ComboBox<String> comparison = new ComboBox<String>();
		comparison.setMinWidth(100);
		comparison.getItems().addAll(
				"<",
				">");
		
		TextField nutrientVal = new TextField();
		nutrientVal.setMaxWidth(100);
		
		Button updateButton = new Button("UPDATE LIST");
		// TODO: Have button add contents of rules to list
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
		
		HBox nameBox = new HBox(3);
		HBox proteinBox = new HBox(3);
		HBox caloriesBox = new HBox(3);
		HBox fiberBox = new HBox(3);
		HBox fatBox = new HBox(3);
		HBox carbsBox = new HBox(3);
		
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
		rules = new ArrayList<String>();
		launch(args);
	}
}
