package gui;

import java.awt.event.*;

import controller.*;
import exceptions.InvalidFileFormatException;
import exceptions.InvalidInputException;
import notes.types.*;

/**
 * The controller for Fugue. Controls the interface between the
 * GUI and the model.
 * 
 * @author Dan Pang
 *
 */
public class PlayController implements ActionListener {
	
	/**
	 * The main user interface to get all the data from.
	 */
	private FugueMain userInterface;
	
	/**
	 * The Song to interact with.
	 */
	private Song song;
	
	/**
	 * The MidiExporter to deal with MIDI data
	 */
	private MidiExporter midiInterface;
	
	/**
	 * The song's tempo
	 */
	int tempo;
	
	/**
	 * The song's minimum length
	 */
	int minLength;
	
	/**
	 * The song's output file
	 */
	String outFileName;
	
	/**
	 * The song's starting pitch
	 */
	NoteName startingKey;
	
	/**
	 * Whether the starting key is major or minor
	 */
	ScaleType startingKeyType;
	
	/**
	 * Constructor. Takes the FugueMain where all the data is input.
	 * 
	 * @param mainUI
	 * 			The FugueMain main UI to get all the information from
	 */
	public PlayController(FugueMain mainUI) {
		userInterface = mainUI;
	}
	
	/**
	 * Overrides the actionPerformed. Sets up the song, exports the MIDI file,
	 * and plays it.
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		// Get the information for the song.
		// If there are errors, display an error message and abort.
		try {
			getSongInfo();
		} catch (InvalidInputException ex) {
			userInterface.displayErrorWindow(ex.getMessage());
			return;
		}
		
		// Create the song and generate it.
		song = new Song(startingKey, startingKeyType, tempo, minLength);
		song.generateSong();
		
		// Export to MIDI and then play it using the default synth
		try {
			midiInterface = new MidiExporter();
			midiInterface.importSong(song);
			midiInterface.exportToFile(outFileName);
			midiInterface.play();
		} catch (Exception ex) {
			String errorMsg = "Error: \n\n" +
					ex.getMessage();
			userInterface.displayErrorWindow(errorMsg);
		}
	}
	
	/**
	 * Gathers information from the UI in order to create the song.
	 * 
	 * If there is an input error, it returns an error message.
	 * 
	 * @return An error message, or null if there is none
	 */
	private void getSongInfo() throws InvalidInputException {
		// Get the starting key.
		startingKey = userInterface.getStartingPitch();
		startingKeyType = userInterface.getStartingKeyType();
		
		// Get the tempo
		try {
			tempo = userInterface.getBPM();
		} catch (NumberFormatException ex) {
			throw new InvalidInputException("Error: \n\n" +
					"You must enter a positive number for the tempo");
		}
		
		// Get the minimum length
		try {
			minLength = userInterface.getMinLengthInSeconds();
		} catch (NumberFormatException ex) {
			throw new InvalidInputException("Error: \n\n" +
					"The min:sec format was not followed!");
		}
		
		// Get the output file name
		try {
			outFileName = userInterface.getOutputFileName();
		} catch (InvalidFileFormatException ex) {
			throw new InvalidInputException("Error: \n\n" +
					"File name must end in .mid or .midi");
		}
	}
}
