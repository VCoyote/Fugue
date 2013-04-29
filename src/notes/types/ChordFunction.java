package notes.types;

/**
 * The ChordFunction represents what function a chord fulfills, harmonically.
 * A + signifies augmented.
 * A * signifies diminished.
 * 
 * @author Dan Pang
 *
 */
public enum ChordFunction {
	/**
	 * Major: I
	 * Minor: i
	 */
	TONIC,
	
	/**
	 * For both authentic and deceptive cadences
	 */
	RESOLUTION,
	
	/**
	 * Major: ii, IV, vi
	 * Minor: II, III, iv, IV, v, vi, VI, VII
	 */
	SUBDOMINANT,
	
	/**
	 * Secondary dominants
	 */
	SECONDARY_DOMINANT,
	
	/**
	 * I or i second inversion
	 * Augmented 6th chords
	 */
	PREDOMINANT,
	
	/**
	 * Major: V, vii*
	 * Minor: V, vii*
	 */
	DOMINANT
}
