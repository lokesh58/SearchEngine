import java.util.Iterator;

public class PageIndex {
	private MyLinkedList<WordEntry> _wordEntries;

	PageIndex() {
		_wordEntries = new MyLinkedList<WordEntry>();
	}

	public void addPositionForWord(String word, Position p) {
		Iterator<WordEntry> it = _wordEntries.iterator();
		boolean found = false;
		while (!found && it.hasNext()) {
			WordEntry entry = it.next();
			if (entry.getWord().equals(word)) {
				found = true;
				entry.addPosition(p);
			}
		}
		if (!found) {
			WordEntry entry = new WordEntry(word);
			entry.addPosition(p);
			_wordEntries.insertRear(entry);
		}
	}

	public MyLinkedList<WordEntry> getWordEntries() {
		return _wordEntries;
	}
}
