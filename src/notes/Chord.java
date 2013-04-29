package notes;

import static controller.FugueUtil.*;
import static controller.Rules.*;
import notes.scales.Scale;
import notes.types.*;

/**
 * The Chord class represents a chord within a scale and stores all the
 * possible notes within this chord, of all octaves.
 * 
 * Currently, it supports major, minor, augmented, or diminished triads.
 * 
 * @author Dan Pang
 *
 */
public class Chord {
	
	/**
	 * The lowest possible note is a G2 (bottom of bass clef staff).
	 */
	private static final Range RANGE = new Range(
			new Note(43), new Note (78));
	
	/**
	 * There should be 3 different possible octaves a note can be.
	 * **NOTE** In order for this to work, every note must appear the same
	 * 			number of times within the range given.
	 */
	private static final int NUM_NOTES =
			(RANGE.higher.getMidiNumber() + 1 - RANGE.lower.getMidiNumber())
			/ OCTAVE_LENGTH;
	
	/**
	 * The root pitch of the chord, stored as a NoteName.
	 */
	private NoteName root;
	
	/**
	 * Whether the chord is major, minor, etc.
	 */
	private ChordType type;
	
	/**
	 * This chord's function (tonic, dominant, etc.)
	 */
	private ChordFunction function;
	
	/**
	 * This chord's inversion.
	 * 0 for no inversion.
	 * -1 if no inversion specified.
	 */
	private int inversion;
	
	/**
	 * The scale used for this chord.
	 */
	private Scale scale;
	
	/**
	 * A list of all the roots of the chord.
	 */
	private Note[] roots;
	
	/**
	 * A list of all the thirds of the chord.
	 */
	private Note[] thirds;
	
	/**
	 * A list of all the fifths of the chord.
	 */
	private Note[] fifths;
	
	/**
	 * A list of all the notes in the chord.
	 */
	private Note[] notes;
	
	/**
	 * Constructor. Sets up a Chord with all the information given, where
	 * you don't care about the inversion.
	 * 
	 * @param rootOfChord
	 * 			Root of the chord
	 * @param chordType
	 * 			Whether the chord is major, minor, etc.
	 * @param function
	 * 			The harmonic function this chord serves
	 * @param scale
	 * 			The scale used in this chord
	 */
	public Chord(NoteName rootOfChord, ChordType chordType,
			ChordFunction function, Scale scale) {
		setUpChord(rootOfChord, chordType, function, scale, -1);
	}
	
	/**
	 * Constructor. Sets up the Chord with all the information given.
	 * 
	 * @param rootOfChord
	 * 			Root of the chord
	 * @param chordType
	 * 			Whether the chord is major, minor, etc.
	 * @param function
	 * 			The harmonic function this chord serves
	 * @param scale
	 * 			The scale used in this chord
	 * @param inversion
	 * 			The inversion of the chord
	 */
	public Chord(NoteName rootOfChord, ChordType chordType,
			ChordFunction function, Scale scale, int inversion) {
		setUpChord(rootOfChord, chordType, function, scale, inversion);
	}

	private void setUpChord(NoteName rootOfChord, ChordType chordType,
			ChordFunction function, Scale scale, int inversion) {
		// Check for valid input.
		if (chordType == null || function == null || rootOfChord == null ||
				scale == null) {
			throw new NullPointerException();
		}
		this.root = rootOfChord;
		this.type = chordType;
		this.function = function;
		this.scale = scale;
		this.inversion = inversion;
		
		// Determine what type of triad this chord is based on.
		Note chordRoot = new Note(rootOfChord.getLowestOctaveMidiNumber());
		Note chordThird;
		Note chordFifth;
		switch (chordType) {
		case MINOR:			// Minor triad
			chordThird = chordRoot.noteAt(3);
			chordFifth = chordRoot.noteAt(7);
			break;
		case AUGMENTED:		// Augmented triad
			chordThird = chordRoot.noteAt(4);
			chordFifth = chordRoot.noteAt(8);
			break;
		case DIMINISHED:	// Diminished triad
			chordThird = chordRoot.noteAt(3);
			chordFifth = chordRoot.noteAt(6);
			break;
		default:			// Default to major triad
			chordThird = chordRoot.noteAt(4);
			chordFifth = chordRoot.noteAt(7);
			break;
		}
		
		// Get all possible octaves of each note.
		roots = findAllOctaves(chordRoot);
		thirds = findAllOctaves(chordThird);
		fifths = findAllOctaves(chordFifth);
		notes = merge(merge(roots, thirds), fifths);
	}
	
	/**
	 * To see if two Chords represent the same chord
	 * 
	 * Ignores scale, chord function, and inversion. The first two are entirely
	 * context-reliant and don't contain information about the chord itself.
	 * The latter doesn't mean a different chord.
	 * 
	 * @param other
	 * 			The Chord to compare to
	 * @return Whether or not they represent the same chord
	 */
	public boolean equals(Chord other) {
		return root == other.getRootName() && type == other.getChordType();
	}
	
	/**
	 * ToString
	 */
	public String toString() {
		return root + " " + function;
	}
	
	/**
	 * You shouldn't be calling this on a regular chord.
	 * 
	 * @return Nothing.
	 */
	public Chord leadsTo() {
		throw new IllegalArgumentException("You shouldn't be calling this");
	}
	
	/**
	 * Finds out what the root of the chord is
	 * 
	 * @return The root of the chord
	 */
	public NoteName getRootName() {
		return root;
	}
	
	/**
	 * Finds out what type of chord it is (major, minor, etc.)
	 * 
	 * @return The chord's type
	 */
	public ChordType getChordType() {
		return type;
	}
	
	/**
	 * Find out whether the chord is tonic, dominant, etc.
	 * 
	 * @return The chord's function
	 */
	public ChordFunction getFunction() {
		return function;
	}
	
	/**
	 * Sets the chord's inversion.
	 * -1 for no inversion specified.
	 * 
	 * @param newInversion
	 * 			The new inversion
	 */
	public void setInversion(int newInversion) {
		inversion = newInversion;
	}
	
	/**
	 * Get the chord's inversion.
	 * -1 means no inversion specified.
	 * 
	 * @return See above
	 */
	public int getInversion() {
		return inversion;
	}
	
	/**
	 * Find the chord's scale.
	 * 
	 * @return The scale this chord is based in
	 */
	public Scale getScale() {
		return scale;
	}
	
	/**
	 * Get a list of all possible roots.
	 * 
	 * @return An array containing all possible roots of this chord.
	 */
	public Note[] getAllRoots() {
		return roots;
	}
	
	/**
	 * Checks if the given note is the root of this chord.
	 * 
	 * @param note
	 * 			The note to check
	 * @return Whether or not the note is the root of this chord
	 */
	public boolean isRoot(Note note) {
		for (int noteIndx = 0; noteIndx < NUM_NOTES; noteIndx++) {
			if (roots[noteIndx].equals(note)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get a list of all possible thirds.
	 * 
	 * @return An array containing all possible thirds of this chord.
	 */
	public Note[] getAllThirds() {
		return thirds;
	}
	
	/**
	 * Checks if the given note is the third of this chord.
	 * 
	 * @param note
	 * 			The note to check
	 * @return Whether or not the note is the third of this chord
	 */
	public boolean isThird(Note note) {
		for (int noteIndx = 0; noteIndx < NUM_NOTES; noteIndx++) {
			if (thirds[noteIndx].equals(note)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get a list of all possible fifths.
	 * 
	 * @return An array containing all possible fifths of this chord.
	 */
	public Note[] getAllFifths() {
		return fifths;
	}
	
	/**
	 * Checks if the given note is the fifth of this chord.
	 * 
	 * @param note
	 * 			The note to check
	 * @return Whether or not the note is the fifth of this chord
	 */
	public boolean isFifth(Note note) {
		for (int noteIndx = 0; noteIndx < NUM_NOTES; noteIndx++) {
			if (fifths[noteIndx].equals(note)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get a list of all possible fifths.
	 * 
	 * @return An array containing all possible fifths of this chord.
	 */
	public Note[] getAllNotes() {
		return notes;
	}
	
	/**
	 * Checks if the given note is a chord tone.
	 * 
	 * @param note
	 * 			The note to check
	 * @return Whether or not this note is a chord tone
	 */
	public boolean isInChord(Note note) {
		return isRoot(note) || isThird(note) || isFifth(note);
	}
	
	/**
	 * Given a note, find the next highest chord tone.
	 * 
	 * @param note
	 * 			The reference pitch
	 * @return The next highest chord tone
	 */
	public Note getNextHighestNote(Note note) {
		int noteIndx = 0;
		
		// Iterate through each note until you find the one that is higher than
		// the current one.
		// Return it.
		while (noteIndx < notes.length){
			if (notes[noteIndx].greaterThan(note)) {
				return new Note(notes[noteIndx]);
			}
			
			noteIndx++;
		}
		
		throw new IllegalArgumentException("Note out of range!");
	}
	
	/**
	 * Returns all possible bass notes in the given range.
	 * Takes inversion into account when doing so.
	 * 
	 * @param range
	 * 			The lowest and highest the note can be (inclusive)
	 * @return All possible bass notes in the given range
	 */
	public Note[] getAllBassNotesBetween(Range range) {
		switch (getInversion()) {
		case 0:
			// No inversion means use only a root note.
			return getAllRootsBetween(range);
		case 1:
			// First inversion means use only a third.
			return getAllThirdsBetween(range);
		case 2:
			// Second inversion means use only a fifth.
			return getAllFifthsBetween(range);
		default:
			// Otherwise, default to only roots and thirds.
			return merge(
					getAllRootsBetween(range),
					getAllThirdsBetween(range)
					);
		}
	}
	
	/**
	 * Returns all the roots in the given range.
	 * 
	 * @param range
	 * 			The lowest and highest the note can be (inclusive)
	 * @return All the root notes in the given range
	 */
	public Note[] getAllRootsBetween(Range range) {
		return getAllNotesBetween(range, roots);
	}
	
	/**
	 * Returns all the thirds in the given range.
	 * 
	 * @param range
	 * 			The lowest and highest the note can be (inclusive)
	 * 			Second note
	 * @return All the thirds in the given range
	 */
	public Note[] getAllThirdsBetween(Range range) {
		return getAllNotesBetween(range, thirds);
	}
	
	/**
	 * Returns all the fifths in the given range.
	 * 
	 * @param range
	 * 			The lowest and highest the note can be (inclusive)
	 * @return All the fifths in the given range
	 */
	public Note[] getAllFifthsBetween(Range range) {
		return getAllNotesBetween(range, fifths);
	}
	
	/**
	 * Returns all the notes in the given range.
	 * 
	 * @param range
	 * 			The lowest and highest the note can be (inclusive)
	 * @return All the notes in the given range
	 */
	public Note[] getAllNotesBetween(Range range) {
		return getAllNotesBetween(range, notes);
	}
	
	/**
	 * Finds all the notes in the array in the given range.
	 * 
	 * @param range
	 * 			The lowest and highest the note can be (inclusive)
	 * @param noteArray
	 * 			The array of notes to search
	 * @return All the notes in the given range
	 */
	private Note[] getAllNotesBetween(Range range, Note[] noteArray) {
		Note note1 = range.lower;
		Note note2 = range.higher;
		
		// Declare the two noteIndx variables outside the for loop because
		// we'll use them later.
		int baseNoteIndx = 0;
		int noteIndx = 0;
		Note[] ret = new Note[4];
		
		// Get to where the range starts
		for (; baseNoteIndx < noteArray.length; baseNoteIndx++){
			if (noteArray[baseNoteIndx].greaterThan(note1) ||
					noteArray[baseNoteIndx].equals(note1)) {
				break;
			}
		}
		
		// Iterate until the range ends, or until the end of the array
		for (; baseNoteIndx + noteIndx < noteArray.length; noteIndx++) {
			if (noteArray[baseNoteIndx + noteIndx].greaterThan(note2)) {
				break;
			}
			// Enlarge the array if needed.
			if (noteIndx == ret.length) {
				ret = resizeArray(ret, ret.length * 2);
			}
			ret[noteIndx] = new Note(noteArray[baseNoteIndx + noteIndx]);
		}
		
		// Make sure to resize the array so that there are no empty spaces
		return resizeArray(ret, noteIndx);
	}
	
	/**
	 * Finds all the possible octaves of the given note.
	 * 
	 * @param note
	 * 			The pitch to find all variants of
	 * @return An array of all the octaves of a note
	 */
	private static Note[] findAllOctaves(Note note) {
		// Create the return array and find the lowest octave.
		Note[] allNotes = new Note[NUM_NOTES];
		Note lowestNote = findLowestOctave(note);
		
		// Add each octave to the list.
		for (int noteCount = 0; noteCount < NUM_NOTES; noteCount++) {
			allNotes[noteCount] = lowestNote;
			lowestNote = lowestNote.noteAt(OCTAVE_LENGTH);
		}
		
		return allNotes;
	}
	
	/**
	 * Finds the lowest possible octave of a note
	 * 
	 * @param note
	 * 			The note to find the lowest version of
	 * @return The lowest possible octave of that note
	 */
	private static Note findLowestOctave(Note note) {
		// Find the lowest possible note value.
		Note lowestOctave = new Note(note.getNoteName().getLowestOctaveMidiNumber());
		
		// Bump it up until it's at or above the lowest note.
		while (RANGE.lower.greaterThan(lowestOctave)) {
			lowestOctave = lowestOctave.noteAt(OCTAVE_LENGTH);
		}
		
		return lowestOctave;
	}
}
