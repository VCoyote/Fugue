package gui;

import java.awt.*;
import javax.swing.*;

import notes.types.*;
import exceptions.InvalidFileFormatException;

/**
 * The main GUI for the Fugue.
 * 
 * Deals with the user interface and contains the main function.
 * 
 * @author Dan Pang
 *
 */
@SuppressWarnings("serial") // ignore
public class FugueMain extends JFrame {
	
	/**
	 * A list of all the notes in the chromatic scale.
	 */
	private static final String[] KEY_NAMES = {
		"C",
		"C#",
		"D",
		"D#",
		"E",
		"F",
		"F#",
		"G",
		"G#",
		"A",
		"A#",
		"B"
	};
	
	/**
	 * Major or minor
	 */
	private static final String[] KEY_TYPES = {
		"Major",
		"Minor"
	};
	
	/**
	 * The JPanel containing everything.
	 */
	private JPanel mainWindow;
	
	/**
	 * The text field containing the song's tempo (in BPM).
	 */
	private JTextField bpmField;
	
	/**
	 * The text field containing the song's minimum length (in minutes:seconds).
	 */
	private JTextField lengthField;
	
	/**
	 * The text field containing the output file's name.
	 */
	private JTextField outFileNameField;
	
	/**
	 * The combo box containing the list of possible keys.
	 */
	private JComboBox<String> keyList;
	
	/**
	 * The combo box containing whether the song is in major or minor.
	 */
	private JComboBox<String> keyTypeList;
	
	/**
	 * The button to create the song and start playing.
	 */
	private JButton playButton;
	
	
	/**
	 * Constructor. Sets up the window.
	 */
	public FugueMain() {
		
		this.setTitle("Fugue");
		this.setSize(300, 250);
		
		mainWindow = new JPanel(new GridBagLayout());
		addAllTextFields();
		
		this.setContentPane(mainWindow);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Returns the BPM entered in the tempo field.
	 * 
	 * @return The BPM entered
	 * @throws NumberFormatException
	 * 			If the text entered isn't a number
	 */
	public int getBPM() throws NumberFormatException {
		// Get the text and return it.
		String bpm = bpmField.getText();
		int ret = Integer.parseInt(bpm);
		
		// Continue checking for valid input.
		if (ret <= 0) {
			String msg = "Tempo must be a positive, nonzero number!";
			throw new NumberFormatException(msg);
		}
		
		return ret;
	}
	
	/**
	 * Returns the minimum song length, in seconds, entered in the minimum
	 * length field.
	 * 
	 * @return The minimum song length entered
	 * @throws NumberFormatException
	 * 			If the text entered isn't in the right format
	 */
	public int getMinLengthInSeconds() throws NumberFormatException {
		// Get the text and split it into components separated by ":".
		String minLength = lengthField.getText();
		String[] minSec = minLength.split(":");
		
		// If there aren't two components, something's not right.
		if (minSec.length != 2) {
			String msg = "Length must use the following format: \n\n" +
					"Minutes:Seconds (eg, 2:35)";
			throw new NumberFormatException(msg);
		}
		
		// Find the number of minutes and the number of seconds.
		int minutes = Integer.parseInt(minSec[0]);
		int seconds = Integer.parseInt(minSec[1]);
		
		// Convert that to seconds only.
		return (60 * minutes) + seconds;
	}
	
	/**
	 * Returns the output file name entered in the output file name field.
	 * 
	 * @return The output file name entered
	 * @throws InvalidFileFormatException
	 * 			If the file name doesn't end in .mid or .midi
	 */
	public String getOutputFileName() throws InvalidFileFormatException {
		String outputFileName = outFileNameField.getText();
		
		// Check for a valid file format.
		if (!outputFileName.endsWith(".mid")
				&& !outputFileName.endsWith(".midi")) {
			String msg = "File names must end in .mid or .midi";
			throw new InvalidFileFormatException(msg);
		}
		
		return outputFileName;
	}
	
	/**
	 * Returns the selected starting pitch.
	 * 
	 * @return The starting pitch selected
	 */
	public NoteName getStartingPitch() {
		int selectedIndex = keyList.getSelectedIndex();
		return NoteName.getNoteNameFromMidiNumber(selectedIndex);
	}
	
	/**
	 * Returns whether the starting key should be major or minor.
	 * 
	 * @return Whether the starting key should be major or minor
	 */
	public ScaleType getStartingKeyType() {
		int selectedType = keyTypeList.getSelectedIndex();
		if (selectedType == 0) {
			return ScaleType.MAJOR;
		} else {
			return ScaleType.MINOR;
		}
	}
	
	/**
	 * Display the given error message.
	 * 
	 * @param errorMsg
	 * 			The error message to display
	 */
	public void displayErrorWindow(String errorMsg) {
		JOptionPane.showMessageDialog(null, errorMsg, "Error!",
				JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Adds all the elements to the window.
	 */
	private void addAllTextFields() {
		// Add a little welcome message
		addToWindow(new JLabel("Welcome to Fugue!"), 0, 0, 2);
		
		addToWindow(Box.createVerticalStrut(20), 0, 1, 1);
		
		// Add all the fields
		addTempoField();
		addStartingKeyField();
		addMinLengthField();
		addOutputFileNameField();
		
		addToWindow(Box.createVerticalStrut(20), 0, 6, 1);
		
		// Add the play/stop buttons
		playButton = new JButton("Generate Music!");
		PlayController controller = new PlayController(this);
		playButton.addActionListener(controller);
		addToWindow(playButton, 2, 7, 1);
	}
	
	/**
	 * Adds the field asking for the tempo.
	 */
	private void addTempoField() {
		JLabel msg = new JLabel("Tempo: ");
		addToWindow(msg, 0, 2, 1);
		
		bpmField = new JTextField(3);
		addToWindow(bpmField, 1, 2, 1);
		
		msg = new JLabel("BPM");
		addToWindow(msg, 2, 2, 1);
	}
	
	/**
	 * Adds the fields asking for the starting key.
	 */
	private void addStartingKeyField() {
		JLabel msg = new JLabel("Starting key: ");
		addToWindow(msg, 0, 3, 1);
		
		keyList = new JComboBox<String>(KEY_NAMES);
		addToWindow(keyList, 1, 3, 1);
		
		keyTypeList = new JComboBox<String>(KEY_TYPES);
		addToWindow(keyTypeList, 2, 3, 1);
	}
	
	/**
	 * Adds the field asking for the minimum length.
	 */
	private void addMinLengthField() {
		JLabel msg = new JLabel("Min. Length: ");
		addToWindow(msg, 0, 4, 1);
		
		lengthField = new JTextField(10);
		addToWindow(lengthField, 1, 4, 1);
		
		msg = new JLabel("Min:Sec");
		addToWindow(msg, 2, 4, 1);
	}

	/**
	 * Adds field asking for the output file's name.
	 */
	private void addOutputFileNameField() {
		JLabel msg = new JLabel("Output file: ");
		addToWindow(msg, 0, 5, 1);
		
		outFileNameField = new JTextField(20);
		addToWindow(outFileNameField, 1, 5, 2);
	}
	
	/**
	 * Adds the element to the window in the desired location.
	 * 
	 * @param element
	 * 			The element to add to the window
	 * @param x
	 * 			The X position of the element
	 * @param y
	 * 			The Y position of the element
	 * @param width
	 * 			How many X positions the element should take up
	 */
	private void addToWindow(Component element, int x, int y, int width) {
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		
		// Add the dialog
		c.gridx = x;
		c.gridy = y;
		c.gridwidth = width;
		mainWindow.add(element, c);
	}
	
	/**
	 * Main function. Runs everything.
	 * 
	 * @param args
	 * 			Unused
	 */
	public static void main(String[] args) {
		new FugueMain();
	}

}
