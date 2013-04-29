package tests;

import static controller.FugueUtil.*;
import static org.junit.Assert.*;

import notes.Note;

import org.junit.Test;


/**
 * Unit tests for the FugueUtil functor.
 * @author Dan Pang
 *
 */
public class UtilTest {
	
	@Test
	public void testEqualArrays() {
		Note[] a = {new Note(1), new Note(2), new Note(3)};
		Note[] b = {new Note(1), new Note(2), new Note(3)};
		assertTrue(arraysAreEqual(a, b));
	}
	
	@Test
	public void testUnequalArrays() {
		Note[] a = {new Note(1), new Note(3), new Note(2)};
		Note[] b = {new Note(1), new Note(2), new Note(3)};
		assertFalse(arraysAreEqual(a, b));
	}
	
	@Test
	public void testUnequalArraysOfDifferentLength() {
		Note[] a = {new Note(1), new Note(2), new Note(3)};
		Note[] b = {new Note(1), new Note(2), new Note(3), new Note(4)};
		assertFalse(arraysAreEqual(a, b));
	}

	/**
	 * Test merging two arrays of equal length.
	 */
	@Test
	public void testMergeEqualLengths() {
		Note[] a = {new Note(1), new Note(4), new Note(6)};
		Note[] b = {new Note(2), new Note(3), new Note(5)};
		Note[] result = merge(a, b);
		Note[] expected = {new Note(1), new Note(2), new Note(3), new Note(4),
				new Note(5), new Note(6)};
		assertTrue(arraysAreEqual(expected, result));
	}
	
	/**
	 * Test merging two arrays of unequal length.
	 */
	@Test
	public void testMergeUnequalLenths() {
		Note[] a = {new Note(1), new Note(4), new Note(6), new Note(7)};
		Note[] b = {new Note(2), new Note(3), new Note(5)};
		Note[] result = merge(a, b);
		Note[] expected = {new Note(1), new Note(2), new Note(3), new Note(4),
				new Note(5), new Note(6), new Note(7)};
		assertTrue(arraysAreEqual(expected, result));
	}
	
	/**
	 * Test merging two arrays with repeated numbers.
	 */
	@Test
	public void testMergeRepeatedNumbers() {
		Note[] a = {new Note(1), new Note(4), new Note(6)};
		Note[] b = {new Note(2), new Note(4), new Note(5)};
		Note[] result = merge(a, b);
		Note[] expected = {new Note(1), new Note(2), new Note(4), new Note(4),
				new Note(5), new Note(6)};
		assertTrue(arraysAreEqual(expected, result));
	}
	
	/**
	 * Test valueExists if the value exists.
	 */
	@Test
	public void testValueExists() {
		int[] tester = {1, 2, 3, 4, 5};
		int testValue = 4;
		assertTrue(valueExists(testValue, tester));
	}
	
	/**
	 * Test valueExists if the value doesn't exist.
	 */
	@Test
	public void testValueDoesntExist() {
		int[] tester = {1, 2, 3, 5};
		int testValue = 4;
		assertFalse(valueExists(testValue, tester));
	}
	
	/**
	 * Test removeElement.
	 */
	@Test
	public void testRemoveElement() {
		Note[] tester = {new Note(1), new Note(2), new Note(3), new Note(4),
				new Note(5)};
		Note[] result = removeElement(3, tester);
		Note[] expected = {new Note(1), new Note(2), new Note(3), new Note(5)};
		assertTrue(arraysAreEqual(expected, result));
	}
}
