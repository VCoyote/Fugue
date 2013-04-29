package tests;

import static org.junit.Assert.*;

import notes.types.NoteName;

import org.junit.Test;

/**
 * Unit tests for the NoteName enum.
 * 
 * @author Dan Pang
 *
 */
public class NoteNameTest {
	
	/**
	 * Tests getMidiNote.
	 */
	@Test
	public void testGetMidiNote() {
		assertEquals(0, NoteName.C.getLowestOctaveMidiNumber());
		assertEquals(1, NoteName.Csharp.getLowestOctaveMidiNumber());
		assertEquals(11, NoteName.B.getLowestOctaveMidiNumber());
	}
	
	/**
	 * Tests representedBy
	 */
	@Test
	public void testRepresentation() {
		assertTrue(NoteName.C.representedBy(0));
		assertTrue(NoteName.C.representedBy(60));
		assertFalse(NoteName.C.representedBy(34));
	}
	
	/**
	 * Tests getInterval with a positive interval
	 */
	@Test
	public void testGetIntervalAbove() {
		NoteName note = NoteName.C;
		NoteName real = note.noteAt(4);
		assertEquals(NoteName.E, real);
	}
	
	/**
	 * Tests getInterval with a negative interval
	 */
	@Test
	public void testGetIntervalBelow() {
		NoteName note = NoteName.C;
		NoteName real = note.noteAt(-4);
		assertEquals(NoteName.Gsharp, real);
	}
	
	/**
	 * Tests the getNoteNameFromMidiNumber function
	 */
	@Test
	public void testGetNoteNameFromMidi() {
		NoteName real = NoteName.getNoteNameFromMidiNumber(2);
		assertEquals(NoteName.D, real);
	}

}
