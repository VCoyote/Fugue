package notes;

/**
 * A simple class to represent the range of a voice.
 * 
 * @author Dan Pang
 *
 */
public class Range {
	/**
	 * The lowest note
	 */
	public Note lower;
	
	/**
	 * The highest note
	 */
	public Note higher;
	
	/**
	 * Constructor. Sets lower and higher.
	 * 
	 * @param lower
	 * 			The lowest note
	 * @param higher
	 * 			The highest note
	 */
	public Range(Note lower, Note higher) {
		this.lower = lower;
		this.higher = higher;
	}
}
