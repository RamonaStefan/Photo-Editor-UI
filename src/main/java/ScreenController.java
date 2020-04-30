import java.awt.color.ProfileDataException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.bmp.BmpMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.sun.media.jfxmedia.MetadataParser;

import javafx.application.Application.Parameters;
import javafx.application.Platform;
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
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.control.SplitPane;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class ScreenController extends DiffImages implements Initializable {
//	ScreensController myController;
	private Scene secondScene, thirdScene, fourthScene, fifthScene, sixthScene;
	@FXML
	private TableView<ImageInfo> tableInfo;
	@FXML
	private ImageView imageChosen;
	@FXML
	private ImageView imageChosen2;
	@FXML
	private ToggleButton start;
	@FXML
	private TableColumn<ImageInfo, String> columnName;
	@FXML
	private TableColumn<ImageInfo, String> columnValue;
	@FXML
	private ProgressBar showProgress;
	@FXML
	private ToggleButton go;
	@FXML
	private RadioButton increaseContrast;
	@FXML
	private RadioButton decreaseContrast;
	@FXML
	private ChoiceBox<String> choiceBox;
	@FXML
	private ToggleButton next;
	private TabPane tabPane;
	private Tab tab1;
	private Tab tab2;
	@FXML
	private Tab tab3;
	@FXML
	private CheckBox showBeforeAfter;
	@FXML
	private Label chooseValue;
	@FXML
	private ToggleButton save;
	@FXML
	private ScrollBar scrollBar;
	@FXML
	private AnchorPane scrollPane;
	@FXML
	private ListView<String> previewList;
	@FXML
	private ProgressIndicator progressIndicator;
	static String argsLine[];
	private static double choiceBoxValue = 1;
	private static boolean checkBoxValue;
	private Stage stage;
	ListView<String> list = new ListView<String>();

	static double ii = 0;
	ObservableList<String> items = FXCollections.observableArrayList();

	public ScreenController() throws IOException {
	}

	@FXML
	void startApp(ActionEvent event) throws IOException {
		VBox secondScreen = FXMLLoader.load(getClass().getResource("SecondScreen.fxml"));
		secondScene = new Scene(secondScreen);
		Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stageTheEventSourceNodeBelongs.setScene(secondScene);

	}

	@FXML
	void modifyContrast(ActionEvent event) throws IOException {
		VBox thirdScreen = FXMLLoader.load(getClass().getResource("ThirdScreen.fxml"));
		Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
		thirdScene = new Scene(thirdScreen);
		stageTheEventSourceNodeBelongs.setScene(thirdScene);
	}

	@FXML
	void setListIncrease() {
		choiceBox.setItems(FXCollections.observableArrayList("2", "4", "6", "8"));
		decreaseContrast.setSelected(false);
		choiceBox.setDisable(false);
		showBeforeAfter.setDisable(false);
		go.setDisable(false);
		chooseValue.setDisable(false);
	}

	@FXML
	void setListDecrease() {
		choiceBox.setItems(FXCollections.observableArrayList("0.2", "0.4", "0.6", "0.8"));
		increaseContrast.setSelected(false);
		choiceBox.setDisable(false);
		showBeforeAfter.setDisable(false);
		go.setDisable(false);
		chooseValue.setDisable(false);
	}

	@FXML
	void goToTimer(ActionEvent event) throws IOException {
		choiceBoxValue = Double.parseDouble(choiceBox.getValue());
		if (showBeforeAfter.isSelected()) {
			checkBoxValue = true;
		} else {
			checkBoxValue = false;
		}
		VBox fourthScreen = (VBox) FXMLLoader.load(getClass().getResource("FourthScreen.fxml"));
		fourthScene = new Scene(fourthScreen);
		Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stageTheEventSourceNodeBelongs.setScene(fourthScene);
	}

	@FXML
	void goToResults(ActionEvent event) throws IOException {
		Process(choiceBoxValue);
		StackPane root = new StackPane();
		Scene seventhScene = new Scene(root);

		tab1 = new Tab("After");
		tab2 = new Tab("Before");
		tab3 = new Tab("More");
		tabPane = new TabPane();

		ImageView after = new ImageView(new Image(getOutputFile().toURI().toString()));
		ImageView before = new ImageView(new Image(getDefaultImage().toURI().toString()));
		ImageView iv = new ImageView(new Image("istockphoto-680890628-170667a.jpg"));
		iv.fitWidthProperty().bind(tabPane.widthProperty());
		iv.fitHeightProperty().bind(tabPane.heightProperty());

		AnchorPane anchorPane = new AnchorPane();

		Hyperlink about = new Hyperlink("Find out how we processed your image");
		about.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				getHostServices().showDocument(getClass().getResource("about.html").toString());
			}
		});
		about.setTextFill(Color.web("#FFFFFF"));
		about.setStyle("-fx-border-color: white;");

		Label impressions = new Label("Tell us your impression about our app :)");
		impressions.setStyle("-fx-font-weight: bold");
		impressions.setTextFill(Color.web("#FFFFFF"));
		final TextField text = new TextField();

		ToggleButton sendText = new ToggleButton("Send your feedback");
		sendText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (!text.getText().equals("")) {
					File file = new File("src/main/resources/impressions.txt");
					FileReader fr;
					try {
						FileWriter writer2 = new FileWriter("src/main/resources/impressions2.txt");
						fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
						String line;
						writer2.append(text.getText());
						writer2.append(System.getProperty("line.separator"));
						while ((line = br.readLine()) != null) {
							writer2.append(line);
							writer2.append(System.getProperty("line.separator"));
						}
						fr.close();
						writer2.flush();
						writer2.close();

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
					list.getItems().add(0, text.getText());
					text.setText("");
				}
			}

		});

		Label reviews = new Label("OUR LAST REVIEWS");
		reviews.setTextFill(Color.web("#FFFFFF"));
		reviews.setStyle("-fx-font-weight: bold");
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
		list.setMouseTransparent(true);
		list.setFocusTraversable(false);

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

		tab1.setContent(after);
		tab2.setContent(before);
		tab3.setContent(anchorPane);

		tabPane.getTabs().add(tab1);
		if (checkBoxValue) {
			tabPane.getTabs().add(tab2);
		}
		tabPane.getTabs().add(tab3);
		root.getChildren().add(tabPane);

		Stage stageTheEventSourceNodeBelongs = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stageTheEventSourceNodeBelongs.setScene(seventhScene);
	}

	void addText() {

	}

	@FXML
	void showInfo(ActionEvent event) throws IOException {
		VBox fifthScreen = FXMLLoader.load(getClass().getResource("FifthScreen.fxml"));
		fifthScene = new Scene(fifthScreen);
		Stage newStage = new Stage();
		newStage.setScene(fifthScene);
		newStage.show();
	}

	@FXML
	void showImage() {
		imageChosen.setImage(new Image(getDefaultImage().toURI().toString()));
	}

	@Override
	public void initialize(java.net.URL arg0, ResourceBundle arg1) {
		setDefaultImageProcessing(argsLine[0]);
		setDefaultImage(new File(argsLine[1]));
		setOutputFile(new File(argsLine[2]));
		if (imageChosen2 != null) {
			imageChosen2.setImage(new Image(getDefaultImage().toURI().toString()));
		}
		if (showProgress != null) {
			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() {
					for (int i = 1; i <= 10; i++) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						updateProgress(i, 10);
					}
					next.setDisable(false);
					return null;
				}
			};

			showProgress.progressProperty().bind(task.progressProperty());
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		if (progressIndicator != null) {
			Task<Void> task = new Task<Void>() {
				@Override
				public Void call() {
					for (int i = 1; i <= 10; i++) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						updateProgress(i, 10);
					}
					if (tableInfo != null ) {
						Metadata metadata = null;
						try {
							metadata = BmpMetadataReader.readMetadata(getDefaultImage());
						} catch (IOException e) {
							e.printStackTrace();
						}

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
			Thread th = new Thread(task);
			th.setDaemon(true);
			th.start();
		}
		if (scrollBar != null) {
			scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
				public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
					scrollPane.setLayoutY(-new_val.doubleValue());
				}
			});
		}
		 if(previewList != null) {
			File folder = new File("src/main/resources/bmp-preview");
			File[] listOfFiles = folder.listFiles();
			List<String>  imageViews = new ArrayList<String>();
			for (int i = 0; i < listOfFiles.length; i++) {
				ImageView img = new ImageView(new Image(new File(
						("src/main/resources/bmp-preview/" + listOfFiles[i].getName()))
						.toURI().toString()));
				imageViews.add((new File(
						("src/main/resources/bmp-preview/" + listOfFiles[i].getName()))
						.toURI().toString()));
			}
			ObservableList<String> listImages = FXCollections.observableArrayList(imageViews);		
		
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
			previewList.setItems(listImages);
		
		}
	}

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("FirstScreen.fxml"));

			Scene scene = new Scene(root);

			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// main method
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
		launch(args);
	}

}
