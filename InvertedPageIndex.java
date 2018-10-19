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
	
	public WordEntry getWordEntryFor(String word) throws Exception {
		return _invertedPageIndex.getWordEntryFor(word);
	}

	public MySet<PageEntry> getPagesWhichContainWord(String word) {
		MySet<PageEntry> webpages = new MySet<PageEntry>();
		MySet<String> hack = new MySet<>();
		WordEntry entry;
		try {
			entry = _invertedPageIndex.getWordEntryFor(word);
		} catch (Exception e) {
			return webpages;
		}
		MyLinkedList<Position> positions = entry.getAllPositionsForThisWord();
		Iterator<Position> it = positions.iterator();
		while (it.hasNext()) {
			PageEntry p = it.next().getPageEntry();
			if (!hack.isMember(p.getPageName())) {
				webpages.addElement(p);
				hack.addElement(p.getPageName());
			}
		}
		return webpages;
	}

	public MySet<PageEntry> getPagesWhichContainPhrase(String str[]) {
		MySet<PageEntry> set = getPagesWhichContainWord(str[0]);
		MySet<PageEntry> webpages = new MySet<>();
		Iterator<PageEntry> it = set.iterator();
		while (it.hasNext()) {
			PageEntry p = it.next();
			if (p.getPhraseTermFrequency(str) != 0.0) {
				webpages.addElement(p);
			}
		}
		return webpages;
	}
}
