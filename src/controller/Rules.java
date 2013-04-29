package controller;

import notes.*;

/**
 * The Rules class is a functor containing the rules of counterpoint.
 * 
 * @author Dan Pang
 *
 */
public class Rules {
	
	public static final int OCTAVE_LENGTH = 12;
	
	private static final int[] DISSONANT_LEAPS = {6, 10, 11};
	
	/**
	 * Checks to make sure the transition to the new note follows
	 * the rules concerning leaps.
	 * 
	 * @param newNote
	 * 			The new note to be added
	 * @param prevNote
	 * 			The previous note in the song
	 * @param prevNoteWasALeap
	 * 			Whether the previous note was a leap from the note before it
	 * @return Whether the transition is valid
	 */
	public static boolean isValidLeap(Note newNote,
			Note prevNote, boolean prevNoteWasALeap) {
		// The interval between the two notes
		int interval = Math.abs(newNote.getInterval(prevNote));
		
		// If the interval is greater than an octave, the new note is invalid.
		if (interval > 12) {
			return false;
		}
		
		// If the leap is a dissonant interval, the new note is invalid.
		if (FugueUtil.valueExists(interval, DISSONANT_LEAPS)) {
			return false;
		}
		
		// If all tests were passed, the new note is valid.
		return true;
	}
	
	/**
	 * Determines whether every chord tone exists in the notes array.
	 * 
	 * @param notes
	 * 			The notes to check
	 * @param chord
	 * 			The chord to compare to
	 * @return Whether the notes array contains at least one of each chord tone
	 */
	public static boolean containsAllChordTones(Note[] notes, Chord chord) {
		// Assume none of these exist, to start.
		boolean root = false;
		boolean third = false;
		boolean fifth = false;
		
		// Iterate through all notes, checking to see what chord tone they are.
		// If a chord tone exists, set its flag to true.
		for (int indx = 0; indx < notes.length; indx++) {
			if (chord.isRoot(notes[indx])) {
				root = true;
			} else if (chord.isThird(notes[indx])) {
				third = true;
			} else if (chord.isFifth(notes[indx])) {
				fifth = true;
			}
		}
		
		// Return true only if all three exist.
		return root && third && fifth;
	}
	
	/**
	 * Determines if there are parallel fifths between the voices.
	 * 
	 * @param currNotes
	 * 			The current beat's notes
	 * @param prevNotes
	 * 			The previous beat's notes
	 * @return Whether or not there are parallel fifths
	 */
	public static boolean noParallelFifths(Note[] currNotes, Note[] prevNotes) {
		// Iterate through each of the prevNotes.
		for (int voice1 = 0; voice1 < prevNotes.length; voice1++) {
			// For each prevNote, once again, iterate through each prevNote.
			for (int voice2 = 0; voice2 < prevNotes.length; voice2++) {
				// If the interval between the two notes is a perfect fifth
				if (Math.abs(prevNotes[voice1].getInterval(
						prevNotes[voice2])) == 7) {
					// If the interval between the same two notes in currNotes
					// is also a perfect fifth
					if (Math.abs(currNotes[voice1].getInterval(
							currNotes[voice2])) == 7) {
						return false;
					} // if
				} // if
			} // for
		} // for
		
		return true;
	}
}
