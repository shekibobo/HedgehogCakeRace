package linkedlists;

public abstract class ShellLinkedList<T> {
	protected Node<T> head;
	protected Node<T> next;
	protected int numberOfItems;
	
	/** Default Constructor: <br />
	 * sets head to null and number of items to 0
	 */
	public ShellLinkedList() {
		head = null;
		next = null;
		numberOfItems = 0;
	}
	
	/** Method getNumberOfItems: <br />
	 * @return numberOfItems
	 */
	public int getNumberOfItems() {
		return numberOfItems;
	}
	
	/** Method isEmpty: <br />
	 * @return true if no items in list; false otherwise
	 */
	public boolean isEmpty() {
		return (numberOfItems == 0);
	}
	
	/** Method toString(): <br />
	 * @return the contents of the list
	 */
	public String toString() {
		String listString = "";
		Node<T> current = head;
		for (int i = 0; i < numberOfItems; i++) {
			listString += current.getData().toString() + "\n";
			current = current.getNext();
		}
		return listString;
	}
	
}
