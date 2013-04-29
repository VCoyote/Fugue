package notes.scales;

import notes.Chord;
import notes.SecondaryDominantChord;
import notes.types.*;

/**
 * Represents a major key.
 * 
 * @author Dan Pang
 *
 */
public class MajorKey extends Key {
	
	/**
	 * Constructor. Given a root pitch and a scale type, create lists of every
	 * chord that exists in the scale.
	 * 
	 * @param rootPitch
	 * 			The pitch considered tonic
	 * @param scaleType
	 * 			Whether the scale is major or minor
	 */
	public MajorKey(NoteName rootPitch) {
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
		ret[0] = new MajorKey(notes[3]);
		// Plus one sharp
		ret[1] = new MajorKey(notes[4]);
		// Relative minor
		ret[2] = new MinorKey(notes[5]);
		
		return ret;
	}
	
	/**
	 * Sets up all the chords for a major scale.
	 * 
	 * @param rootPitch
	 * 			The pitch considered tonic
	 */
	protected void setUpChords(NoteName rootPitch) {
		// Add the tonic chord.
		// I
		tonic = new Chord(notes[0], ChordType.MAJOR, ChordFunction.TONIC,
				new MajorScale(rootPitch));
		
		// Add the subdominant chords.
		subdominant = new Chord[3];
		// ii
		subdominant[0] = new Chord(notes[1], ChordType.MINOR,
				ChordFunction.SUBDOMINANT, new MajorScale(rootPitch));
		// IV
		subdominant[1] = new Chord(notes[3], ChordType.MAJOR,
				ChordFunction.SUBDOMINANT, new MajorScale(rootPitch));
		// vi
		subdominant[2] = new Chord(notes[5], ChordType.MINOR,
				ChordFunction.SUBDOMINANT, new MajorScale(rootPitch));
		
		// Add the chords to resolve from dominant
		resolution = new Chord[2];
		// I -- authentic
		resolution[0] = tonic;
		// vi -- deceptive
		resolution[1] = subdominant[2];
		
		
		// Add the dominant chords.
		dominant = new Chord[2];
		// V
		dominant[0] = new Chord(notes[4], ChordType.MAJOR,
				ChordFunction.DOMINANT, new MajorScale(rootPitch));
		// vii(dim)
		dominant[1] = new Chord(notes[6], ChordType.DIMINISHED,
				ChordFunction.DOMINANT, new MajorScale(rootPitch));
		
		// Add the secondary dominant chords.
		secondaryDominant = new Chord[3];
		// V/ii
		secondaryDominant[0] = new SecondaryDominantChord(notes[5],
				new MelodicMinorScale(notes[1]), subdominant[0]);
		// V/V
		secondaryDominant[1] = new SecondaryDominantChord(notes[1],
				new MajorScale(notes[4]), dominant[0]);
		// V/vi
		secondaryDominant[2] = new SecondaryDominantChord(notes[2],
				new MelodicMinorScale(notes[5]), subdominant[2]);
	}
	
	/**
	 * Sets up all the notes of the major scale.
	 * 
	 * @param rootPitch
	 * 			The root of the scale
	 */
	protected void setUpScale(NoteName rootPitch) {
		notes = new NoteName[7];
		notes[0] = rootPitch;
		notes[1] = rootPitch.noteAt(2);
		notes[2] = rootPitch.noteAt(4);
		notes[3] = rootPitch.noteAt(5);
		notes[4] = rootPitch.noteAt(7);
		notes[5] = rootPitch.noteAt(9);
		notes[6] = rootPitch.noteAt(11);
	}

}
