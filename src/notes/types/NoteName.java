package notes.types;

/**
 * Represents a note in a scale.
 * 
 * @author Dan Pang
 *
 */
public enum NoteName {
	C (0),
	Csharp (1),
	D (2),
	Dsharp (3),
	E (4),
	F (5),
	Fsharp (6),
	G (7),
	Gsharp (8),
	A (9),
	Asharp (10),
	B (11);
	
	/**
	 * A MIDI note number for this note
	 */
	private int exampleMidiNote;
	
	/**
	 * Constructor. Takes the NoteName and stores an example MIDI note.
	 * 
	 * @param midiNote
	 * 			A MIDI note corresponding to this pitch.
	 */
	private NoteName(int midiNote) {
		exampleMidiNote = midiNote;
	}
	
	/**
	 * Converts a NoteName to a MIDI note number.
	 * 
	 * @return A MIDI number representing that note
	 */
	public int getLowestOctaveMidiNumber() {
		return exampleMidiNote;
	}
	
	/**
	 * Tests whether the integer given is a representation of this note.
	 * 
	 * @param test
	 * 			The MIDI integer value
	 * @return Whether that integer represents this note
	 */
	public boolean representedBy(int test) {
		return (test % 12) == exampleMidiNote;
	}
	
	/**
	 * Finds the note an interval above (or below) this one.
	 * 
	 * @param interval
	 * 			A positive or negative value representing the interval between
	 * 			this note and the one you wish to find.
	 * @return The NoteName of that note.
	 */
	public NoteName noteAt(int interval) {
		// First, get this MIDI number.
		int thisNote = getLowestOctaveMidiNumber();
		// Workaround for negative numbers:
		// Find the modulus of the interval and add 12, ensuring it will always
		// be a positive interval.
		int modInterval = (interval % 12) + 12;
		// Find the correct note, and ensure it's always below 12.
		int retNote = (thisNote + modInterval) % 12;
		
		// Iterate through all the NoteNames to find the NoteName equaling
		// retNote.
		for (NoteName testNote : NoteName.values()) {
			if (testNote.getLowestOctaveMidiNumber() == retNote) {
				return testNote;
			}
		}
		// If, for some reason, the interval doesn't exist (which is
		// impossible), something clearly went wrong.
		throw new IllegalArgumentException(interval + " doesn't exist!");
	}
	
	/**
	 * Finds the NoteName at that given midiNumber.
	 * 
	 * @param midiNumber
	 * 			The note to check for
	 * @return The NoteName of that note
	 */
	public static NoteName getNoteNameFromMidiNumber(int midiNumber) {
		return NoteName.C.noteAt(midiNumber);
	}
}
