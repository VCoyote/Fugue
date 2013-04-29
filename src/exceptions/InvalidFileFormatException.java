package exceptions;

/**
 * Thrown for illegal file names.
 * 
 * @author Dan Pang
 *
 */
@SuppressWarnings("serial") // ignore
public class InvalidFileFormatException extends Exception {
	
	/**
	 * Default constructor. Does nothing.
	 */
	public InvalidFileFormatException() {
	}
	
	/**
	 * Constructor. Takes a message to be displayed with the exception.
	 * 
	 * @param message
	 * 			A message to be displayed with the exception
	 */
    public InvalidFileFormatException (String message) {
    	super (message);
	}
    
    /**
     * Constructor. Takes a cause of the exception.
     * 
     * @param cause
     * 			What caused the exception to occur
     */
    public InvalidFileFormatException (Throwable cause) {
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
    public InvalidFileFormatException (String message, Throwable cause) {
    	super (message, cause);
	}
}
