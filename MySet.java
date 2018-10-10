import java.util.Iterator;

public class MySet<T> implements Iterable<T> {
	private MyLinkedList<T> _list;

	MySet() {
		_list = new MyLinkedList<T>();
	}

	public boolean isMember(T item) {
		return _list.isMember(item);
	}

	public boolean isEmpty() {
		return _list.isEmpty();
	}
	
	public int size() {
		return _list.size();
	}

	public void addElement(T element) {
		if (!isMember(element)) {
			_list.insertRear(element);
		}
	}

	public MySet<T> union(MySet<T> otherSet) {
		MySet<T> unionSet = new MySet<T>();
		Iterator<T> it = _list.iterator();
		while (it.hasNext()) {
			unionSet._list.insertRear(it.next());
		}
		it = otherSet.iterator();
		while (it.hasNext()) {
			unionSet.addElement(it.next());
		}
		return unionSet;
	}

	public MySet<T> intersection(MySet<T> otherSet) {
		MySet<T> intersectionSet = new MySet<T>();
		Iterator<T> it = _list.iterator();
		while (it.hasNext()) {
			T item = it.next();
			if (otherSet.isMember(item)) {
				intersectionSet._list.insertRear(item);
			}
		}
		return intersectionSet;
	}

	@Override
	public Iterator<T> iterator() {
		return _list.iterator();
	}
}
