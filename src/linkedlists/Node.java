package linkedlists;



public class Node<T> {
	
	private T data;
	private Node<T> next;
	
	/**Default Constructor: <br />
	 * initializes data and next references to null
	 */
	public Node () {
		data = null;
		next = null;
	}
	
	/** Overloaded Constructor: <br />
	 * @param item reference to data item
	 * sets next to null
	 */
	public Node (T item) {
		setData(item);
		next = null;
	}
	
	/** Method getNode: <br />
	 * @return copy of player
	 */
	public T getData() {
		return data;
	}
	
	/** Method getNext: <br />
	 * @return next
	 */
	public Node<T> getNext() {
		return next;
	}
	
	/** Method setData: <br />
	 * @param reference to data item
	 */
	public void setData(T item) {
		data = item;
	}
	
	/** Method setNext(): <br />
	 * @param reference to next Node
	 */
	public void setNext(Node<T> nd) {
		next = nd;
	}

}
