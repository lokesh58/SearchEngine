import java.util.ArrayList;
import java.util.Iterator;

public class MySort<T extends Comparable<T>> {
	public ArrayList<T> sortThisList(MySet<T> listOfSortableEntries) {
		ArrayList<T> sortedList = new ArrayList<T>(listOfSortableEntries.size());
		while (!listOfSortableEntries.isEmpty()) {
			Iterator<T> it = listOfSortableEntries.iterator();
			T biggestEntry = it.next();
			while (it.hasNext()) {
				T entry = it.next();
				if (entry.compareTo(biggestEntry) > 0) {
					biggestEntry = entry;
				}
			}
			sortedList.add(biggestEntry);
			try {
				listOfSortableEntries.deleteElement(biggestEntry);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sortedList;
	}
}
