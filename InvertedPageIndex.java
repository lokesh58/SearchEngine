import java.util.Iterator;

public class InvertedPageIndex {
	private MyHashTable _invertedPageIndex;

	InvertedPageIndex() {
		_invertedPageIndex = new MyHashTable();
	}

	public void addPage(PageEntry p) {
		MyLinkedList<WordEntry> wordEntries = p.getPageIndex().getWordEntries();
		Iterator<WordEntry> it = wordEntries.iterator();
		while (it.hasNext()) {
			_invertedPageIndex.addPositionsForWord(it.next());
		}
	}

	public MySet<PageEntry> getPagesWhichContainWord(String word) {
		MySet<PageEntry> webpages = new MySet<PageEntry>();
		WordEntry entry;
		try {
			entry = _invertedPageIndex.getWordEntryFor(word);
		} catch (Exception e) {
			return webpages;
		}
		MyLinkedList<Position> positions = entry.getAllPositionsForThisWord();
		Iterator<Position> it = positions.iterator();
		while (it.hasNext()) {
			webpages.addElement(it.next().getPageEntry());
		}
		return webpages;
	}
}
