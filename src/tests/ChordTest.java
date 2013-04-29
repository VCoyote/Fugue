package tests;

import static controller.FugueUtil.*;
import static org.junit.Assert.*;
import notes.*;
import notes.scales.MajorScale;
import notes.types.*;

import org.junit.*;

/**
 * Unit tests for the Chord class.
 * 
 * @author Dan Pang
 *
 */
public class ChordTest {
	
	/**
	 * Test the creation of a major chord.
	 */
	@Test
	public void testMajorChord() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] allNotes = chord.getAllNotes();
		Note[] expected = {new Note(43), new Note(47), new Note(50),
				new Note(55), new Note(59), new Note(62), new Note(67),
				new Note(71), new Note(74)};
		assertTrue(arraysAreEqual(allNotes, expected));
	}
	
	/**
	 * Test the creation of a minor chord.
	 */
	@Test
	public void testMinorChord() {
		Chord chord = new Chord(NoteName.G, ChordType.MINOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] allNotes = chord.getAllNotes();
		Note[] expected = {new Note(43), new Note(46), new Note(50),
				new Note(55), new Note(58), new Note(62), new Note(67),
				new Note(70), new Note(74)};
		assertTrue(arraysAreEqual(allNotes, expected));
	}
	
	/**
	 * Test the creation of an augmented chord.
	 */
	@Test
	public void testAugmentedChord() {
		Chord chord = new Chord(NoteName.G, ChordType.AUGMENTED,
				ChordFunction.SUBDOMINANT, new MajorScale(NoteName.G));
		Note[] allNotes = chord.getAllNotes();
		Note[] expected = {new Note(43), new Note(47), new Note(51),
				new Note(55), new Note(59), new Note(63), new Note(67),
				new Note(71), new Note(75)};
		assertTrue(arraysAreEqual(allNotes, expected));
	}
	
	/**
	 * Test the creation of a diminished chord.
	 */
	@Test
	public void testDiminishedChord() {
		Chord chord = new Chord(NoteName.G, ChordType.DIMINISHED,
				ChordFunction.DOMINANT, new MajorScale(NoteName.G));
		Note[] allNotes = chord.getAllNotes();
		Note[] expected = {new Note(43), new Note(46), new Note(49),
				new Note(55), new Note(58), new Note(61), new Note(67),
				new Note(70), new Note(73)};
		assertTrue(arraysAreEqual(allNotes, expected));
	}
	
	/**
	 * Test the equals function with another Chord.
	 */
	@Test
	public void testEquals() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Chord otherChord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.DOMINANT, new MajorScale(NoteName.C));
		assertTrue(chord.equals(otherChord));
	}
	
	/**
	 * Test the isInChord function.
	 */
	@Test
	public void testIsInChord() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] expected = {new Note(43), new Note(47), new Note(50),
				new Note(55), new Note(59), new Note(62), new Note(67),
				new Note(71), new Note(74)};
		for (int indx = 0; indx < expected.length; indx++) {
			assertTrue(chord.isInChord(expected[indx]));
		}
	}
	
	/**
	 * Test the getNextHighestNote function with a random input note.
	 */
	@Test
	public void testNextHighestNote() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note result = chord.getNextHighestNote(new Note(44));
		assertTrue(new Note(47).equals(result));
	}
	
	/**
	 * Test the getNextHighestNote function with an input note that is also a part of the chord.
	 */
	@Test
	public void testNextHighestNoteWithChordTone() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note result = chord.getNextHighestNote(new Note(43));
		assertTrue(new Note(47).equals(result));
	}
	
	/**
	 * Test the getAllNotesBetween function
	 */
	@Test
	public void testGetAllNotesBetween() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] result = chord.getAllNotesBetween(
				new Range(new Note(44), new Note(56)));
		Note[] expected = {new Note(47), new Note(50), new Note(55)};
		assertTrue(arraysAreEqual(expected, result));
	}
	
	/**
	 * Test the getAllNotesBetween function where the lower bound is a chord tone
	 */
	@Test
	public void testGetAllNotesBetweenWithChordToneBottom() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] result = chord.getAllNotesBetween(
				new Range(new Note(47), new Note(56)));
		Note[] expected = {new Note(47), new Note(50), new Note(55)};
		assertTrue(arraysAreEqual(expected, result));
	}
	
	/**
	 * Test the getAllNotesBetween function where the upper bound is a chord tone
	 */
	@Test
	public void testGetAllNotesBetweenWithChordToneTop() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] result = chord.getAllNotesBetween(
				new Range(new Note(44), new Note(55)));
		Note[] expected = {new Note(47), new Note(50), new Note(55)};
		assertTrue(arraysAreEqual(expected, result));
	}
	
	/**
	 * Test the getAllRootsBetween function.
	 */
	@Test
	public void testGetAllRootsBetween() {
		Chord chord = new Chord(NoteName.G, ChordType.MAJOR,
				ChordFunction.TONIC, new MajorScale(NoteName.G));
		Note[] result = chord.getAllRootsBetween(
				new Range(new Note(44), new Note(55)));
		Note[] expected = {new Note(55)};
		assertTrue(arraysAreEqual(expected, result));
		
	}
}
