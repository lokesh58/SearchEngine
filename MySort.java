import java.util.ArrayList;
import java.util.Iterator;

public class MySort<T extends Comparable<T>> {
	public ArrayList<T> sortThisList(MySet<T> listOfSortableEntries) {
		ArrayList<T> sortedList = new ArrayList<T>(listOfSortableEntries.size());
		MyAVLTree<T> tree = new MyAVLTree<>();
		Iterator<T> it = listOfSortableEntries.iterator();
		while (it.hasNext()) {
			tree.insert(it.next());
		}
		it = tree.getInOrderList().iterator();
		while (it.hasNext()) {
			sortedList.add(it.next());
		}
		return reverse(sortedList);//To get in descending order
	}

	private ArrayList<T> reverse(ArrayList<T> list) {
		ArrayList<T> rev = new ArrayList<>(list.size());
		for (int i = list.size()-1; i >= 0; --i) {
			rev.add(list.get(i));
		}
		return rev;
	}
}
