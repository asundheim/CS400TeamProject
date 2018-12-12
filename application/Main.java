package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends Application{

	/**
	 * TODO:
	 * View a list of current food items available. *
	 * Scroll existing food list alphabetical by name *
	 * Load new foods from a data file containing new food list information (replaces existing food list) *
	 * Save existing foods to data file containing current food list information *
	 *
	 * Select (add/remove) food items to a meal list *
	 * View meal summary - (show the nutritional analysis for the food items in the meal) *
	 *
	 * Add new food items to the existing food list *
	 *
	 * Add/remove rules to a food query (list of filter rules) *
	 * Query the food list using the food query rules *
	 *
	 * Exit the program *
	 */

	// Fields
	private static FoodData foodData;
	private static ArrayList<String> queryList;
	private String filterString = "";
	private static int foodCounter;

	/**
	 * Start method to run the entire program
	 * @param arg0 the main stage in which the program resides
	 */
	@Override
	public void start(Stage arg0) throws Exception {
	    foodData = new FoodData();
		// Set up window
		BorderPane root = new BorderPane();
		Scene scene1 = new Scene(root, 1000, 700);
		scene1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		// Banner at top of window
		VBox topBox = new VBox();
		topBox.setId("topbox");
		topBox.setBackground(new Background(new BackgroundFill(Color.rgb(13, 44, 94), null, null)));
		// Title
		Label title = new Label("MEAL MAKER 9000");
		title.setId("Title");
		topBox.setMinHeight(50);
		topBox.getChildren().add(title);
		root.setTop(topBox);
		
		// Center Pane ///////////
		
		HBox centerPane = new HBox(20);
		centerPane.setId("centerpane");

		
		///////// FOOD LIST ////////////
		// Setup food list box
		VBox foodListBox = new VBox(5);
		Label foodListLabel = new Label("FOOD LIST:");
		foodListLabel.setId("Header");
		
		// Setup scroll-able box to view food items
		Pane foodScroll = new Pane();
		ObservableList<String> foodList = FXCollections.observableList(new ArrayList<String>());
		List<FoodItem> foodItems = foodData.getAllFoodItems();
		//add all food items to the list
		foodList.addAll(foodItems.stream().map(FoodItem::getName).collect(Collectors.toList()));

		//made list viewable
		ListView<String> list = new ListView<String>(foodList);
		foodScroll.getChildren().addAll(list);
		foodScroll.setPrefHeight(400);
		foodScroll.setPrefWidth(230);

		//Setup box for save and load buttons
		HBox slButtonBox = new HBox(98);
		//setup save and load buttons
		Button saveButton = new Button("SAVE");
		Button loadButton = new Button("LOAD");
		//when load button is pressed...
		loadButton.setOnAction(event -> {
			//open a new dialog
			Stage dialog = new Stage();
			dialog.setResizable(false);
			VBox dialogVBox = new VBox(20);
			dialogVBox.setAlignment(Pos.TOP_CENTER);
			dialogVBox.setPadding(new Insets(10));
			//prompt user for a file name
			TextField fileNameField = new TextField();
			fileNameField.setPromptText("Enter File Name");
			Button submitButton = new Button("LOAD");
			dialogVBox.getChildren().addAll(fileNameField, submitButton);
			Scene dialogScene = new Scene(dialogVBox, 300, 200);
			//make file look nice
			dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			dialog.setScene(dialogScene);
			dialog.show();
			//when the submit button is pressed...
			submitButton.setOnAction(action -> {
				//add all the food items from the file to the food items list
				foodData.loadFoodItems(fileNameField.getCharacters().toString());
                foodList.addAll(foodData.filterByNutrients(queryList)
                        .stream()
                        .filter(x -> foodData.filterByName(filterString).contains(x))
                        .map(FoodItem::getName)
                        .collect(Collectors.toList())
                );
                //close the dialog box
                dialog.close();
			});
		});
		//when the save button is pressed...
		saveButton.setOnAction(event -> {
			//create a new dialog box
			Stage dialog = new Stage();
			dialog.setResizable(false);
			VBox dialogVBox = new VBox(20);
			dialogVBox.setAlignment(Pos.TOP_CENTER);
			dialogVBox.setPadding(new Insets(10));
			//prompt the user for a file name to save to
			TextField fileNameField = new TextField();
			fileNameField.setPromptText("Enter File Name");
			fileNameField.setFocusTraversable(false);
			Button submitButton = new Button("SAVE");
			dialogVBox.getChildren().addAll(fileNameField, submitButton);
			Scene dialogScene = new Scene(dialogVBox, 300, 200);
			fileNameField.requestFocus();
			//make it look nice with css
			dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			dialog.setScene(dialogScene);
			dialog.show();
			//when the submit but is pressed...
			submitButton.setOnAction(action -> {
				//save the food items
                foodData.saveFoodItems(String.join(",", foodList) + "-----" + fileNameField.getCharacters().toString());
                dialog.close();
            });
		});
		//add buttons to the box
		slButtonBox.getChildren().addAll(saveButton, loadButton);
		//add everything to the food list box
		foodListBox.getChildren().addAll(foodListLabel, foodScroll, slButtonBox);
		////////////////////////////////



		////////// MEAL LIST ///////////
		VBox mealListBox = new VBox(5);
		Label mealListLabel = new Label("MEAL LIST:");
		mealListLabel.setId("Header");
		//create a scrollable pane for the meal
		Pane mealScroll = new Pane();
		ObservableList<String> mealList = FXCollections.observableList(new ArrayList<String>());
		ListView<String> list2 = new ListView<String>(mealList);

		mealScroll.getChildren().addAll(list2);
		mealScroll.setPrefHeight(400);
		mealScroll.setMaxWidth(230);
        ///////// ADD BUTTON ///////////
        VBox addBox = new VBox();
        addBox.setPadding(new Insets(200,0,0,0));
        Button addButton = new Button("ADD");
        //when the add button is pressed...
        addButton.setOnAction(action -> {
        	//make sure an item is selected
            if (list.getSelectionModel().getSelectedItem() != null) {
            	//add the item to the meal
                mealList.add(list.getSelectionModel().getSelectedItem());
            }
        });
        addBox.getChildren().add(addButton);
        ////////////////////////////////
        //box for remove and analyze box
		HBox raButtonBox = new HBox(30);
		Button removeButton = new Button("REMOVE");
		//when the remove button is pressed...
		removeButton.setOnAction(action -> {
			//if selected isn't null
            if (list2.getSelectionModel().getSelectedItem() != null) {
            	//remove selected
                mealList.remove(list2.getSelectionModel().getSelectedItem());
            }
        });
		Button analyzeButton = new Button("ANALYZE");
		//when the analyze button is pressed
		analyzeButton.setOnAction(action -> {
			//select the items in the meal
            ArrayList<FoodItem> selectedItems = new ArrayList<>(foodData.getAllFoodItems()
                    .stream()
                    .filter(x -> mealList.contains(x.getName()))
                    .collect(Collectors.toList())
            );
            //create a new dialog to display meal nutrients
            Stage dialog = new Stage();
            dialog.setResizable(false);
            VBox dialogVBox = new VBox(20);
            Label message = new Label("Kowalski: Analysis");
            message.setId("Header");
            //get protein info and display it
            Label proteinInfo = new Label(selectedItems
                    .stream()
                    .mapToDouble(x -> x.getNutrientValue("protein"))
                    .sum() + " grams of protein"
            );
            //get calorie info and display it
            Label caloriesInfo = new Label(selectedItems
                    .stream()
                    .mapToDouble(x -> x.getNutrientValue("calories"))
                    .sum() + " calories"
            );
            //get fiber info and display it
            Label fiberInfo = new Label(selectedItems
                    .stream()
                    .mapToDouble(x -> x.getNutrientValue("fiber"))
                    .sum() + " grams of fiber"
            );
            //get fat info and display it
            Label fatInfo = new Label(selectedItems
                    .stream()
                    .mapToDouble(x -> x.getNutrientValue("fat"))
                    .sum() + " grams of fat"
            );
            //get carb info and display it
            Label carbInfo = new Label(selectedItems
                    .stream()
                    .mapToDouble(x -> x.getNutrientValue("carbohydrate"))
                    .sum() + " grams of carbohydrates"
            );
            //create a confirmation button
            HBox buttonBox = new HBox();
            buttonBox.setAlignment(Pos.CENTER);
            Button confirmButton = new Button("OK");
            //add everything to the dialog and make it look nice
            buttonBox.getChildren().add(confirmButton);
            dialogVBox.getChildren().addAll(message, proteinInfo, caloriesInfo, fiberInfo, fatInfo, carbInfo, buttonBox);
            dialogVBox.setPadding(new Insets(10));;
            Scene dialogScene = new Scene(dialogVBox, 300, 300);
            dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            //display the dialog
            dialog.setScene(dialogScene);
            dialog.show();
            //when confirm button is pressed, close the dialog
            confirmButton.setOnAction(x -> dialog.close());
        });
		//add remove and analyze buttons
		raButtonBox.getChildren().addAll(removeButton, analyzeButton);
		//add meal box
		mealListBox.getChildren().addAll(mealListLabel, mealScroll, raButtonBox);
		////////////////////////////////
		//add food list and meal list and add box to main center pane
		centerPane.getChildren().addAll(foodListBox, addBox, mealListBox);
		root.setCenter(centerPane);
		//////////////////////////

		// Rules to filter the list //
		//setup rule box
		VBox ruleList = new VBox(15);
		Label rules = new Label("RULES:");
		rules.setId("Header");
		TextField nameFilter = new TextField();
		//filter by food name
		nameFilter.setPromptText("Filter By Name");
		nameFilter.setMaxWidth(242);
		//as one types, update the filtered list
		nameFilter.textProperty().addListener((observable, oldValue, newValue) -> {
		    filterString = newValue;
            foodList.clear();
            foodList.addAll(foodData.filterByName(filterString)
                    .stream()
                    .filter(x -> foodData.filterByNutrients(queryList).contains(x))
                    .map(FoodItem::getName)
                    .collect(Collectors.toList())
            );
		});
				
		//setup drop down menu for defining rules
		HBox settings = new HBox(3);
		ComboBox<String> nutrientList = new ComboBox<String>();
		nutrientList.setMaxWidth(100);
		nutrientList.getItems().addAll("calories", "fat", "protein", "carbohydrate", "fiber");
				
		//drop down for comparison operators
		ComboBox<String> comparison = new ComboBox<String>();
		comparison.setMinWidth(40);
		comparison.getItems().addAll("<=", ">=", "==");
				
		//text field for entering a value for the rulle
		TextField nutrientVal = new TextField();
		nutrientVal.setMaxWidth(70);
		//only able to add valid numbers
        nutrientVal.textProperty().addListener((observable, oldValue, newValue) -> {
            nutrientVal.setText(newValue.replaceAll("[^\\d.]", ""));
        });
        //add everything to the settings box
		settings.getChildren().addAll(nutrientList, comparison, nutrientVal);

		//add "add rule" button
		HBox ruleButtons = new HBox(30);
		Button updateButton = new Button("ADD RULE");
		updateButton.setMinWidth(125);
		//when the add rule button is added
		updateButton.setOnAction(event -> {
			//clear the food list
            foodList.clear();
            //add everything within the rules back to the list
            String queryString = nutrientList.getValue() + " " + comparison.getValue() + " " + nutrientVal.getCharacters().toString();
            queryList.add(queryString);
            foodList.addAll(foodData.filterByNutrients(queryList)
                    .stream()
                    .filter(x -> foodData.filterByName(filterString).contains(x))
                    .map(FoodItem::getName)
                    .collect(Collectors.toList())
            );
        });
		//create reset button to clear rules
		Button resetButton = new Button("RESET");
		//when reset is pressed...
		resetButton.setOnAction(action -> {
			//clear list
		    queryList.clear();
		    foodList.clear();
		    //add back all items 
		    foodList.addAll(foodData.filterByNutrients(queryList)
                    .stream()
                    .filter(x -> foodData.filterByName(filterString).contains(x))
                    .map(FoodItem::getName)
                    .collect(Collectors.toList())
            );
        });

		//add rule buttons
		ruleButtons.getChildren().addAll(updateButton, resetButton);

		//add "See Rules" button
		Button seeRules = new Button("SEE RULES");
		seeRules.setMinWidth(125);
		// when see rules is pressed...
		seeRules.setOnAction(event -> {
			//create a new dialog
			Stage dialog = new Stage();
			dialog.setResizable(false);
			VBox dialogVBox = new VBox(20);
			//create list of rules
			ObservableList<String> ruleListDialog = FXCollections.observableList(queryList);				
			ListView<String> ruleListView = new ListView<String>(ruleListDialog);
			//show rules in a scrollabe pane
			Pane ruleScroll = new Pane();
			ruleScroll.setMaxWidth(250);
			ruleScroll.setMaxHeight(300);
			ruleScroll.getChildren().addAll(ruleListView);

			//create remove rule button
			Button removeRule = new Button("REMOVE");
			//when remove is pressed...
			removeRule.setOnAction(eventRemove -> {
				//get the selected item and remove it
				String selectedItem = ruleListView.getSelectionModel().getSelectedItem();
                ruleListDialog.remove(selectedItem);
				queryList.remove(selectedItem);
				//update food list 
				foodList.addAll(foodData.filterByNutrients(queryList)
                        .stream()
                        .filter(x -> foodData.filterByName(filterString).contains(x))
                        .map(FoodItem::getName)
                        .collect(Collectors.toList())
                );
			});
			//create ok button
			Button OK = new Button("OK");
			//close dialog when pressed
			OK.setOnAction(eventOK -> dialog.close());
			HBox dialogButtonBox = new HBox(50);
			dialogButtonBox.setPadding(new Insets(0, 0, 0, 20));
			dialogButtonBox.getChildren().addAll(removeRule, OK);
					
			//add rule scroll and dialog button box
			dialogVBox.getChildren().addAll(ruleScroll, dialogButtonBox);
			//make it look nice
			Scene dialogScene = new Scene(dialogVBox, 250, 375);
			dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			dialog.setScene(dialogScene);
			dialog.show();
		});

		//add everything to rulelist
		ruleList.getChildren().addAll(rules, nameFilter, settings, ruleButtons, seeRules);
		/////////////////////////////
				
		// ADD TO FOOD LIST SECTION //
		VBox addFoodBox = new VBox(5);
		VBox labelBox = new VBox(14);
		VBox fieldBox = new VBox(5);
		HBox labelFieldBox = new HBox(3);
		Label addFood = new Label("ADD TO FOOD LIST:");
		addFood.setId("Header");
		//several text fields for defining protein, calories, fiber, fat, and carbs
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
        //add food button
		Button addFoodButton = new Button("ADD FOOD");
		//when food button is pressed...
		addFoodButton.setOnAction(eventAddFood -> {
			//try to parse the inputs as numbers 
			try {
				String nameVal = nameField.getCharacters().toString();
				if (nameVal.trim().equals("")) {
					throw new Exception();
				}
				double proteinVal = Double.parseDouble(proteinField.getCharacters().toString());
				double caloriesVal = Double.parseDouble(caloriesField.getCharacters().toString());
				double fiberVal = Double.parseDouble(fiberField.getCharacters().toString());
				double fatVal = Double.parseDouble(fatField.getCharacters().toString());
				double carbsVal = Double.parseDouble(carbsField.getCharacters().toString());
				String idVal = "" + foodCounter++;

				//add the nutrients to the new food item
				FoodItem newFood = new FoodItem(idVal, nameVal);
				newFood.addNutrient("protein", proteinVal);
				newFood.addNutrient("calories", caloriesVal);
				newFood.addNutrient("fiber", fiberVal);
				newFood.addNutrient("fat", fatVal);
				newFood.addNutrient("carbohydrate", carbsVal);

				//add the food item to the food list
				foodData.addFoodItem(newFood);
				foodList.clear();
				foodList.addAll(foodData.filterByNutrients(queryList)
                        .stream()
                        .filter(x -> foodData.filterByName(filterString).contains(x))
                        .map(FoodItem::getName)
                        .collect(Collectors.toList())
                );
			//if a number cannot be parsed correctly...
			} catch (NumberFormatException e) {
				//create a dialog to inform the user they have invalid input
				Stage dialog = new Stage();
				dialog.setResizable(false);
				VBox dialogVBox = new VBox(20);
				HBox buttonBox = new HBox();
				buttonBox.setPadding(new Insets(0, 0, 0, 90));
				dialogVBox.setPadding(new Insets(20, 0, 0, 40));
				Button confirmButton = new Button("OK");
				buttonBox.getChildren().addAll(confirmButton);
				Label message = new Label("Incorrect / Missing values in Nutrients");
				message.setId("popup");
				dialogVBox.getChildren().addAll(message, buttonBox);
				Scene dialogScene = new Scene(dialogVBox, 300, 200);
				dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				dialog.setScene(dialogScene);
				dialog.show();
				confirmButton.setOnAction(action -> dialog.close());
			} catch (Exception e) {
				//catch general execeptions
				//inform the user they don't have the required info in a dialog
                Stage dialog = new Stage();
                dialog.setResizable(false);
                VBox dialogVBox = new VBox(20);
                HBox buttonBox = new HBox();
                buttonBox.setPadding(new Insets(0, 0, 0, 40));
				dialogVBox.setPadding(new Insets(20, 0, 0, 90));
                Label message = new Label("Missing Food Name");
                message.setId("popup");
                Button confirmButton = new Button("OK");
                buttonBox.getChildren().addAll(confirmButton);
                dialogVBox.getChildren().addAll(message, buttonBox);
                Scene dialogScene = new Scene(dialogVBox, 300, 200);
                dialogScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                dialog.setScene(dialogScene);
                dialog.show();
                confirmButton.setOnAction(action -> {
                    dialog.close();
                });
			}
			//reset all the text fields to blank
			nameField.setText("");
			proteinField.setText("");
			caloriesField.setText("");
			fiberField.setText("");
			fatField.setText("");
			carbsField.setText("");
		});
				
		//add everything to the appropriate boxes
		labelBox.getChildren().addAll(name, protein, calories, fiber, fat, carbs);
		fieldBox.getChildren().addAll(nameField, proteinField, caloriesField, 
				fiberField, fatField, carbsField);
		
		labelFieldBox.getChildren().addAll(labelBox, fieldBox);
		addFoodBox.getChildren().addAll(addFood, labelFieldBox, addFoodButton);
		//////////////////////////////
				
				
		//////// Left Pane //////////
		//add appropriate modules to the left pane
		VBox leftPane = new VBox(90);
		leftPane.setId("leftpane");
		leftPane.getChildren().addAll(ruleList, addFoodBox);
		root.setLeft(leftPane);
		////////////////////////////
		//display the main stage
		arg0.setResizable(false);
		arg0.setScene(scene1);
		arg0.show();
	}
	
	/*
	 * initialize the program
	 */
	public static void main(String[] args) {
		foodData = new FoodData();
		queryList = new ArrayList<String>();
		foodCounter = 1;
		launch(args);
	}
}
