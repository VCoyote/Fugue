package controller;

import java.util.*;

import notes.*;
import notes.scales.*;
import notes.types.*;

import static controller.FugueUtil.*;
import static controller.ChordGenerator.*;
import exceptions.*;

/**
 * The Song class represents the song, comprised of Beats.
 * 
 * @author Dan Pang
 *
 */
public class Song {
	
	/**
	 * The number of voices.
	 * Currently set to 4 to match SATB 2nd species counterpoint.
	 */
	private static final int NUM_VOICES = 4;
	
	/**
	 * The number of quarter notes per measure
	 */
	private int beatsPerMeasure = 4;
	
	/**
	 * The number of measures in a phrase.
	 */
	private int measuresPerPhrase = 4;
	
	/**
	 * The number of beats in a phrase.
	 */
	private int beatsPerPhrase = beatsPerMeasure * measuresPerPhrase;
	
	/**
	 * The current beat of the phrase.
	 * This resets after each phrase.
	 */
	private int currBeatInPhrase = 0;
	
	/**
	 * The number of beats until the song is allowed to end.
	 */
	private int beatsUntilSongCanEnd;
	
	/**
	 * The tempo, in BPM
	 */
	private float beatsPerMinute;
	
	/**
	 * A list of all the beats
	 */
	private LinkedList<Beat> beats;
	
	/**
	 * Counts the beats left mid-modulation.
	 * Zero if there is no modulating taking place.
	 */
	private int beatsUntilModulation = 0;
	
	/**
	 * The current key the song is writing in
	 */
	private Key currentKey;
	
	/**
	 * The key to modulate to
	 */
	private Key modulationKey;
	
	/**
	 * The random number generator
	 */
	private Random randomizer;
	
	/**
	 * Constructor.
	 * 
	 * @param startingKey
	 * 			The root pitch of the starting key.
	 * @param keyType
	 * 			Whether the key is major or minor
	 * @param tempo
	 * 			The tempo, in BPM
	 * @param minSongLength
	 * 			The shortest the song can be, in seconds
	 */
	public Song(NoteName startingKey, ScaleType keyType, int tempo,
			int minSongLength) {
		beats = new LinkedList<Beat>();
		beatsPerMinute = tempo;
		beatsUntilSongCanEnd = secondsToBeats(minSongLength);
		if (keyType == ScaleType.MAJOR){
			currentKey = new MajorKey(startingKey);
		} else {
			currentKey = new MinorKey(startingKey);
		}
		randomizer = new Random();
	}
	
	/**
	 * Return an array of Beats.
	 * 
	 * @return the array of beats
	 */
	public Beat[] getBeatArray() {
		return convertObjectsToBeats(beats.toArray());
	}
	
	/**
	 * Fetches the song's BPM.
	 * 
	 * @return the song's BPM
	 */
	public float getBPM() {
		return beatsPerMinute;
	}
	
	/**
	 * Fetches the number of voices in this song.
	 * 
	 * @return The number of voices
	 */
	public int getNumVoices() {
		return NUM_VOICES;
	}
	
	/**
	 * Continues adding beats until the song is over.
	 */
	public void generateSong() {
		do {
			addBeat();
		} while (!endOfSong());
	}
	
	/**
	 * Adds a generated beat to the chord.
	 */
	public void addBeat() {
		while (true) {
			Chord newChord = generateNextChord();
			try {
				addBeat(newChord);
			} catch (InvalidChordException ex) {
				continue;
			}
			return;
		}
	}
	
	/**
	 * Adds a new beat to the song with the given chord.
	 * 
	 * @param newChord
	 * 			The chord to be played on the next beat
	 */
	public void addBeat(Chord newChord)
			throws InvalidChordException {
		// Get the previous beat.
		Beat prevBeat = null;
		try {
			prevBeat = beats.getLast();
		} catch (NoSuchElementException ex) {
			// Do nothing; prevBeat is already null
		}
		
		// Create the beat and generate its notes.
		Beat newBeat = new Beat(newChord, prevBeat, NUM_VOICES);
		newBeat.generateNotes();
		
		// Add the beat to the list and increment the beat count.
		beats.add(newBeat);
		currBeatInPhrase ++;
		currBeatInPhrase %= beatsPerPhrase;
		beatsUntilSongCanEnd--;
	}
	
	/**
	 * Finds if the previous beat ended the phrase.
	 * 
	 * @return Whether or not the phrase has ended.
	 */
	public boolean endOfSong() {
		// Get the previous beat.
		Beat prevBeat = null;
		try {
			prevBeat = beats.getLast();
		} catch (NoSuchElementException ex) {
			// If is is the first beat, it's obviously not the end.
			return false;
		}
		
		return currBeatInPhrase == 0
				&& prevBeat.getChord().getFunction() == ChordFunction.TONIC
				&& beatsUntilSongCanEnd <= 0;
	}
	
	/**
	 * Generates the next chord based on what the previous chord was.
	 * 
	 * @return The next chord
	 */
	private Chord generateNextChord() {
		// Get the previous chord of the song.
		Chord prevChord;
		Beat prevBeat;
		try {
			prevBeat = beats.getLast();
			prevChord = prevBeat.getChord();
		} catch (NoSuchElementException ex) {
			// If this is the first chord of the song, it must be tonic.
			return currentKey.getChords(ChordFunction.TONIC)[0];
		}
		
		// If this is the last measure of the phrase, hold out the chord.
		if (beatsUntilLastMeasure() < 0) {
			return prevBeat.getChord();
		}
		
		// Otherwise, proceed as normal.
		ChordFunction prevFunction = prevChord.getFunction();
		Chord ret = null;
		int attempts = 0;
		do {
			switch (prevFunction) {
			case TONIC:
				setUpModulation();
				ret = generateChordOffTonic(
						beatsUntilLastMeasure(), currentKey);
				break;
			case SUBDOMINANT:
				setUpModulation();
				ret = generateChordOffSubdominant(
						beatsUntilLastMeasure(), currentKey);
				break;
			case SECONDARY_DOMINANT:
				setUpModulation();
				ret = generateChordOffSecondaryDominant(
						prevChord);
				break;
			case DOMINANT:
				ret = generateChordOffDominant(
						beatsUntilLastMeasure(), currentKey);
				break;
			default:
				// do nothing
			}
			
			// If it tries to modulate too many times, there probably aren't
			// any possible combinations.
			attempts++;
			if (attempts >= 50) {
				modulationKey = null;
				beatsUntilModulation = 0;
			}
		} while (!fulfilsModulationRequirements(ret));
		
		// Check to see if a modulation is taking place.
		if (beatsUntilModulation > 0) {
			beatsUntilModulation--;
			// If the modulation is over, set the modkey to the currkey
			if (beatsUntilModulation == 0) {
				currentKey = modulationKey;
				modulationKey = null;
			}
		}
			
		return ret;
	}
	
	/**
	 * Finds the number of beats until the last measure of the phrase.
	 * 
	 * @return See above
	 */
	private int beatsUntilLastMeasure() {
		return beatsPerPhrase - 1 - beatsPerMeasure - currBeatInPhrase;
	}
	
	/**
	 * Checks if the newChord fulfills modulation requirements.
	 * 
	 * @param newChord
	 * 			The new chord to check
	 * @return Whether or not that chord continues following the modulation
	 */
	private boolean fulfilsModulationRequirements(Chord newChord) {
		// If there is no modulation, well, keep going
		if (beatsUntilModulation == 0 || modulationKey == null) {
			return true;
		}
		
		// If the modulation key also contains the chord, true.
		if (modulationKey.containsChord(newChord)) {
			return true;
		}
		
		// Otherwise, it fulfilled none of the requirements.
		return false;
	}
	
	/**
	 * Sets up a potential common chord modulation.
	 */
	private void setUpModulation() {
		// Only a 5% chance of happening.
		// 0% if there's already a modulation in place.
		float chance = randomizer.nextFloat();
		if (chance < 0.90 ||
				beatsUntilModulation > 0) {
			return;
		}
		
		// Find a key to modulate to.
		Key[] possibleModulations = currentKey.getPossibleModulations();
		int choice = randomizer.nextInt(possibleModulations.length);
		modulationKey = possibleModulations[choice];
		
		// Have the modulation take place over two beats.
		beatsUntilModulation = 2;
	}
	
	/**
	 * Converts a number of seconds to a number of beats. Rounds up.
	 * 
	 * @param seconds
	 * 			The number of seconds
	 * @return How many beats long that timeframe is
	 */
	private int secondsToBeats(int seconds) {
		float beatsPerSecond = (float) beatsPerMinute / 60;
		return (int) Math.ceil(seconds * beatsPerSecond);
	}
}
