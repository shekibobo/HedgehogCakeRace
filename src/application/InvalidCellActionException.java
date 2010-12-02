package application;

import linkedlists.DataStructureException;

/** Class InvalidCellActionException: <br />
 * An exception that indicates a failed cell movement attempt
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class InvalidCellActionException extends DataStructureException {
	/**
	 * gets rid of that stupid warning
	 */
	private static final long serialVersionUID = -291491895475695080L;

	/** Constructor: <br />
	 * @param s error message
	 */
	public InvalidCellActionException(String s) {
		super(s);
	}
}
