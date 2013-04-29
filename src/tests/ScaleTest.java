package tests;

import static org.junit.Assert.*;

import notes.*;
import notes.scales.MajorScale;
import notes.scales.MelodicMinorScale;
import notes.scales.Scale;
import notes.types.*;

import org.junit.Test;

/**
 * Unit tests for the Scale class and all its subclasses.
 * @author Dan Pang
 *
 */
public class ScaleTest {
	
	/**
	 * Test getUpperNeighbor
	 */
	@Test
	public void testUpperNeighbor() {
		Scale scale = new MajorScale(NoteName.C);
		Note result = scale.getUpperNeighbor(new Note(60));
		assertTrue(new Note(62).equals(result));
	}
	
	/**
	 * Test getLowerNeighbor
	 */
	@Test
	public void testLowerNeighbor() {
		Scale scale = new MajorScale(NoteName.C);
		Note result = scale.getLowerNeighbor(new Note(60));
		assertTrue(new Note(59).equals(result));
	}
	
	/**
	 * Test getPassingTone with a major key
	 */
	@Test
	public void testPassingToneMajor() {
		Scale scale = new MajorScale(NoteName.C);
		Note result = scale.getPassingTone(new Note(60), new Note(64));
		assertTrue(new Note(62).equals(result));
	}
	
	/**
	 * Test getPassingTone with a minor key
	 */
	@Test
	public void testPassingToneMinor() {
		Scale scale = new MelodicMinorScale(NoteName.C);
		Note result = scale.getPassingTone(new Note(60), new Note(63));
		assertTrue(new Note(62).equals(result));
		
		result = scale.getPassingTone(new Note(67), new Note(71));
		assertTrue(new Note(69).equals(result));
	}
}
