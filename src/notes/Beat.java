package notes;

import java.util.Random;

import notes.scales.Scale;

import static controller.FugueUtil.*;
import static controller.Rules.*;

import exceptions.*;

/**
 * The Beat class represents a beat of a Song, in simple time.
 * 
 * @author Dan Pang
 *
 */
public class Beat {
	
	/**
	 * The bass range is G2 to A3 (43-57).
	 * The tenor range is E3 to Eb4 (52-63).
	 * The alto range is Bb3 to C5 (58-72).
	 * The soprano range is E4 to F#5 (64-78).
	 */
	private static final Range[] RANGES = {
		new Range(new Note(43), new Note(57)),
		new Range(new Note(52), new Note(63)),
		new Range(new Note(58), new Note(72)),
		new Range(new Note(64), new Note(78))
	};
	
	/**
	 * The number of voices.
	 */
	private int numVoices = 4;
	
	/**
	 * The chord followed for this beat.
	 */
	private Chord chord;
	
	/**
	 * The notes played on this beat
	 */
	private Note[] chordTones;
	
	/**
	 * The notes played on the first half of the beat.
	 */
	private Note[] noteFirstHalf;
	
	/**
	 * The notes played on the second half of the beat.
	 */
	private Note[] noteSecondHalf;
	
	/**
	 * Keeps track of whether the previous note was a leap.
	 */
	private boolean[] leaped;
	
	/**
	 * The previous beat. Some rules of 2nd species counterpoint refer to
	 * previous notes in the piece.
	 */
	private Beat prevBeat;
	
	/**
	 * A flag whether or not this beat contains a non chord tone.
	 */
	private boolean containsNonChordTone;
	
	/**
	 * The random number generator
	 */
	private Random randomizer;
	
	/**
	 * Constructor. Sets up all the member variables.
	 * 
	 * @param chord
	 * 			The chord to be played on this beat
	 * @param prevBeat
	 * 			The previous beat
	 */
	public Beat(Chord chord, Beat prevBeat, int numVoices) {
		// Check for valid input
		if (chord == null) {
			throw new IllegalArgumentException("No chord specified!");
		}
		this.numVoices = numVoices;
		this.chordTones = new Note[numVoices];
		this.noteFirstHalf = new Note[numVoices];
		this.noteSecondHalf = new Note[numVoices];
		this.leaped = new boolean[numVoices];
		this.chord = chord;
		this.prevBeat = prevBeat;
		this.containsNonChordTone = false;
		this.randomizer = new Random();
	}
	
	/**
	 * Just gets the notes played in this beat.
	 * 
	 * @return The notes played in this beat
	 */
	public Note[] getChordTones() {
		return chordTones;
	}
	
	/**
	 * Just gets the notes played on the second half of this beat.
	 * 
	 * @return The notes played on the second half of this beat
	 */
	public Note[] getNotesFirstHalf() {
		return noteFirstHalf;
	}
	
	/**
	 * Gets the notes played on the second half of this beat.
	 * 
	 * @return The notes played on the second half of this beat
	 */
	public Note[] getNotesSecondHalf() {
		return noteSecondHalf;
	}
	
	/**
	 * Sets the second half of the beat.
	 * 
	 * @param voice
	 * 			The voice to change
	 * @param newNote
	 * 			The new note
	 */
	public void setNoteSecondHalf(int voice, Note newNote) {
		noteSecondHalf[voice] = newNote;
		if (!chord.isInChord(newNote)) {
			containsNonChordTone = true;
		}
	}
	
	/**
	 * Just gets the chord played on this beat.
	 * 
	 * @return The chord played on this beat
	 */
	public Chord getChord() {
		return chord;
	}
	
	/**
	 * Finds whether or not each voice contains a leap this beat.
	 * 
	 * @return See above
	 */
	public boolean[] getLeaps() {
		return leaped;
	}
	
	/**
	 * Finds whether or not this beat contains non-chord tones.
	 * 
	 * @return See above
	 */
	public boolean containsNonChordTone() {
		return containsNonChordTone;
	}
	
	/**
	 * Generates the next beat.
	 * If no valid note configuration can be found, throws an
	 * InvalidChordException.
	 * 
	 * @throws InvalidChordException
	 * 			If no valid note configuration can be found
	 */
	public void generateNotes() throws InvalidChordException {
		// If this is the first chord of the song, treat it differently.
		if (prevBeat == null) {
			generateFirstChord();
			return;
		}
		
		// Until all the rules are followed, continue generating combinations.
		int tries = 0;
		boolean notesFollowAllRules = false;
		while (!notesFollowAllRules) {
			// Generate notes for each voice.
			try {
				for (int voiceIndx = 0; voiceIndx < numVoices; voiceIndx++) {
					generateNote(voiceIndx);
				}
			} catch (InvalidChordException ex) {
				continue;
			}
			
			// Check to make sure this beat follows all the rules.
			notesFollowAllRules = containsAllChordTones(chordTones, chord);
			notesFollowAllRules &= noParallelFifths(prevBeat.getChordTones(),
					chordTones);
			
			// Keep track of the number of attempts. If it goes on for too
			// long, give up.
			tries++;
			if (tries >= 100) {
				System.out.println("Cannot find note combination for this chord");
				throw new InvalidChordException(
						"Cannot find note combination for this chord");
			}
		}
		
		for (int voiceIndx = 0; voiceIndx < numVoices; voiceIndx++) {
			setPlayedNotes(voiceIndx);
			addNonChordTones(voiceIndx);
		}
	}
	
	/**
	 * Generates the first chord.
	 * 
	 * The bass must be a root.
	 * 
	 * No note can be below a lower voice's note.
	 * No note can be more than an octave higher than the next lowest note.
	 */
	private void generateFirstChord() {
		for (int indx = 0; indx < leaped.length; indx++) {
			leaped[indx] = false;
		}
		
		// Find the root's range
		// Get a list of all possible notes in that range.
		Note[] allNotes = chord.getAllRootsBetween(RANGES[0]);
		// Pick a random note and use it.
		int noteChoice = randomizer.nextInt(allNotes.length);
		chordTones[0] = allNotes[noteChoice];
		setPlayedNotes(0);
		
		for (int voiceIndx = 1; voiceIndx < numVoices; voiceIndx++) {
			// Find the voice's range.
			// The voice cannot be lower than its lowest note or the note
			// of the voice below it.
			// The voice cannot be higher than its highest note or over
			// an octave above the note below it.
			Range range = new Range(
					max(
							chordTones[voiceIndx - 1].noteAt(1),
							RANGES[voiceIndx].lower
							),
					min(
							chordTones[voiceIndx - 1].noteAt(OCTAVE_LENGTH),
							RANGES[voiceIndx].higher
							)
					);
			
			// Get a list of all notes in that range.
			allNotes = chord.getAllNotesBetween(range);
			
			// Pick a random one and use it.
			noteChoice = randomizer.nextInt(allNotes.length);
			chordTones[voiceIndx] = allNotes[noteChoice];
			setPlayedNotes(voiceIndx);
		}
	}
	
	/**
	 * Generates a note.
	 * 
	 * @param voiceIndx
	 * 			The voice the note should be generated for
	 * @param range
	 * 			The voice's range
	 * @throws InvalidChordException
	 * 			If it cannot find a note
	 */
	private void generateNote(int voiceIndx) throws InvalidChordException {
		// Find the previous note.
		Note prevNote = prevBeat.getChordTones()[voiceIndx];
		boolean doNotLeap = prevBeat.getLeaps()[voiceIndx];
		
		Range range = getNoteRange(voiceIndx);
		
		// Get a list of possible notes.
		Note[] possibleNotes;
		if (voiceIndx == 0) {
			// If it's a bass voice, follow the rules for inversions.
			possibleNotes = chord.getAllBassNotesBetween(range);
		} else {
			// Otherwise, draw from any of them.
			possibleNotes = chord.getAllNotesBetween(range);
		}
		
		// Randomly pick a note.
		// If it works, stick with it.
		// Otherwise, remove it and try again.
		while (possibleNotes.length > 0) {
			int newNoteIndx = randomizer.nextInt(possibleNotes.length);
			Note newNote = possibleNotes[newNoteIndx];
			if (isValidLeap(newNote, prevNote, doNotLeap)) {
				chordTones[voiceIndx] = newNote;
				leaped[voiceIndx] = Math.abs(newNote.getInterval(prevNote)) > 2;
				return;
			}
			
			possibleNotes = removeElement(newNoteIndx, possibleNotes);
		}
		
		throw new InvalidChordException("Cannot find a valid note!");
	}
	
	/**
	 * Find this note's range.
	 * 
	 * @param voiceIndx
	 * 			The voice to look at
	 * @return The Range representing this voice's highest and lowest possible
	 * 		   notes
	 */
	private Range getNoteRange(int voiceIndx) {
		Note prevNote = prevBeat.getChordTones()[voiceIndx];
		
		// The lowest this note can be is the lowest note, one half-step higher
		// than the next lowest voice's note, or an octave below the previous
		// note.
		Note lowest;
		if (voiceIndx == 0) {
			// If this is the lowest note, there's obviously no note lower than it.
			lowest = max(
					RANGES[voiceIndx].lower,
					prevNote.noteAt(0 - OCTAVE_LENGTH)
					);
		} else {
			// If not, then we need to take that into account.
			lowest = max(
					RANGES[voiceIndx].lower,
					chordTones[voiceIndx - 1].noteAt(1),
					prevNote.noteAt(0 - OCTAVE_LENGTH)
					);
		}
		
		// The highest this note can be is either the highest bass note, or
		// an octave above the previous note.
		Note highest = min(
				RANGES[voiceIndx].higher,
				prevNote.noteAt(OCTAVE_LENGTH)
				);
		
		Range range = new Range(lowest, highest);
		return range;
	}
	
	/**
	 * Adds non chord tones to the beat.
	 * 
	 * @param voiceIndx
	 * 			The voice to add non chord tones to
	 */
	private void addNonChordTones(int voiceIndx) {
		// Get the current note, the previous note, and the interval between them.
		Note currNote = chordTones[voiceIndx];
		Note prevNote = prevBeat.getChordTones()[voiceIndx];
		int interval = Math.abs(currNote.getInterval(prevNote));
		
		// If you can add a passing tone, add it.
		if (interval == 3 || interval == 4) {
			try {
				generatePassingTone(voiceIndx);
			} catch (IllegalArgumentException ex) {
				// whoops
			}
		}
		
		// If the voice didn't leap into the previous note, but did into this
		// one, and if there isn't already a non-chord tone, add an
		// appogiatura
		boolean prevNoteLeaped = prevBeat.getLeaps()[voiceIndx];
		if (!containsNonChordTone && !prevNoteLeaped && leaped[voiceIndx]) {
			// There's only a 60% chance of this happening
			float insertNonChordTone = randomizer.nextFloat();
			if (insertNonChordTone < 0.6 - .1 * voiceIndx) {
				return;
			}
			
			generateNeighborTone(voiceIndx);
		}
	}
	
	/**
	 * Generates a neighbor tone (or an appogiatura, depending on the
	 * previous note).
	 * 
	 * @param voiceIndx
	 * 			The voice to look at
	 */
	private void generateNeighborTone(int voiceIndx) {
		Note currNote = chordTones[voiceIndx];
		Scale prevScale = prevBeat.getChord().getScale();
		
		// Pick either an upper or a lower note
		Note[] neighborTones = new Note[2];
		neighborTones[0] = prevScale.getLowerNeighbor(currNote);
		neighborTones[1] = prevScale.getUpperNeighbor(currNote);
		int choice = randomizer.nextInt(2);
		
		// Use it
		prevBeat.setNoteSecondHalf(voiceIndx, neighborTones[choice]);
		leaped[voiceIndx] = false;
		containsNonChordTone = true;
	}
	
	/**
	 * Creates a passing tone.
	 * 
	 * @param voiceIndx
	 * 			The voice to look at
	 * @throws IllegalArgumentException
	 * 			If the current note and the previous note can't have a passing
	 * 			tone between them
	 */
	private void generatePassingTone(int voiceIndx)
			throws IllegalArgumentException {
		// Find this beat's note, the previous beat's note, and the scale
		// used in the previous beat.
		Note currNote = chordTones[voiceIndx];
		Note prevNote = prevBeat.getChordTones()[voiceIndx];
		Scale prevScale = prevBeat.getChord().getScale();
		
		// Find the lower and higher of the two notes.
		Note lowerNote = min(currNote, prevNote);
		Note higherNote = max(currNote, prevNote);
		
		// Find the passing tone, set it, and set any associated variables
		Note passingTone = prevScale.getPassingTone(lowerNote, higherNote);
		prevBeat.setNoteSecondHalf(voiceIndx, passingTone);
		leaped[voiceIndx] = false;
		containsNonChordTone = true;
	}
	
	/**
	 * Sets the noteFirstHalf and noteSecondHalf variables to the current chord
	 * tone.
	 * 
	 * @param voiceIndx
	 * 			The voice to copy over
	 */
	private void setPlayedNotes(int voiceIndx) {
		noteFirstHalf[voiceIndx] = chordTones[voiceIndx];
		noteSecondHalf[voiceIndx] = chordTones[voiceIndx];
	}
}
