package linkedlists;

/** Class StackLinkedList<T>: <br />
 * creates a generic stack extended from the generic linked list.
 * Provides methods for push, pop, and peek.
 * @author Joshua Kovach
 * @version 1.0
 *
 * @param <T>
 */
public class StackLinkedList<T> extends ShellLinkedList<T> {
	public StackLinkedList() {
		super();
	}
	
	/** Method push: <br />
	 * @param item to insert
	 */
	public void push(T item) {
		Node<T> newItem = new Node<T>(item);
		newItem.setNext(head);
		head = newItem;
		numberOfItems++;
	}
	
	/** Method pop: <br />
	 * @return the item deleted
	 */
	public T pop() throws DataStructureException {
		if (isEmpty())
			throw new DataStructureException("empty stack: cannot be popped");
		else {
			T deleted = head.getData();
			head = head.getNext();
			numberOfItems--;
			return deleted;
		}
	}
	
	/** Method Peek: <br />Object
	 * @return the item T retrieved
	 */
	public T peek() throws DataStructureException {
		if (isEmpty())
			throw new DataStructureException("empty stack: cannot peek");
		else {
			return head.getData();
		}
	}
}
