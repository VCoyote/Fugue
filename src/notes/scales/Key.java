package notes.scales;

import notes.Chord;
import notes.Note;
import notes.types.*;

/**
 * The Key class represents the key signature of the song and keeps track of
 * all the possible chords that can be used within this key.
 * 
 * @author Dan Pang
 *
 */
public abstract class Key {
	
	/**
	 * All the notes in the scale.
	 */
	protected NoteName[] notes;
	
	/**
	 * The tonic chord
	 */
	protected Chord tonic;
	
	/**
	 * The subdominant chords
	 */
	protected Chord[] subdominant;
	
	/**
	 * The dominant chords
	 */
	protected Chord[] dominant;
	
	protected Chord[] resolution;
	
	protected Chord[] predominant;
	
	protected Chord[] secondaryDominant;
	
	/**
	 * Constructor. Given a root pitch and a scale type, create lists of every
	 * chord that exists in the scale.
	 * 
	 * @param rootPitch
	 * 			The pitch considered tonic
	 * @param scaleType
	 * 			Whether the scale is major or minor
	 */
	public Key(NoteName rootPitch) {
		// Check for valid input
		if (rootPitch == null) {
			throw new IllegalArgumentException();
		}
		
		setUpScale(rootPitch);
		setUpChords(rootPitch);
	}
	
	/**
	 * Gets all chords fulfilling the given function.
	 * 
	 * @param chordFunction
	 * 			Whether a chord is tonic, dominant, etc.
	 * @return All the chords fulfilling that function
	 */
	public Chord[] getChords(ChordFunction chordFunction) {
		switch (chordFunction) {
		case TONIC:
			Chord[] ret = {tonic};
			return ret;
		case RESOLUTION:
			return resolution;
		case SUBDOMINANT:
			return subdominant;
		case DOMINANT:
			return dominant;
		case PREDOMINANT:
			return predominant;
		case SECONDARY_DOMINANT:
			return secondaryDominant;
		}
		throw new IllegalArgumentException("Invalid chord function!");
	}
	
	/**
	 * Gets all the notes in the scale.
	 * 
	 * @return A list of all the notes in the scale
	 */
	public NoteName[] getScale() {
		return notes;
	}
	
	/**
	 * Get a list of keys that you can modulate to from this one.
	 * 
	 * @return See above
	 */
	public abstract Key[] getPossibleModulations();
	
	/**
	 * See if this chord exists within this key.
	 * 
	 * @param chord
	 * 			The chord to check
	 * @return Whether that chord exists in this key
	 */
	public boolean containsChord(Chord chord) {
		// Check for tonic
		if (tonic.equals(chord)) {
			return true;
		}
		
		// Check all the subdominant chords
		for (int indx = 0; indx < subdominant.length; indx++) {
			if (subdominant[indx].equals(chord)) {
				return true;
			}
		}
		
		// Check all the dominant chords
		for (int indx = 0; indx < dominant.length; indx++) {
			if (dominant[indx].equals(chord)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Given a note, finds the index of the scale array that note belongs to.
	 * 
	 * @param note
	 * 			The note to find
	 * @return The index in the notes array that contains that note
	 * @throws IllegalArgumentException
	 * 			If the note doesn't exist in the scale.
	 */
	protected int findScaleIndex(Note note) throws IllegalArgumentException {
		NoteName noteName = note.getNoteName();
		
		for (int degree = 0; degree < notes.length; degree++) {
			if (notes[degree] == noteName) {
				return degree;
			}
		}
		
		throw new IllegalArgumentException(note + " doesn't exist in this key!");
	}
	
	/**
	 * Sets up all the notes of the major scale.
	 * 
	 * @param rootPitch
	 * 			The root of the scale
	 */
	protected abstract void setUpScale(NoteName rootPitch);
	
	/**
	 * Sets up all the chords for a major scale.
	 * 
	 * @param rootPitch
	 * 			The pitch considered tonic
	 */
	protected abstract void setUpChords(NoteName rootPitch);

}
