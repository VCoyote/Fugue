package exceptions;

/**
 * Thrown for illegal inputs to the GUI
 * 
 * @author Dan Pang
 *
 */
@SuppressWarnings("serial") // ignore
public class InvalidInputException extends Exception {
	
	/**
	 * Default constructor. Does nothing.
	 */
	public InvalidInputException() {
	}
	
	/**
	 * Constructor. Takes a message to be displayed with the exception.
	 * 
	 * @param message
	 * 			A message to be displayed with the exception
	 */
    public InvalidInputException (String message) {
    	super (message);
	}
    
    /**
     * Constructor. Takes a cause of the exception.
     * 
     * @param cause
     * 			What caused the exception to occur
     */
    public InvalidInputException (Throwable cause) {
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
    public InvalidInputException (String message, Throwable cause) {
    	super (message, cause);
	}
}
