package notes.scales;

import notes.Chord;
import notes.SecondaryDominantChord;
import notes.types.*;

/**
 * Represents a minor key.
 * 
 * @author Dan Pang
 *
 */
public class MinorKey extends Key {
	
	/**
	 * Constructor. Given a root pitch and a scale type, create lists of every
	 * chord that exists in the scale.
	 * 
	 * @param rootPitch
	 * 			The pitch considered tonic
	 * @param scaleType
	 * 			Whether the scale is major or minor
	 */
	public MinorKey(NoteName rootPitch) {
		super(rootPitch);
	}
	
	/**
	 * Get a list of keys that you can modulate to from this one.
	 * 
	 * @return See above
	 */
	public Key[] getPossibleModulations() {
		Key[] ret = new Key[3];
		// Plus one flat
		ret[0] = new MinorKey(notes[3]);
		// Plus one sharp
		ret[1] = new MinorKey(notes[4]);
		// Relative major
		ret[2] = new MajorKey(notes[2]);
		
		return ret;
	}
	
	/**
	 * Sets up all the chords for a minor scale.
	 * 
	 * @param rootPitch
	 * 			The pitch considered tonic
	 */
	protected void setUpChords(NoteName rootPitch) {
		// Add the tonic chord.
		// i
		tonic = new Chord(notes[0], ChordType.MINOR, ChordFunction.TONIC,
				new MelodicMinorScale(rootPitch));
		
		// Add the subdominant chords.
		subdominant = new Chord[5];
		// ii(dim)
		subdominant[0] = new Chord(notes[1], ChordType.DIMINISHED,
				ChordFunction.SUBDOMINANT, new NaturalMinorScale(rootPitch));
		// iv
		subdominant[1] = new Chord(notes[3], ChordType.MINOR,
				ChordFunction.SUBDOMINANT, new NaturalMinorScale(rootPitch));
		// v
		subdominant[2] = new Chord(notes[4], ChordType.MINOR,
				ChordFunction.SUBDOMINANT, new NaturalMinorScale(rootPitch));
		// VI
		subdominant[3] = new Chord(notes[5], ChordType.MAJOR,
				ChordFunction.SUBDOMINANT, new NaturalMinorScale(rootPitch));
		// VII
		subdominant[4] = new Chord(notes[7], ChordType.MAJOR,
				ChordFunction.SUBDOMINANT, new NaturalMinorScale(rootPitch));
		
		// Add the chords to resolve from dominant
		resolution = new Chord[3];
		// i -- authentic cadence
		resolution[0] = tonic;
		// VI -- deceptive cadence
		resolution[1] = subdominant[3];
		// I -- Picardy third
		resolution[2] = new Chord(notes[0], ChordType.MAJOR, ChordFunction.TONIC,
				new MajorScale(rootPitch));
		
		// Add the dominant chords.
		dominant = new Chord[2];
		// V
		dominant[0] = new Chord(notes[4], ChordType.MAJOR,
				ChordFunction.DOMINANT, new MelodicMinorScale(rootPitch));
		// vii(dim)
		dominant[1] = new Chord(notes[8], ChordType.DIMINISHED,
				ChordFunction.DOMINANT, new MelodicMinorScale(rootPitch));
		
		// Add the secondary dominant chords.
		secondaryDominant = new Chord[3];
		// V/iv
		secondaryDominant[0] = new SecondaryDominantChord(notes[0],
				new MelodicMinorScale(notes[3]), subdominant[1]);
		// V/v
		secondaryDominant[1] = new SecondaryDominantChord(notes[1],
				new MelodicMinorScale(notes[4]), subdominant[2]);
		// V/V
		secondaryDominant[1] = new SecondaryDominantChord(notes[1],
				new MajorScale(notes[4]), dominant[0]);
		// V/VI
		secondaryDominant[2] = new SecondaryDominantChord(notes[2],
				new MajorScale(notes[5]), subdominant[3]);
		// V/VII
		secondaryDominant[2] = new SecondaryDominantChord(notes[3],
				new MajorScale(notes[7]), subdominant[4]);
	}
	
	/**
	 * Sets up all the notes of the major scale.
	 * 
	 * @param rootPitch
	 * 			The root of the scale
	 */
	protected void setUpScale(NoteName rootPitch) {
		notes = new NoteName[9];
		notes[0] = rootPitch;
		notes[1] = rootPitch.noteAt(2);
		notes[2] = rootPitch.noteAt(3);
		notes[3] = rootPitch.noteAt(5);
		notes[4] = rootPitch.noteAt(7);
		notes[5] = rootPitch.noteAt(8);
		notes[6] = rootPitch.noteAt(9);
		notes[7] = rootPitch.noteAt(10);
		notes[8] = rootPitch.noteAt(11);
	}

}
