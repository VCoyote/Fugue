package controller;

import notes.Beat;
import notes.Note;

/**
 * The FugueUtil class is a functor containing utility functions.
 * 
 * I really should have chosen a scripting language instead of Java.
 * 
 * @author Dan Pang
 *
 */
public class FugueUtil {
	
	/**
	 * Converts an array of Objects into an array of Beats
	 * 
	 * @param in
	 * 			An array of Objects
	 * @return An array of Beats
	 */
	public static Beat[] convertObjectsToBeats(Object[] in) {
		Beat[] ret = new Beat[in.length];
		for (int indx = 0; indx < in.length; indx++) {
			ret[indx] = (Beat) in[indx];
		}
		
		return ret;
	}
	
	/**
	 * Given an array of Notes, finds the Note that is the closest to
	 * the given Note.
	 * 
	 * @param note
	 * 			The Note to compare to
	 * @param array
	 * 			The array of Notes to search
	 * @return The Note in the array that is closest to the given Note
	 */
	public static int findNoteIndxClosestTo(Note note, Note[] array) {
		int distance = 0;
		int ret = 0;
		
		for (int noteIndx = 0; noteIndx < array.length; noteIndx++) {
			int interval = Math.abs(note.getInterval(array[noteIndx]));
			if (interval < distance) {
				ret = noteIndx;
				distance = interval;
			}
		}
		
		return ret;
	}
	
	/**
	 * Resizes an array of Notes
	 * 
	 * @param source
	 * 			The source array
	 * @param newSize
	 * 			The size to resize it to
	 * @return The resized array
	 */
	public static Note[] resizeArray(Note[] source, int newSize) {
		Note[] ret = new Note[newSize];
		System.arraycopy(source, 0, ret, 0, Math.min(source.length, newSize));
		return ret;
	}
	
	/**
	 * Merge two sorted arrays into a sorted array.
	 * Used to merge the three separate note arrays into a list of all the
	 * notes.
	 * 
	 * @param arr1
	 * 			The first array
	 * @param arr2
	 * 			The second array
	 * @return The two arrays, merged and sorted
	 */
	public static Note[] merge(Note[] arr1, Note[] arr2) {
		// Set up the return index and all the counting variables
		Note[] ret = new Note[arr1.length + arr2.length];
		int retIndx = 0;
		int indx1 = 0;
		int indx2 = 0;
		
		// Loop through the arrays until one of them runs out,
		// adding the lower number to the return array.
		while (indx1 < arr1.length && indx2 < arr2.length) {
			if (arr1[indx1].lessThan(arr2[indx2])) {
				ret[retIndx] = arr1[indx1];
				indx1++;
			} else {
				ret[retIndx] = arr2[indx2];
				indx2++;
			}
			retIndx++;
		}
		
		// Add all the un-added numbers to the return array.
		while (indx1 < arr1.length) {
			ret[retIndx] = arr1[indx1];
			indx1++;
			retIndx++;
		}
		while (indx2 < arr2.length) {
			ret[retIndx] = arr2[indx2];
			indx2++;
			retIndx++;
		}
		
		return ret;
	}
	
	/**
	 * Assert that two arrays are equal.
	 * 
	 * @param a
	 * 			First array
	 * @param b
	 * 			Second array
	 * @return Whether or not they are equal
	 */
	public static boolean arraysAreEqual(Note[] a, Note[] b) {
		if (a.length != b.length) {
			return false;
		}
		
		for (int x = 0; x < a.length; x++) {
			if (!a[x].equals(b[x])) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Removes an element from the array.
	 * 
	 * @param index
	 * 			The index of an element to remove
	 * @param array
	 * 			The array to remove from
	 * @return A new array with that index removed
	 */
	public static Note[] removeElement(int index, Note[] array) {
		// If the index is out of bounds, just return the same array.
		if (index < 0 || index >= array.length) {
			return array.clone();
		}
		
		// Set up the return index
		Note[] ret = new Note[array.length - 1];
		
		// Iterate through the old array, copying values over.
		int newIndx = 0;
		for (int oldIndx = 0; oldIndx < array.length; oldIndx++) {
			if (oldIndx != index) {
				ret[newIndx] = new Note(array[oldIndx]);
				newIndx++;
			}
		}
		
		return ret;
	}
	
	/**
	 * Finds the higher of the two notes.
	 * 
	 * @param note1
	 * @param note2
	 * @return
	 */
	public static Note max(Note note1, Note note2) {
		if (note1.greaterThan(note2)) {
			return note1;
		}
		return note2;
	}
	
	/**
	 * Finds the highest of the three notes.
	 * 
	 * @param note1
	 * @param note2
	 * @param note3
	 * @return
	 */
	public static Note max(Note note1, Note note2, Note note3) {
		return max(max(note1, note2), note3);
	}
	
	/**
	 * Finds the lower of the two notes.
	 * 
	 * @param note1
	 * @param note2
	 * @return
	 */
	public static Note min(Note note1, Note note2) {
		if (note1.lessThan(note2)) {
			return note1;
		}
		
		return note2;
	}
	
	/**
	 * Finds the highest of the three notes.
	 * 
	 * @param note1
	 * @param note2
	 * @param note3
	 * @return
	 */
	public static Note min(Note note1, Note note2, Note note3) {
		return min(min(note1, note2), note3);
	}
	
	/**
	 * Check if a value exists in an array.
	 * 
	 * @param value
	 * 			The value to search for
	 * @param array
	 * 			The array to search
	 * @return Whether the value exists in the array
	 */
	public static boolean valueExists(int value, int[] array) {
		// Iterate through every element and see if any equal the value.
		for (int indx = 0; indx < array.length; indx++) {
			// If there's a match, return true.
			if (array[indx] == value) {
				return true;
			}
		}
		
		// If there's no match, return false.
		return false;
	}
}
