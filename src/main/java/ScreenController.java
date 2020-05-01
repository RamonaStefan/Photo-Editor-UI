import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.drew.imaging.bmp.BmpMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ScreenController extends DiffImages implements Initializable {
	// User interface elements
	@FXML
	private TableView<ImageInfo> tableInfo;
	@FXML
	private ImageView imageChosen, imageChosenUsed;
	@FXML
	private ToggleButton start, go, next;
	@FXML
	private TableColumn<ImageInfo, String> columnName, columnValue;
	@FXML
	private ProgressBar showProgress;
	@FXML
	private RadioButton increaseContrast, decreaseContrast;
	@FXML
	private ChoiceBox<String> choiceBox;
	@FXML
	private TabPane tabPane;
	@FXML
	private Tab tab1, tab2, tab3;
	@FXML
	private CheckBox showBeforeAfter;
	@FXML
	private Label chooseValue;
	@FXML
	private ScrollBar scrollBar;
	@FXML
	private AnchorPane scrollPane;
	@FXML
	private ListView<String> previewList;
	@FXML
	private ProgressIndicator progressIndicator;
	
	private static String argsLine[];											// array to store command line arguments
	private static double choiceBoxValue = 1;									// variable to store user's contrast choice
	private static boolean checkBoxValue;										// variable to decide if before picture is displayed
	private static double ii = 0;												// variable used to increase progress-bar/progress-indicator
	private ListView<String> list = new ListView<String>();						// listview with images
	private Scene secondScene, thirdScene, fourthScene, fifthScene;				// scenes of application
	private ObservableList<String> items = FXCollections.observableArrayList();	// list of images locations for listview

	// Constructor without parameters
	public ScreenController() throws IOException {
		super();
	}
	

	@FXML
	/**
	 * Method used to redirect from stage 1 to stage 2
	 * 
	 * @param event ActionEvent that caused the method to be called
	 * @throws IOException
	 */
	void startApp(ActionEvent event) throws IOException {
		VBox secondScreen = FXMLLoader.load(getClass().getResource("SecondScreen.fxml"));
		secondScene = new Scene(secondScreen);
		Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stageTheEventSourceNodeBelongs.setScene(secondScene);

	}

	@FXML
	/**
	 * Method used to redirect from stage 2 to stage 3
	 * 
	 * @param event ActionEvent that caused the method to be called
	 * @throws IOException
	 */
	void modifyContrast(ActionEvent event) throws IOException {
		VBox thirdScreen = FXMLLoader.load(getClass().getResource("ThirdScreen.fxml"));
		Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
		thirdScene = new Scene(thirdScreen);
		stageTheEventSourceNodeBelongs.setScene(thirdScene);
	}

	@FXML
	/**
	 * Method used to set choice-box values, display choice-box, go-button, check-box and label 
	 * in case of increasing contrast and uncheck decrease-contrast radio-button
	 */
	void setListIncrease() {
		// set choice-box values
		choiceBox.setItems(FXCollections.observableArrayList("2", "4", "6", "8"));
		// uncheck decrease-contrast radio button
		decreaseContrast.setSelected(false);
		// display choice-box
		choiceBox.setDisable(false);
		// display check-box
		showBeforeAfter.setDisable(false);
		// display button
		go.setDisable(false);
		// display label
		chooseValue.setDisable(false);
	}

	@FXML
	/**
	 * Method used to set choice-box values, display choice-box, go-button, check-box and label 
	 * in case of decreasing contrast and uncheck increase-contrast radio-button
	 */
	void setListDecrease() {
		// set choice-box values
		choiceBox.setItems(FXCollections.observableArrayList("0.2", "0.4", "0.6", "0.8"));
		// uncheck increase-contrast radio button
		increaseContrast.setSelected(false);
		// display choice-box
		choiceBox.setDisable(false);
		// display check-box
		showBeforeAfter.setDisable(false);
		// display button
		go.setDisable(false);
		// display label
		chooseValue.setDisable(false);
	}

	@FXML
	/**
	 * Method used to redirect from stage 3 to stage 4 and save choice-box value, check-box value
	 * @param event ActionEvent that caused the method to be called
	 * @throws IOException
	 */
	void goToTimer(ActionEvent event) throws IOException {
		// save choice-box value
		choiceBoxValue = Double.parseDouble(choiceBox.getValue());
		// save check-box value
		if (showBeforeAfter.isSelected()) {
			checkBoxValue = true;
		} else {
			checkBoxValue = false;
		}
		// redirect 3 -> 4
		VBox fourthScreen = (VBox) FXMLLoader.load(getClass().getResource("FourthScreen.fxml"));
		fourthScene = new Scene(fourthScreen);
		Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stageTheEventSourceNodeBelongs.setScene(fourthScene);
	}

	@FXML
	/**
	 * Method used to build stage 6
	 * @param event ActionEvent that caused the method to be called
	 * @throws IOException
	 */
	void goToResults(ActionEvent event) throws IOException {
		// call modify contrast method
		Process(choiceBoxValue);
		// create stage 7
		StackPane root = new StackPane();
		Scene sixthScene = new Scene(root);

		// create TabPane(Tab, Tab, Tab) elements
		tab1 = new Tab("After");
		tab2 = new Tab("Before");
		tab3 = new Tab("More");
		tabPane = new TabPane();

		// create and style ImageView elements
		ImageView after = new ImageView(new Image(getOutputFile().toURI().toString()));
		ImageView before = new ImageView(new Image(getDefaultImage().toURI().toString()));
		ImageView iv = new ImageView(new Image("istockphoto-680890628-170667a.jpg"));
		iv.fitWidthProperty().bind(tabPane.widthProperty());
		iv.fitHeightProperty().bind(tabPane.heightProperty());

		// create AnchorPane
		AnchorPane anchorPane = new AnchorPane();

		// create hyperlink, set action on click and style
		Hyperlink about = new Hyperlink("Find out how we processed your image");
		about.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				getHostServices().showDocument(getClass().getResource("about.html").toString());
			}
		});
		about.setTextFill(Color.web("#FFFFFF"));
		about.setStyle("-fx-border-color: white;");

		// add and style label, add text-field for users to write impressions
		Label impressions = new Label("Tell us your impression about our app :)");
		impressions.setStyle("-fx-font-weight: bold");
		impressions.setTextFill(Color.web("#FFFFFF"));
		final TextField text = new TextField();
		
		// create button to send impression and set action on click
		ToggleButton sendText = new ToggleButton("Send your feedback");
		sendText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!text.getText().equals("")) {
					// add new impression at the beginning of the impressions text file 
					File file = new File("src/main/resources/impressions.txt");
					FileReader fr;
					try {
						FileWriter writer2 = new FileWriter("src/main/resources/impressions2.txt");
						fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
						String line;
						writer2.append(text.getText());
						writer2.append(System.getProperty("line.separator"));
						// write in an auxiliary text file
						while ((line = br.readLine()) != null) {
							writer2.append(line);
							writer2.append(System.getProperty("line.separator"));
						}
						fr.close();
						writer2.flush();
						writer2.close();

						// write in the impressions text file from the auxiliary file
						FileWriter writer = new FileWriter("src/main/resources/impressions.txt");
						file = new File("src/main/resources/impressions2.txt");
						fr = new FileReader(file);
						br = new BufferedReader(fr);
						while ((line = br.readLine()) != null) {
							writer.append(line);
							writer.append(System.getProperty("line.separator"));
						}
						writer.flush();
						writer.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					// add impression in list-view
					list.getItems().add(0, text.getText());
					// remove text from text-field
					text.setText("");
				}
			}

		});
		
		// add and style label
		Label reviews = new Label("OUR LAST REVIEWS");
		reviews.setTextFill(Color.web("#FFFFFF"));
		reviews.setStyle("-fx-font-weight: bold");
		
		// add impressions to list-view from impressions text file
		File file = new File("src/main/resources/impressions.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		fr.close();
		items.addAll(Arrays.asList(sb.toString().split("\n")));
		List<String> myList = new ArrayList<String>(Arrays.asList(sb.toString().split("\n")));
		list.setItems(items);
		// make list non-editable
		list.setMouseTransparent(true);
		list.setFocusTraversable(false);

		// create restrictions for elements positions
		anchorPane.getChildren().addAll(iv, about, impressions, text, sendText, reviews, list);
		anchorPane.setBottomAnchor(about, 50.0);
		anchorPane.setLeftAnchor(about, 10.0);
		anchorPane.setTopAnchor(impressions, 30.0);
		anchorPane.setLeftAnchor(impressions, 10.0);
		anchorPane.setTopAnchor(text, 70.0);
		anchorPane.setLeftAnchor(text, 10.0);
		anchorPane.setTopAnchor(sendText, 120.0);
		anchorPane.setLeftAnchor(sendText, 10.0);
		anchorPane.setTopAnchor(reviews, 10.0);
		anchorPane.setRightAnchor(reviews, 60.0);
		anchorPane.setTopAnchor(list, 35.0);
		anchorPane.setRightAnchor(list, 15.0);
		anchorPane.setBottomAnchor(list, 50.0);

		// set content for tabs
		tab1.setContent(after);
		tab2.setContent(before);
		tab3.setContent(anchorPane);

		// add tabs to tab-pane
		tabPane.getTabs().add(tab1);
		if (checkBoxValue) {
			tabPane.getTabs().add(tab2);
		}
		tabPane.getTabs().add(tab3);
		
		// add tab-pane to stage
		root.getChildren().add(tabPane);

		// display the stage created
		Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stageTheEventSourceNodeBelongs.setScene(sixthScene);
	}

	@FXML
	/**
	 * Method used to display new stage 
	 * 
	 * @param event ActionEvent that caused the method to be called
	 * @throws IOException
	 */
	void showInfo(ActionEvent event) throws IOException {
		VBox fifthScreen = FXMLLoader.load(getClass().getResource("FifthScreen.fxml"));
		fifthScene = new Scene(fifthScreen);
		Stage newStage = new Stage();
		newStage.setScene(fifthScene);
		newStage.show();
	}

	@FXML
	/**
	 * Method used to display image chosen
	 */
	void showImage() {
		imageChosen.setImage(new Image(getDefaultImage().toURI().toString()));
	}

	@Override
	/**
	 * Method used to initialise stages
	 */
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		// set image-processing-method, default-file and output-file
		setDefaultImageProcessing(argsLine[0]);
		setDefaultImage(new File(argsLine[1]));
		setOutputFile(new File(argsLine[2]));
		
		// display image-chosen in the stages that include it
		if (imageChosenUsed != null) {
			imageChosenUsed.setImage(new Image(getDefaultImage().toURI().toString()));
		}
		// set functionality for progress bar
		if (showProgress != null) {
			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() {
					// increase progress for progress-bar
					for (int i = 1; i <= 10; i++) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						updateProgress(i, 10);
					}
					// display next-button
					next.setDisable(false);
					return null;
				}
			};
			showProgress.progressProperty().bind(task.progressProperty());
			// start new thread
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		// set functionality for progress indicator
		if (progressIndicator != null) {
			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() {
					// increase progress for progress-indicator
					for (int i = 1; i <= 10; i++) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						updateProgress(i, 10);
					}
					// display file information in table-view
					if (tableInfo != null ) {
						// read metadata of file
						Metadata metadata = null;
						try {
							metadata = BmpMetadataReader.readMetadata(getDefaultImage());
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						// add elements in table-view
						columnName.setCellValueFactory(new PropertyValueFactory<ImageInfo, String>("name"));
						columnValue.setCellValueFactory(new PropertyValueFactory<ImageInfo, String>("value"));
						for (Directory directory : metadata.getDirectories()) {
							for (Tag tag : directory.getTags()) {
								ImageInfo value = new ImageInfo(tag.getTagName(), tag.getDescription());
								tableInfo.getItems().add(value);
							}
						}
					}
					return null;
				}
			};

			progressIndicator.progressProperty().bind(task.progressProperty());
			// start new thread
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		// set functionality for scroll-bar
		if (scrollBar != null) {
			scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					scrollPane.setLayoutY(-new_val.doubleValue());
				}
			});
		}
		// create  horizontal-list=view with images
		 if(previewList != null) {
			File folder = new File("src/main/resources/bmp-preview");
			// get all images from preview-directory
			File[] listOfFiles = folder.listFiles();
			List<String>  imageViews = new ArrayList<String>();
			for (int i = 0; i < listOfFiles.length; i++) {
				// add location of images to list-view
				ImageView img = new ImageView(new Image(new File(
						("src/main/resources/bmp-preview/" + listOfFiles[i].getName()))
						.toURI().toString()));
				imageViews.add((new File(
						("src/main/resources/bmp-preview/" + listOfFiles[i].getName()))
						.toURI().toString()));
			}
			ObservableList<String> listImages = FXCollections.observableArrayList(imageViews);		
		
			// set functionality for list-view to display graphic (the image) and not text
			previewList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
				@Override
				public ListCell<String> call(ListView<String> param) {
					ListCell<String> cell = new ListCell<String>() {
						@Override
						public void updateItem(final String item, final boolean empty) {
							super.updateItem(item, empty);
							if (item == null) {
						        setText(null);
						        setGraphic(null);
						        return;
						    }
			                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
						    setText(null);
						    final HBox hbox = new HBox();
						    Label iconLabel = new Label();
						    ImageView iv = new ImageView(new Image(item));
						    iv.setFitHeight(previewList.getHeight()/1.08);
						    iv.setFitWidth(previewList.getWidth()/4.5);
						    iconLabel.setGraphic(iv);
						    
						    hbox.getChildren().addAll(iconLabel);
						    setGraphic(hbox);
						}
					};
					
					return cell;
				}
			});
			// add elements to list-view
			previewList.setItems(listImages);
		
		}
	}

	@Override
	/**
	 * Method to set stage 1
	 */
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FirstScreen.fxml"));

			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main method
	 * @param args command line arguments (image-processing-method, source-filename, output-filename)
	 */
	public static void main(String[] args) {
		DiffImages obj = new DiffImages(); // Instantiate an object of this class
		long startTime = System.nanoTime(); // Time at the beginning of a phase
		long stopTime; // Time at the end of a phase

		// Get names for the image processing program and the image file to be processed
		if (args.length == 1) {
			setDefaultImageProcessing(args[0]);
			// Use default image file
		} else if (args.length == 2) {
			setDefaultImageProcessing(args[0]);
			setDefaultImage(new File(args[1]));
		} else if (args.length == 3) {
			setDefaultImageProcessing(args[0]);
			setDefaultImage(new File(args[1]));
			setOutputFile(new File(args[2]));
		} else {
			System.out.println("Invalid args");
			System.exit(1);
		}

		argsLine = new String[3];
		argsLine = args;

		stopTime = System.nanoTime();
		// Calculate and display source file reading time plus destination file reading
		// time
		System.out.print("Source file and destination file reading: ");
		System.out.println(calculateTime(startTime, stopTime) + " seconds");
		// start ui app
		launch(args);
	}

}
