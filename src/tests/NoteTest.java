package tests;

import static org.junit.Assert.*;
import notes.Note;
import notes.types.NoteName;

import org.junit.Test;

/**
 * Unit tests for the Note class.
 * 
 * @author Dan Pang
 *
 */
public class NoteTest {
	
	private static Note note = new Note(60);

	/**
	 * Test the getNoteName function.
	 */
	@Test
	public void testGetNoteName() {
		NoteName result = note.getNoteName();
		assertEquals(NoteName.C, result);
	}
	
	/**
	 * Test the getMidiNote function.
	 */
	@Test
	public void testGetMidiNote() {
		int result = note.getMidiNumber();
		assertEquals(60, result);
	}
	
	/**
	 * Test the getNoteName function.
	 */
	@Test
	public void testGetNoteAt() {
		Note result = note.noteAt(4);
		assertEquals(NoteName.E, result.getNoteName());
		assertEquals(64, result.getMidiNumber());
	}
	
	/**
	 * Test the getNoteName function with a negative interval..
	 */
	@Test
	public void testGetNoteAtNegative() {
		Note result = note.noteAt(-3);
		assertEquals(NoteName.A, result.getNoteName());
		assertEquals(57, result.getMidiNumber());
	}
	
	/**
	 * Test the equals function.
	 */
	@Test
	public void testEquals() {
		Note note2 = new Note(60);
		assertTrue(note2.equals(note));
	}
	
	/**
	 * Test the greaterThan function.
	 */
	@Test
	public void testGreaterThan() {
		Note note2 = new Note(64);
		assertTrue(note2.greaterThan(note));
	}

}
