package application;

import linkedlists.DataStructureException;
import linkedlists.StackLinkedList;
import linkedlists.Node;

/** Class GameBoardCell: <br />
 * Creates a Stack Linked List of type PlayerToken
 * @author Joshua Kovach
 * @version 1.0
 *
 */
public class GameBoardCell extends StackLinkedList<PlayerToken> {
	private boolean enabled;
	private int pitMode;
	
	/** Constructor: <br />
	 * Creates a cell that contains a stack of PlayerTokens
	 * that is either enabled or disabled
	 * @param enabled - true if enabled, false if disabled
	 */
	public GameBoardCell(boolean enabled, int pitType) {
		super();
		setEnabled(enabled);
		setPitMode(pitType);
	}
	
	/** Method setEnabled: <br />
	 * Enables or disables the cell
	 * @param enabledOption - true to enable, false to disable
	 */
	public void setEnabled(boolean enabledOption) {
		enabled = enabledOption;
	}
	
	/** Method isEnabled: <br />
	 * @return - true if enabled, false if disabled
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/** Method setPitMode: <br />
	 * Sets the pit mode to 
	 * @param pitType PITS(0), BLACK_HOLES(1), WORM_HOLES(2), or ORIGINS(3)
	 */
	public void setPitMode(int pitType) {
		pitMode = pitType;
	}
	
	/** Method getPitMode: <br />
	 * @return int PITS(0), BLACK_HOLES(1), WORM_HOLES(2), or ORIGINS(3)
	 */
	public int getPitMode() {
		return pitMode;
	}
	
	/** Method pop: <br />
	 * @return the item deleted
	 */
	public PlayerToken pop() throws DataStructureException {
		if (enabled) {
			return super.pop();
		}
		else throw new InvalidCellActionException("cell is not enabled");
	}
	
	/** Method peekNext: <br />
	 * Allows the board to see each item in the stack.  Must use getData()
	 * method to access the data contained in the node.
	 * @return
	 * @throws DataStructureException
	 */
	public Node<PlayerToken> peekNext() throws DataStructureException {
		if (isEmpty())
			throw new DataStructureException("empty stack: cannot peek");
		else return head.getNext();
	}
}
