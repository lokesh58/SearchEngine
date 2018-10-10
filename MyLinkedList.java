import java.util.Iterator;

public class MyLinkedList<T> implements Iterable<T> {
	class Node {
		T _data;
		Node _next;
		Node(T data) {
			_data = data;
			_next = null;
		}
	}

	private Node _front, _rear;
	private int _size;

	MyLinkedList() {
		_front = _rear = null;
		_size = 0;
	}

	public int size() {
		return _size;
	}

	public boolean isEmpty() {
		return _front == null;
	}

	public boolean isMember(T item) {
		Node curr = _front;
		while (curr != null) {
			if (curr._data.equals(item)) {
				return true;
			}
			curr = curr._next;
		}
		return false;
	}

	public T getAt(int i) throws Exception {
		if (i < 0 || i >= _size) {
			throw new Exception("Error - "+i+"th item not in list");
		} else {
			Node curr = _front;
			while (--i >= 0) {
				curr = curr._next;
			}
			return curr._data;
		}
	}

	public void insertFront(T item) {
		if (_front == null) {
			_front = _rear = new Node(item);
		} else {
			Node n = new Node(item);
			n._next = _front;
			_front = n;
		}
		++_size;
	}

	public void insertRear(T item) {
		if (_rear == null) {
			_front = _rear = new Node(item);
		} else {
			Node n = new Node(item);
			_rear._next = n;
			_rear = n;
		}
		++_size;
	}

	public T deleteFront() throws Exception {
		if (_front == null) {
			throw new Exception("Error - Linked list is empty");
		} else {
			T deleted = _front._data;
			_front = _front._next;
			if (_front == null) {
				_rear = null;
			}
			--_size;
			return deleted;
		}
	}

	public T deleteRear() throws Exception {
		if (_rear == null) {
			throw new Exception("Error - Linked list is empty");
		} else if (_front == _rear) {
			T deleted = _rear._data;
			_front = _rear = null;
			--_size;
			return deleted;
		} else {
			Node curr = _front;
			while (curr._next != _rear) {
				curr = curr._next;
			}
			T deleted = _rear._data;
			curr._next = null;
			_rear = curr;
			--_size;
			return deleted;
		}
	}

	public void delete(T item) throws Exception {
		if (_front == null) {
			throw new Exception("Error - Linked list is empty");
		} else if (_front._data.equals(item)) {
			this.deleteFront();
		} else if (_rear._data.equals(item)) {
			this.deleteRear();
		} else {
			Node curr = _front;
			while (curr != _rear) {
				if (curr._next._data.equals(item)) {
					break;
				}
			}
			if (curr == _rear) {
				throw new Exception("Error - The given item not in list");
			} else {
				curr._next = curr._next._next;
				--_size;
			}
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			private Node curr = _front;

			@Override
			public boolean hasNext() {
				return curr != null;
			}

			@Override
			public T next() {
				T ret = curr._data;
				curr = curr._next;
				return ret;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Error - Remove operation not supported");
			}
		};
	}
}
