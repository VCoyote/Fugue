package controller;

import java.io.*;
import javax.sound.midi.*;

import notes.*;

import exceptions.*;

/**
 * The MidiExporter takes a Song and exports it to a MIDI file, or plays it.
 * Very rudimentary function; only takes the system's default player.
 * 
 * Lots of thanks to jsresources.org for helping me figure out what the hell
 * I'm doing
 * 
 * @author Dan Pang
 *
 */
public class MidiExporter {
	
	/**
	 * The note's velocity
	 */
	private static final int VELOCITY = 64;
	
	/**
	 * The Sequence that represents the song
	 */
	private Sequence sequence;
	
	/**
	 * The sequencer used to play the song
	 */
	private Sequencer sequencer;
	
	/**
	 * The Song itself
	 */
	private Song song;
	
	/**
	 * Constructor. It breaks a Song into its MidiEvent components, and
	 * adds them together in order to create a MIDI file.
	 * 
	 * @param song
	 * 			The song to export
	 */
	public MidiExporter() {
		// Create the Sequence that will contain everything
		try {
			sequence = new Sequence(Sequence.PPQ, 2);
		} catch (InvalidMidiDataException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Import a song into the MidiExporter to play or export.
	 * 
	 * @param song
	 * 			The Song to convert to MIDI
	 */
	public void importSong(Song song) {
		// Fetch important data from the song.
		// Meaning, metadata and the song itself.
		Beat[] beats = song.getBeatArray();
		this.song = song;
		
		// Create a track for each voice.
		int numVoices = song.getNumVoices();
		for (int voiceCount = 0; voiceCount < numVoices; voiceCount++) {
			sequence.createTrack();
		}
		
		// Iterate through each beat, adding each note to the corresponding
		// track.
		Track[] tracks = sequence.getTracks();
		for (int beat = 0; beat < beats.length; beat++) {
			Note[] firstHalf = beats[beat].getNotesFirstHalf();
			Note[] secondHalf = beats[beat].getNotesSecondHalf();
			// Iterate through each note in the beat, adding it to the
			// corresponding track.
			for (int note = 0; note < firstHalf.length; note++) {
				if (firstHalf[note] == secondHalf[note]) {
					createNote(firstHalf[note], 2 * beat, 2, tracks[note]);
				} else {
					createNote(firstHalf[note], 2 * beat, 1, tracks[note]);
					createNote(secondHalf[note], 2 * beat + 1, 1, tracks[note]);
				} // if/else
			} // for
		} // for
		
		try {
			setUpSequencer();
		} catch (MidiUnavailableException ex) {
			System.out.println("Unable to set up sequencer");
			// do nothing
		}
	}
	
	/**
	 * Plays back the song.
	 */
	public void play() throws MidiUnavailableException {
		sequencer.start();
	}
	
	/**
	 * Sets up the sequencer with a given sequence.
	 * @param sequenceToUse
	 * @throws MidiUnavailableException
	 */
	private void setUpSequencer()
			throws MidiUnavailableException {
		// First, get the system's default sequencer.
		try {
			sequencer = MidiSystem.getSequencer();
		} catch (MidiUnavailableException ex) {
			// Something went wrong.
			ex.printStackTrace();
			System.exit(1);
		}
		
		// If there is none, throw an exception.
		if (sequencer == null) {
			String msg = "Cannot find a sequencer";
			throw new MidiUnavailableException(msg);
		}
		
		// Set up the transmitter and receiver of the synth to play the song.
		linkTransmitterToReceiver();
	}
	
	/**
	 * Gets the default transmitter and receiver, and then links them.
	 */
	private void linkTransmitterToReceiver() {
		try {
			// Set up the sequencer (including its tempo)
			sequencer.open();
			sequencer.setSequence(sequence);
			sequencer.setTempoInBPM(song.getBPM());
			
			// Get the system's default synthesizer and set that up, too.
			Synthesizer synth = MidiSystem.getSynthesizer();
			synth.open();
			
			// Get the receiver and transmitter to use and set those up.
			Receiver receiver = synth.getReceiver();
			Transmitter transmitter = sequencer.getTransmitter();
			transmitter.setReceiver(receiver);
		} catch (Exception ex) {
			// Something went wrong.
			ex.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Exports to a file.
	 * 
	 * @param outputFileName
	 * 			The output file name
	 * @throws InvalidFileFormatException
	 * 			If the file name doesn't end in .mid or .midi
	 */
	public void exportToFile(String outputFileName) 
			throws InvalidFileFormatException {
		// Check for a valid file format.
		if (!outputFileName.endsWith(".mid")
				&& !outputFileName.endsWith(".midi")) {
			String msg = "File names must end in .mid or .midi";
			throw new InvalidFileFormatException(msg);
		}
		
		// Find a supported file type, and export the file.
		int[] types = MidiSystem.getMidiFileTypes(sequence);
		try {
			File outputFile = new File(outputFileName);
			MidiSystem.write(sequence, types[0], outputFile);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Create a note and add it to the track.
	 * 
	 * @param note
	 * 			The MIDI value for the note to be played
	 * @param startTime
	 * 			When this note should be played
	 * @param duration
	 * 			How long the note should last
	 */
	private void createNote(Note note, int startTime, int duration, Track track) {
		int endTime = startTime + duration;
		int midiNumber = note.getMidiNumber();
		track.add(createNoteEvent(ShortMessage.NOTE_ON, midiNumber, startTime, VELOCITY));
		track.add(createNoteEvent(ShortMessage.NOTE_OFF, midiNumber, endTime, 0));
	}
	
	/**
	 * Create the MidiEvent for a note, given the data.
	 * 
	 * @param command
	 * 			The command value for the ShortMessage
	 * @param note
	 * 			The MIDI value for the note to be played
	 * @param eventTime
	 * 			When this event should occur
	 * @param velocity
	 * 			The velocity of this note
	 * @return The MidiEvent for the note
	 */
	private MidiEvent createNoteEvent(int command, int note, int eventTime, int velocity) {
		// Create the message and set its parameters to the ones given.
		ShortMessage message = new ShortMessage();
		try {
			message.setMessage(command, 0, note, velocity);
		} catch (InvalidMidiDataException ex) {
			// Something went wrong.
			ex.printStackTrace();
			System.exit(1);
		}
		
		// Create the MidiEvent and return it.
		return new MidiEvent(message, eventTime);
	}
}
