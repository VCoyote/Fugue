package notes.scales;

import notes.types.NoteName;

/**
 * Represents a major scale.
 * 
 * @author Dan Pang
 *
 */
public class MajorScale extends Scale {
	
	/**
	 * Constructor. Just calls the superconstructor.
	 * 
	 * @param rootPitch
	 */
	public MajorScale(NoteName rootPitch) {
		super(rootPitch);
	}
	
	/**
	 * Sets up a major scale.
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
