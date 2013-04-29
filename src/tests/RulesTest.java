package tests;

import static controller.Rules.*;
import static org.junit.Assert.*;
import notes.*;
import notes.scales.MajorScale;
import notes.types.*;

import org.junit.Test;

/**
 * Unit tests for the Rules functor.
 * 
 * @author Dan Pang
 *
 */
public class RulesTest {

	/**
	 * Test a valid leap.
	 */
	@Test
	public void testValidLeap() {
		Note oldNote = new Note(60);			// C4
		Note newNote = oldNote.noteAt(2);	// D4
		assertTrue(isValidLeap(newNote, oldNote, false));
	}
	
	/**
	 * Test a dissonant leap.
	 */
	@Test
	public void testDissonantLeap() {
		Note oldNote = new Note(60);			// C4
		Note newNote = oldNote.noteAt(6);	// A tritone
		assertFalse(isValidLeap(newNote, oldNote, false));
	}
	
	/**
	 * Test two leaps in a row.
	 */
	@Test
	public void testDoubleLeap() {
		Note oldNote = new Note(60);			// C4
		Note newNote = oldNote.noteAt(5);	// F4
		assertFalse(isValidLeap(newNote, oldNote, true));
	}
	
	/**
	 * Test containsAllChordTones.
	 */
	@Test
	public void testContainsAllChordTones() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] test = {new Note(43), new Note(47), new Note(50)};
		assertTrue(containsAllChordTones(test, chord));
	}
	
	/**
	 * Test containsAllChordTones, without all the chord tones.
	 */
	@Test
	public void testMissingSomeChordTones() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] test = {new Note(47), new Note(50)};
		assertFalse(containsAllChordTones(test, chord));
	}
	
	/**
	 * Test parallelFifths
	 */
	@Test
	public void testParallelFifths() {
		Note[] beat1 = {new Note(0), new Note(2), new Note(7), new Note(8)};
		Note[] beat2 = {new Note(4), new Note(5), new Note(11), new Note(12)};
		assertFalse(noParallelFifths(beat1, beat2));
	}
}
