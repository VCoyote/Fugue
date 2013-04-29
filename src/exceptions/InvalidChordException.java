package exceptions;

/**
 * The InvalidChordException is raised whenever an invalid chord is selected
 * as the next chord.
 * 
 * @author Dan Pang
 *
 */
@SuppressWarnings("serial") // ignore
public class InvalidChordException extends Exception {
	
	/**
	 * Default constructor. Does nothing.
	 */
	public InvalidChordException() {
	}
	
	/**
	 * Constructor. Takes a message to be displayed with the exception.
	 * 
	 * @param message
	 * 			A message to be displayed with the exception
	 */
    public InvalidChordException (String message) {
    	super (message);
	}
    
    /**
     * Constructor. Takes a cause of the exception.
     * 
     * @param cause
     * 			What caused the exception to occur
     */
    public InvalidChordException (Throwable cause) {
    	super (cause);
	}
    
    /**
     * Constructor. Takes both a message and a cause.
     * 
     * @param message
	 * 			A message to be displayed with the exception
     * @param cause
     * 			What caused the exception to occur
     */
    public InvalidChordException (String message, Throwable cause) {
    	super (message, cause);
	}

}
