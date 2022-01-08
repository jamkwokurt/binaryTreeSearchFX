package tree;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class TreeSearchMain extends Application {
	Person firstNameRoot,lastNameRoot;
	//	Scanner scanner = new Scanner(System.in);
	Scene scene;
	Group entry;
	BorderPane mainPane = new BorderPane();//BorderPane for menubar, print area and display area
	Pane contentPane = new Pane();//tree diagram display area

	//menubar field
	MenuBar menuBar = new MenuBar();
	Menu load = new Menu("Load");
	Menu print = new Menu("Print");
	Menu initialise = new Menu("Initialise");
	Menu display = new Menu("Display");
	HBox menuBox = new HBox(10);
	TextField searchField = new TextField();
	ComboBox<String> nameTypeCB = new ComboBox<>();
	Button searchButton = new Button();
	Label length = new Label("*Please select name type first*");
	ComboBox<String> lengthBox = new ComboBox<>();
	Button show = new Button("Show Result");
	TextArea listNameArea = new TextArea();

	//print field
	Label orderTypeLabel = new Label();
	TextFlow nameFlow;
	TextFlow fDepInFlow,fDepPreFlow,fDepPostFlow,fBreFlow;
	TextFlow lDepInFlow,lDepPreFlow,lDepPostFlow,lBreFlow;
	VBox printBox = new VBox(2);

	//initialise screen
	public void initialise() {
		mainPane.getChildren().removeAll(printBox,contentPane);
		this.firstNameRoot = null;
		this.lastNameRoot = null;
	}
	//Create Person and make connection using data from file
	public void loadNameData() {
		
		try {
			Scanner scan = new Scanner(new File("mswdev.csv"));
			while (scan.hasNextLine()) {
				String firstName = scan.next();
				String lastName = scan.nextLine().trim();
				//Person object constructed in first name - last name order
				if(firstNameRoot == null) {
					firstNameRoot = new Person(firstName,lastName);
				}else {
					addPerson(firstNameRoot,new Person(firstName,lastName));
				}
				//Person object constructed in last name - first name order
				if(lastNameRoot == null) {
					lastNameRoot = new Person(lastName,firstName);
				}else {
					addPerson(lastNameRoot,new Person(lastName,firstName));
				}
			}
			searchButton.setDisable(false);
			show.setDisable(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Add new Person to current Person by comparing names
	public void addPerson(Person currentP, Person newP) {
		if (currentP.getFirstName().compareTo(newP.getFirstName()) > 0) {
			if (currentP.getLeft() == null) {
				currentP.setLeft(newP);
				//				newP.setPersonParent(currentP);
			} else {
				addPerson(currentP.getLeft(), newP);
			}
		} else {
			if (currentP.getRight() == null) {
				currentP.setRight(newP);
				//				newP.setPersonParent(currentP);
			} else {
				addPerson(currentP.getRight(), newP);
			}
		}
	}
	//recursively display name tree labels and draw lines
	public void displayTree(Person current, double x, double y, double xGap, double yGap) {
		if(current.getLeft() != null) {
			contentPane.getChildren().add(new Line(x, y, x-xGap, y+yGap));
			displayTree(current.getLeft(), x-xGap, y+yGap, xGap*0.5, yGap);
		}
		if(current.getRight() != null) {
			contentPane.getChildren().add(new Line(x, y, x+xGap, y+yGap));
			displayTree(current.getRight(), x+xGap, y+yGap, xGap*0.5, yGap);
		}
		current.setOnMouseClicked(event -> current.toggleText());
		current.setStyle("-fx-background-color: linear-gradient(to right, grey, green);-fx-text-fill: #f0f8ff;");
		current.setLayoutX(x-8);
		current.setLayoutY(y-8);
		String text = current.getFirstName();
		current.setText(text);
		contentPane.getChildren().addAll(current);
	}
	//recursively display initial tree labels and draw lines
	public void displayInitialTree(Person current, double x, double y, double xGap, double yGap) {
		if(current.getLeft() != null) {
			contentPane.getChildren().add(new Line(x, y, x-xGap, y+yGap));
			displayTree(current.getLeft(), x-xGap, y+yGap, xGap*0.5, yGap);
		}

		if(current.getRight() != null) {
			contentPane.getChildren().add(new Line(x, y, x+xGap, y+yGap));
			displayTree(current.getRight(), x+xGap, y+yGap, xGap*0.5, yGap);
		}
		current.setOnMouseClicked(event -> current.toggleText());
		current.setStyle("-fx-background-color: linear-gradient(to right, grey, green);-fx-text-fill: #f0f8ff;");
		current.setLayoutX(x-8);
		current.setLayoutY(y-8);
		String text = current.getInitial();
		current.setText(text);
		contentPane.getChildren().addAll(current);
	}
	//delete method using while loop
	public void delete(Person p, Person root) {
		Person parent = null;
		Person current = root;
		while (current != null) {
			if (p.getFirstName().compareTo(current.getFirstName()) < 0) {
				parent = current;
				current = current.getLeft();
			} else if (p.getFirstName().compareTo(current.getFirstName()) > 0) {
				parent = current;
				current = current.getRight();
			} else
				break;
		}

		if (current == null)
			return; // Element is not in the tree

		// Case 1: current has no left child
		if (current.getLeft() == null) {
			// Connect the parent with the right child of the current node
			if (parent == null) {
				root = current.getRight();
			} else {
				if (p.getFirstName().compareTo(parent.getFirstName()) < 0)
					parent.setLeft(current.getLeft());
				else
					parent.setRight(current.getRight());
			}
		} else {
			// Case 2: The current node has a left child
			// Locate the rightmost node in the left subtree of
			// the current node and also its parent
			Person parentOfRightMost = current;
			Person rightMost = current.getLeft();

			while (rightMost.getRight() != null) {
				parentOfRightMost = rightMost;
				rightMost = rightMost.getRight(); // Keep going to the right
			}

			// Replace the element in current by the element in rightMost
			current.setFirstName(rightMost.getFirstName());

			// Eliminate rightmost node
			if (parentOfRightMost.getRight() == rightMost)
				parentOfRightMost.setRight(rightMost.getLeft());
			else
				// Special case: parentOfRightMost == current
				parentOfRightMost.setLeft(rightMost.getLeft());
		}

	}
	//print first names by traversing tree breadth first
	public void printFirstNamesBreadthFirst(Person current,TextFlow nameFlow) {
		ArrayDeque<Person> nodesQueue = new ArrayDeque<>();
		nodesQueue.offer(current);
		while (!nodesQueue.isEmpty()) {
			Person p = nodesQueue.poll();
			Text name = new Text("\n"+p.getFirstName()+" "+p.getLastName());
			nameFlow.getChildren().add(name);
			if (p.getLeft() != null) {
				nodesQueue.offer(p.getLeft());
			}
			if (p.getRight() != null) {
				nodesQueue.offer(p.getRight());
			}
		}
	}
	//print names by traversing tree breadth first
	public void printNamesBreadthFirst(Person current,TextFlow nameFlow,String delimiter) {
		ArrayDeque<Person> nodesQueue = new ArrayDeque<>();
		nodesQueue.offer(current);
		while (!nodesQueue.isEmpty()) {
			Person p = nodesQueue.poll();
			Text name = new Text("\n"+p.getFirstName()+delimiter+p.getLastName());
			nameFlow.getChildren().add(name);
			if (p.getLeft() != null) {
				nodesQueue.offer(p.getLeft());
			}
			if (p.getRight() != null) {
				nodesQueue.offer(p.getRight());
			}
		}
	}
	//print names by traversing tree depth first(pre-order) 
	public void printNamesPreOrder(Person current,TextFlow nameFlow,String delimiter) {
		if (current != null) {
			Text name = new Text("\n"+current.getFirstName()+delimiter+current.getLastName());
			nameFlow.getChildren().add(name);
			printNamesPreOrder(current.getLeft(),nameFlow,delimiter);
			printNamesPreOrder(current.getRight(),nameFlow,delimiter);
		}
	}
	//print names by traversing tree depth first(in-order) 
	public void printNamesInOrder(Person current,TextFlow nameFlow,String delimiter) {
		if (current != null) {
			printNamesInOrder(current.getLeft(),nameFlow,delimiter);
			Text name = new Text("\n"+current.getFirstName()+delimiter+current.getLastName());
			nameFlow.getChildren().add(name);
			printNamesInOrder(current.getRight(),nameFlow,delimiter);
		}
	}
	//print names by traversing tree depth first(post-order) 
	public void printNamesPostOrder(Person current,TextFlow nameFlow,String delimiter) {
		if (current != null) {
			printNamesPostOrder(current.getLeft(),nameFlow,delimiter);
			printNamesPostOrder(current.getRight(),nameFlow,delimiter);
			Text name = new Text("\n"+current.getFirstName()+delimiter+current.getLastName());
			nameFlow.getChildren().add(name);
		}

	}

	// Find person object by name
	public Person searchByName(Person person, String name) {
		if (person != null) {
			if (person.getFirstName().equalsIgnoreCase(name)) {
				return person;
			}
			Person ans = searchByName(person.getLeft(), name);
			if (ans != null) {
				return ans;
			}
			return searchByName(person.getRight(), name);
		} else {
			return null;
		}
	}

	// Find person object by name recursively and print
	public void searchNameRecursively(Person current, String targetName) {
		if(current != null) {
			String name = current.getFirstName();
			if(name.equalsIgnoreCase(targetName)) {
				String personInfo = current.getFirstName()+" "+current.getLastName();
				listNameArea.appendText("\n"+personInfo);
			}
			if(current.getLeft() != null) {
				searchNameRecursively(current.getLeft(), targetName);
			}
			if(current.getRight() != null) {
				searchNameRecursively(current.getRight(), targetName);
			}
		}
	}

	// Find person object by name length
	public Person searchByNameLength(Person person, int nameLength) {
		if (person != null) {
			if (person.getFirstName().length() == nameLength) {
				return person;
			}
			Person ans = searchByNameLength(person.getLeft(), nameLength);
			if (ans != null) {
				return ans;
			}
			return searchByNameLength(person.getRight(), nameLength);
		} else {
			return null;
		}
	}
	// Find person object by name length recursively and print
	public void searchNameLengthRecursively(Person current, int nameLength) {
		if(current != null) {
			int length = current.getFirstName().length();
			if(length == nameLength) {
				String personInfo = current.getFirstName()+" "+current.getLastName();
				listNameArea.appendText("\n"+personInfo);
			}
			if(current.getLeft() != null) {
				searchNameLengthRecursively(current.getLeft(), nameLength);
			}
			if(current.getRight() != null) {
				searchNameLengthRecursively(current.getRight(), nameLength);
			}
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setMenuBar();
		setSearchBar();
		//menuBox for holding menuBar
		menuBox.getChildren().addAll(menuBar,searchField,nameTypeCB,searchButton,length,lengthBox,show);
		HBox.setHgrow(menuBox, Priority.ALWAYS);
		printBox.setLayoutY(60);
		printBox.setPadding(new Insets(40,0,0,20));
		//for displaying tree diagram or search bar result
		contentPane.setLayoutY(60);
		mainPane.setPrefSize(1200, 640);
		mainPane.setStyle("-fx-background-color: #e6e6fa;");
		mainPane.setTop(menuBox);
		mainPane.setLeft(printBox);
		mainPane.setCenter(contentPane);

		entry = new Group();
		entry.getChildren().addAll(mainPane);
		VBox.setVgrow(contentPane, Priority.ALWAYS);

		scene = new Scene(entry, 1200, 640);

		Image treeImage = new Image(getClass().getResourceAsStream("tree.png"));
		primaryStage.getIcons().add(treeImage);
		primaryStage.setTitle("Binary Tree Search");
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	//Set up search bar
	public void setSearchBar() {
		//buttons are disabled if data is not loaded
		searchButton.setDisable(true);
		show.setDisable(true);
		//textfield for user input
		searchField.setPromptText("Enter a name");
		//combobox that returns name type(first name or last name)
		nameTypeCB.setPromptText("Please select name type to start");
		nameTypeCB.getItems().addAll("First Name","Last Name");
		//search button that triggers search name action
		ImageView searchView = new ImageView(new Image(getClass().getResourceAsStream("search.png")));
		searchView.setFitWidth(16);
		searchView.setFitHeight(16);
		searchButton.setGraphic(searchView);
		searchButton.setOnAction(e -> {
			contentPane.getChildren().clear();
			String input = searchField.getText();
//			String result = null;
			if(nameTypeCB.getValue().equalsIgnoreCase("First Name")) {
				contentPane.getChildren().clear();
				listNameArea.clear();
				searchNameRecursively(firstNameRoot, input);
				if(searchByName(firstNameRoot, input)==null) {
					listNameArea.appendText("No data found");
				}
			}else {
				contentPane.getChildren().clear();
				listNameArea.clear();
				searchNameRecursively(lastNameRoot, input);
				if(searchByName(lastNameRoot, input)==null) {
					listNameArea.appendText("No data found");
				}
			}
			listNameArea.setLayoutX(200);
			listNameArea.setLayoutY(100);
			contentPane.getChildren().add(listNameArea);
		});

		lengthBox.setPromptText("Select/input length of name");
		lengthBox.getItems().addAll("2","3","4","5","6","7","8","9","10");
		lengthBox.setEditable(true);
		//show button that triggers name length search
		ImageView showView = new ImageView(new Image(getClass().getResourceAsStream("show.png")));
		showView.setFitWidth(16);
		showView.setFitHeight(16);
		show.setGraphic(showView);
		show.setOnAction(e -> {
			contentPane.getChildren().clear();
			searchField.clear();
			listNameArea.clear();
			if(nameTypeCB.getValue().equals("First Name")) {
				int targetLength = Integer.parseInt(lengthBox.getValue());
				listNameArea.appendText("\n Person(s) that has first name length of "+targetLength+ " : ");
				searchNameLengthRecursively(firstNameRoot,targetLength);
				if(searchByNameLength(firstNameRoot, targetLength)==null) {
					listNameArea.appendText("No data found");
				}
			}else {
				int targetLength = Integer.parseInt(lengthBox.getValue());
				listNameArea.appendText("\n Person(s) that has last name length of "+targetLength+ " : ");
				searchNameLengthRecursively(lastNameRoot,targetLength);
				if(searchByNameLength(lastNameRoot, targetLength)==null) {
					listNameArea.appendText("No data found");
				}
			}

			listNameArea.setLayoutX(200);
			listNameArea.setLayoutY(100);
			contentPane.getChildren().add(listNameArea);
		});
	}
	
	//Set up menuBar
	public void setMenuBar() {
		//Load menu for loading data from file
		load.setDisable(false);
		ImageView loadView = new ImageView(new Image(getClass().getResourceAsStream("load.png")));
		loadView.setFitWidth(16);
		loadView.setFitHeight(16);
		load.setGraphic(loadView);
		//Print according to first name and last name in depth/breadth first order
		ImageView printView = new ImageView(new Image(getClass().getResourceAsStream("print.png")));
		printView.setFitWidth(16);
		printView.setFitHeight(16);
		print.setGraphic(printView);
		ImageView displayView = new ImageView(new Image(getClass().getResourceAsStream("display.png")));
		displayView.setFitWidth(16);
		displayView.setFitHeight(16);
		display.setGraphic(displayView);
		ImageView initialiseView = new ImageView(new Image(getClass().getResourceAsStream("initialise.png")));
		initialiseView.setFitWidth(16);
		initialiseView.setFitHeight(16);
		initialise.setGraphic(initialiseView);
		menuBar.getMenus().addAll(load,print,display,initialise);
		MenuItem loadData = new MenuItem("Load Data");
		load.getItems().add(loadData);
		loadData.setOnAction(e -> {
			loadNameData();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information");
			alert.setHeaderText(null);
			alert.setContentText("File loaded successfully!");
			alert.showAndWait();
		});
		//Depth First(in-order, pre-order, post-order) and Breadth First Traversal printing for first name
		MenuItem printFirstNameInOrder = new MenuItem("Depth First In-Order");
		MenuItem printFirstNamePreOrder = new MenuItem("Depth First Pre-Order");
		MenuItem printFirstNamePostOrder = new MenuItem("Depth First Post-Order");
		Menu printFirstNameDepththFirst = new Menu("Depth First");
		printFirstNameDepththFirst.getItems().addAll(printFirstNameInOrder,printFirstNamePreOrder,printFirstNamePostOrder);
		MenuItem printFirstNameBreadthFirst = new MenuItem("Breadth First");
		
		printFirstNameInOrder.setOnAction(e -> {
			if(dataLoaded()) {
				printBox.getChildren().clear();
				printBox.setStyle("-fx-background-color: #fafad2;");
				fDepInFlow = new TextFlow();
				orderTypeLabel.setText("Depth First In-Order : First Name");
				printNamesInOrder(firstNameRoot, fDepInFlow, " ");
				printBox.getChildren().addAll(orderTypeLabel,fDepInFlow);
			}else {
				dataLoadingWarning();
			}
		});
		printFirstNamePreOrder.setOnAction(e -> {
			if(dataLoaded()) {
				printBox.getChildren().clear();
				printBox.setStyle("-fx-background-color: #fafad2;");
				fDepPreFlow = new TextFlow();
				orderTypeLabel.setText("Depth First Pre-Order : First Name");
				printNamesPreOrder(firstNameRoot, fDepPreFlow, " ");
				printBox.getChildren().addAll(orderTypeLabel, fDepPreFlow);
			}else {
				dataLoadingWarning();
			}
		});
		printFirstNamePostOrder.setOnAction(e -> {
			if(dataLoaded()) {
				printBox.getChildren().clear();
				printBox.setStyle("-fx-background-color: #fafad2;");
				fDepPostFlow = new TextFlow();
				orderTypeLabel.setText("Depth First Post-Order : First Name");
				printNamesPostOrder(firstNameRoot, fDepPostFlow, " ");
				printBox.getChildren().addAll(orderTypeLabel, fDepPostFlow);
			}else {
				dataLoadingWarning();
			}
		});
		printFirstNameBreadthFirst.setOnAction(e -> {
			if(dataLoaded()) {
				printBox.getChildren().clear();
				printBox.setStyle("-fx-background-color: #fafad2;");
				fBreFlow = new TextFlow();
				orderTypeLabel.setText("Breadth First : First Name");
				printNamesBreadthFirst(firstNameRoot,fBreFlow," ");
				printBox.getChildren().addAll(orderTypeLabel, fBreFlow);
			}else {
				dataLoadingWarning();
			}
		});
		//Depth First(in-order, pre-order, post-order) and Breadth First Traversal printing for last name
		MenuItem printLastNameInOrder = new MenuItem("Depth First In-Order");
		MenuItem printLastNamePreOrder = new MenuItem("Depth First Pre-Order");
		MenuItem printLastNamePostOrder = new MenuItem("Depth First Post-Order");
		Menu printLastNameDepththFirst = new Menu("Depth First");
		printLastNameDepththFirst.getItems().addAll(printLastNameInOrder,printLastNamePreOrder,printLastNamePostOrder);
		MenuItem printLastNameBreadthFirst = new MenuItem("Breadth First");
		printLastNameInOrder.setOnAction(e -> {
			if(dataLoaded()) {
				printBox.getChildren().clear();
				printBox.setStyle("-fx-background-color: #fafad2;");
				lDepInFlow = new TextFlow();
				orderTypeLabel.setText("Depth First In-Order : Last Name");
				printNamesInOrder(lastNameRoot, lDepInFlow, ", ");
				printBox.getChildren().addAll(orderTypeLabel, lDepInFlow);
			}else {
				dataLoadingWarning();
			}
		});
		printLastNamePreOrder.setOnAction(e -> {
			if(dataLoaded()) {
				printBox.getChildren().clear();
				printBox.setStyle("-fx-background-color: #fafad2;");
				lDepPreFlow = new TextFlow();
				orderTypeLabel.setText("Depth First Pre-Order : Last Name");
				printNamesPreOrder(lastNameRoot, lDepPreFlow, ", ");
				printBox.getChildren().addAll(orderTypeLabel, lDepPreFlow);
			}else {
				dataLoadingWarning();
			}
		});
		printLastNamePostOrder.setOnAction(e -> {
			if(dataLoaded()) {
				printBox.getChildren().clear();
				printBox.setStyle("-fx-background-color: #fafad2;");
				lDepPostFlow = new TextFlow();
				orderTypeLabel.setText("Depth First Post-Order : Last Name");
				printNamesPostOrder(lastNameRoot, lDepPostFlow, ", ");
				printBox.getChildren().addAll(orderTypeLabel, lDepPostFlow);
			}else {
				dataLoadingWarning();
			}
		});
		printLastNameBreadthFirst.setOnAction(e -> {
			if(dataLoaded()) {
				printBox.getChildren().clear();
				printBox.setStyle("-fx-background-color: #fafad2;");
				lBreFlow = new TextFlow();
				orderTypeLabel.setText("Breadth First : Last Name");
				printNamesBreadthFirst(lastNameRoot,lBreFlow,", ");
				printBox.getChildren().addAll(orderTypeLabel, lBreFlow);
			}else {
				dataLoadingWarning();
			}
		});
		Menu byFirstName = new Menu("By First Name");
		byFirstName.getItems().addAll(printFirstNameDepththFirst,printFirstNameBreadthFirst);
		Menu byLastName = new Menu("By Last Name");
		byLastName.getItems().addAll(printLastNameDepththFirst,printLastNameBreadthFirst);
		print.getItems().addAll(byFirstName,byLastName);
		//initialise screen menu for erasing screen
		MenuItem initialiseScreen = new MenuItem("Initialise Screen");
		initialise.getItems().add(initialiseScreen);
		initialiseScreen.setOnAction(e -> initialise());
		//Display tree diagram according to first Name, last name or initial
		MenuItem displayFirstNameTree = new MenuItem("Display Tree by First Name");
		MenuItem displayLastNameTree = new MenuItem("Display Tree by Last Name");
		display.getItems().addAll(displayFirstNameTree,displayLastNameTree);
		displayFirstNameTree.setOnAction(e -> {
			if(dataLoaded()) {
				contentPane.getChildren().clear();
				displayTree(firstNameRoot, 500, 80, 180, 50);
			}else {
				dataLoadingWarning();
			}
		});
		displayLastNameTree.setOnAction(e -> {
			if(dataLoaded()) {
				contentPane.getChildren().clear();
				displayTree(lastNameRoot, 500, 80, 180, 50);
			}else {
				dataLoadingWarning();
			}
		});
	}
	
	//Pop warning if printing or displaying is done without data loaded
	public void dataLoadingWarning() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("No Data");
		alert.setHeaderText(null);
		alert.setContentText("Please load data first!");
		alert.showAndWait();
	}
	
	//Check if data is load
	public boolean dataLoaded() {
		if(firstNameRoot == null || lastNameRoot == null) {
			return false;
		}return true;
	}

	public static void main(String[] args) {
		new TreeSearchMain();
		launch();
	}

}
