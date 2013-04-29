package notes.scales;

import notes.Note;
import notes.types.NoteName;

/**
 * Represents a diatonic scale and all the notes that exist in it.
 * 
 * @author Dan Pang
 *
 */
public abstract class Scale {
	
	/**
	 * An array of NoteNames representing the scale.
	 */
	protected NoteName[] notes;
	
	/**
	 * Constructor. Builds the scale off the root pitch.
	 * 
	 * @param rootPitch
	 * 			The root note of the scale.
	 */
	public Scale(NoteName rootPitch) {
		// Check for valid input
		if (rootPitch == null) {
			throw new IllegalArgumentException();
		}
		
		setUpScale(rootPitch);
	}
	
	/**
	 * Returns whether or not the note exists in this key.
	 * 
	 * @param note
	 * 			The note to check for
	 * @return Whether or not the note exists in the key
	 */
	public boolean existsInScale(NoteName note) {
		// Check through each note in the scale.
		for (int degree = 0; degree < notes.length; degree++) {
			// If there's a match, return true.
			if (notes[degree] == note) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Given a note (in MIDI number form), finds the note a step up from it.
	 * 
	 * @param note
	 * 			The note to find a neighbor of
	 * @return The neighboring note up the scale
	 */
	public Note getUpperNeighbor(Note note) {
		// Go up the chromatic scale from the note until you find the next
		// note that exists in the key.
		for (int interval = 1; true; interval++) {
			Note newNote = note.noteAt(interval);
			if (existsInScale(newNote.getNoteName())) {
				return newNote;
			}
		}
	}
	
	/**
	 * Given a note (in MIDI number form), finds the note a step down from it.
	 * 
	 * @param note
	 * 			The note to find a neighbor of
	 * @return The neighboring note down the scale
	 */
	public Note getLowerNeighbor(Note note) {
		// Go up the chromatic scale from the note until you find the next
		// note that exists in the key.
		for (int interval = -1; true; interval--) {
			Note newNote = note.noteAt(interval);
			if (existsInScale(newNote.getNoteName())) {
				return newNote;
			}
		}
	}
	
	/**
	 * Finds the passing tone between two notes.
	 * 
	 * @param lowerNote
	 * 			The lower pitch
	 * @param higherNote
	 * 			The higher pitch
	 * @return The passing tone between them
	 * @throws IllegalArgumentException
	 * 			If either note doesn't exist within the scale, or if the
	 * 			two notes aren't a third apart
	 */
	public Note getPassingTone(Note lowerNote, Note higherNote)
			throws IllegalArgumentException {
		int lowerDegree = findScaleIndex(lowerNote);
		int higherDegree = findScaleIndex(higherNote);
		
		// If the two notes don't have a scale degree between them, it's a bad
		// input.
		if (higherDegree - lowerDegree != 2) {
			throw new IllegalArgumentException("No passing tone exists between " +
					"these notes!");
		}
		
		return getUpperNeighbor(lowerNote);
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
	 * Set up the scale, depending on the type of scale.
	 * 
	 * @param rootPitch
	 * 			The root pitch of the scale
	 */
	protected abstract void setUpScale(NoteName rootPitch);
}
