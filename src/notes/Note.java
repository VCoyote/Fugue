package notes;

import static controller.Rules.*;
import notes.types.*;

/**
 * The Note class represents a single note and deals with the technicalities
 * of MIDI numbers, etc.
 * 
 * @author Dan Pang
 *
 */
public class Note {
	
	/**
	 * This note's name
	 */
	private NoteName noteName;
	
	/**
	 * The octave of this note
	 */
	private int octave;
	
	/**
	 * The MIDI numerical representation of this note
	 */
	private int midiNumber;
	
	/**
	 * Constructor. Takes a MIDI number and turns it into a Note.
	 * 
	 * @param midiNumber
	 * 			The MIDI number of the note
	 */
	public Note(int midiNumber) {
		this.noteName = NoteName.C.noteAt(midiNumber);
		this.octave = midiNumber / OCTAVE_LENGTH;
		this.midiNumber = midiNumber;
	}
	
	/**
	 * Copy constructor.
	 * 
	 * @param source
	 * 			The Note to copy
	 */
	public Note(Note source) {
		midiNumber = source.getMidiNumber();
		noteName = source.getNoteName();
		octave = source.getOctave();
	}
	
	/**
	 * Checks if the two Notes equal each other.
	 * 
	 * @param otherNote
	 * 			The Note to compare to
	 * @return Whether or not the Notes are equal
	 */
	public boolean equals(Note otherNote) {
		return getMidiNumber() == otherNote.getMidiNumber();
	}
	
	/**
	 * Checks if the two NoteNames are the same.
	 * 
	 * @param otherNote
	 * 			The Note to compare to
	 * @return Whether or not they have the same NoteName
	 */
	public boolean sameNoteAs(Note otherNote) {
		return getNoteName() == otherNote.getNoteName();
	}
	
	/**
	 * Checks if this note is higher than the other note.
	 * 
	 * @param otherNote
	 * 			The Note to compare to
	 * @return Whether this note is higher than the other
	 */
	public boolean greaterThan(Note otherNote) {
		return getMidiNumber() > otherNote.getMidiNumber();
	}
	
	/**
	 * Checks if this note is lower than the other note.
	 * 
	 * @param otherNote
	 * 			The Note to compare to
	 * @return Whether this note is lower than the other
	 */
	public boolean lessThan(Note otherNote) {
		return getMidiNumber() < otherNote.getMidiNumber();
	}
	
	/**
	 * Finds the NoteName.
	 * 
	 * @return This note's NoteName
	 */
	public NoteName getNoteName() {
		return noteName;
	}
	
	/**
	 * Finds the octave.
	 * 
	 * @return This note's octave
	 */
	public int getOctave() {
		return octave;
	}
	
	/**
	 * Finds the MIDI number
	 * 
	 * @return This note's MIDI number
	 */
	public int getMidiNumber() {
		return midiNumber;
	}
	
	/**
	 * Finds this note's lowest octave's MIDI number.
	 * 
	 * @return The MIDI number of this note's lowest octave
	 */
	public int getLowestOctaveMidiNumber() {
		return noteName.getLowestOctaveMidiNumber();
	}
	
	/**
	 * Finds the interval between two notes.
	 * 
	 * @param otherNote
	 * 			The note to compare to
	 * @return The interval between this note and the other note
	 */
	public int getInterval(Note otherNote) {
		return getMidiNumber() - otherNote.getMidiNumber();
	}
	
	/**
	 * Finds the note at a given interval.
	 * 
	 * @param interval
	 * 			The interval to check
	 * @return The note at that interval
	 */
	public Note noteAt(int interval) {
		int newMidiNote = getMidiNumber() + interval;
		
		return new Note(newMidiNote);
	}
}
