package notes;

import notes.scales.Scale;
import notes.types.*;

/**
 * The SecondaryDominantChord class represents a secondary dominant chord.
 * It fills out the leadsTo() function in the Chord class.
 * 
 * @author Dan Pang
 *
 */
public class SecondaryDominantChord extends Chord {
	
	private Chord leadsTo;
	
	public SecondaryDominantChord(NoteName rootOfChord, Scale scale,
			Chord leadsTo) {
		
		super(rootOfChord, ChordType.MAJOR,
				ChordFunction.SECONDARY_DOMINANT, scale);
		this.leadsTo = leadsTo;
	}
	
	public Chord leadsTo() {
		return leadsTo;
	}

}
