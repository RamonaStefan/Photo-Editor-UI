import java.io.*;
import javax.imageio.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.Graphics;
import java.awt.image.*;

/**
 * This program reads an image file from the disk and saves it in memory. Then
 * the program instantiates an object of type DataBufferInt, and invokes the
 * getData method to cause the pixels in the originalImage to be extracted into
 * int values and stored in the array named pixels1D. Then the program copies
 * the pixel values from the pixels1D array into the pixels3D array of type:
 * int[row][column][depth]. The first two dimensions of the array correspond to
 * the rows and noColumns of pixels in the image. The third dimension always has
 * a value of 3 and contains the following values by index value: 0 red, 1
 * green, 2 blue. The pixels3D array is passed to an image processing program.
 * In the end, the image processing program returns a modified version of the 3D
 * array of pixel data. Typical usage is as follows: java DiffImage ContrastMod
 * ImageSourcePathAndFileName ImageDestinationPathAndFileName If the
 * ImageSourcePathAndFileName is not specified on the command line, the program
 * will search for an image file in the current directory named tiger.bmp and
 * will process it using the processing program specified by the second
 * command-line argument. If the command-line arguments are omitted, the program
 * will throw an error.
 */

public class DiffImages extends PixelImage {
	private static String defaultImageProcessing; // Image processing program
	private static File outputFile; // Destination file
	private BufferedImage buffImage; // New image
	private int[][][] newPixels3D; // 3D pixels array of data of the new image
	private ImageInterface imageProcessingObject; // Reference to the image processing object


	// Constructors with parameters
	public DiffImages(BufferedImage buffImage, String defaultImageProcessing, File outputFile, int[][][] newPixels3D,
			ImageInterface imageProcessingObject, BufferedImage originalImage, File defaultImage, int noColumns,
			int noRows, int[][][] pixels3d, int[] pixels1d) {
		super(originalImage, defaultImage, noColumns, noRows, pixels3d, pixels1d);
		setDefaultImageProcessing(defaultImageProcessing);
		setOutputFile(outputFile);
		setBuffImage(buffImage);
		setNewPixels3D(newPixels3D);
		setImageProcessingObject(imageProcessingObject);
	}

	public DiffImages(BufferedImage buffImage, String defaultImageProcessing, File outputFile, int[][][] newPixels3D,
			ImageInterface imageProcessingObject) {
		super();
		setDefaultImageProcessing(defaultImageProcessing);
		setOutputFile(outputFile);
		setBuffImage(buffImage);
		setNewPixels3D(newPixels3D);
		setImageProcessingObject(imageProcessingObject);
	}

	// Constructor without parameters
	public DiffImages() {
		super();
		setOutputFile(new File("out/backupCopy.bmp"));
		setDefaultImageProcessing("ContrastMod");
	}



	/**
	 * Method to calculate and return the duration of a phase
	 *
	 * @param startTime time at the beginning of a phase
	 * @param stopTime  time at the end of a phase
	 * @return duration of a phase by calculating it and converting it from
	 *         nanoseconds to seconds
	 */
	protected static double calculateTime(long startTime, long stopTime) {
		return ((stopTime - startTime) * Math.pow(10, -9));
	}

	/**
	 * Method to calculate and return how much time has passed since a certain
	 * moment
	 *
	 * @param startTime time at the beginning of a phase
	 * @return time passed since startTime in seconds
	 */
	protected static double calculateTime(long startTime) {
		return (System.nanoTime() - startTime) * Math.pow(10, -9);
	}

	/**
	 * Method to process an image and pass it to an image contrast modification
	 * program.
	 */
	protected void Process(double contrast ) {
		long startTime; // Time at the beginning of a phase
		long stopTime; // Time at the end of a phase

		startTime = System.nanoTime();
		try {
			// Read image
			setOriginalImage(ImageIO.read(getDefaultImage()));
		} catch (IOException e) {
			System.out.println("Could not find any photo with that name.");
			System.err.println(e.getMessage());
		}
		stopTime = System.nanoTime();
		// Calculate and display the original image reading time
		System.out.print("Original image reading: ");
		System.out.println(calculateTime(startTime, stopTime) + " seconds");

		// Get width and height of the raw image.
		setNoColumns(getOriginalImage().getWidth());
		setNoRows(getOriginalImage().getHeight());

		// Create an empty BufferedImage object
		setBuffImage(new BufferedImage(getNoColumns(), getNoRows(), BufferedImage.TYPE_INT_ARGB));

		// Draw Image into BufferedImage
		Graphics g = getBuffImage().getGraphics();
		g.drawImage(getOriginalImage(), 0, 0, null);

		// Convert the BufferedImage to numeric pixel representation.
		DataBufferInt dataBufferInt = (DataBufferInt) getBuffImage().getRaster().getDataBuffer();
		setPixels1D(dataBufferInt.getData());

		// Convert the pixel data in the 1D array to data in a 3D array to make it
		// easier to work with
		setPixels3D(convert3Dim(getPixels1D(), getNoColumns(), getNoRows()));

		// Instantiate a new object of the image processing class.
		// Note that this object is instantiated using the newInstance method of the
		// class named Class.
		try {
			imageProcessingObject = new ContrastMod();
			// Pass a 3D array of pixel data to the processing object and get a modified 3D
			// array of pixel data back
			setNewPixels3D(getImageProcessingObject().imageModification(contrast, getPixels3D(), getNoRows(), getNoColumns(),
					getOutputFile(), new File("out/backupCopy.bmp")));

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	// getters & setters
	protected BufferedImage getBuffImage() {
		return buffImage;
	}

	protected void setBuffImage(BufferedImage buffImage) {
		this.buffImage = buffImage;
	}

	public int[][][] getNewPixels3D() {
		return newPixels3D;
	}

	protected void setNewPixels3D(int[][][] newPixels3D) {
		this.newPixels3D = newPixels3D;
	}

	protected ImageInterface getImageProcessingObject() {
		return imageProcessingObject;
	}

	protected void setImageProcessingObject(ImageInterface imageProcessingObject) {
		this.imageProcessingObject = imageProcessingObject;
	}

	protected static String getDefaultImageProcessing() {
		return defaultImageProcessing;
	}

	protected static void setDefaultImageProcessing(String defaultImageProcessing) {
		DiffImages.defaultImageProcessing = defaultImageProcessing;
	}

	protected static File getOutputFile() {
		return outputFile;
	}

	protected static void setOutputFile(File outputFile) {
		DiffImages.outputFile = outputFile;
	}


}
