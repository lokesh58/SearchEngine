import java.util.Iterator;

public class MyHashTable {
	class WordEntryList extends MyLinkedList<WordEntry> {} //Work around generic array

	private WordEntryList _hashTable[];
	private int _hashSize;

	MyHashTable() {
		_hashSize = 100;
		_hashTable = new WordEntryList[_hashSize];
		for (int i = 0; i < _hashSize; ++i) {
			_hashTable[i] = new WordEntryList();
		}
	}

	private int getHashIndex(String str) {
		int hash = 1;
		for (int i = 0; i < str.length(); ++i) {
			hash ^= (hash*(int)str.charAt(i));
		}
		hash %= _hashSize;
		if (hash < 0) {
			hash += _hashSize;
		}
		return hash;
	}

	public void addPositionsForWord(WordEntry w) {
		int idx = getHashIndex(w.getWord());
		Iterator<WordEntry> it = _hashTable[idx].iterator();
		boolean found = false;
		WordEntry entry = null;
		while (it.hasNext()) {
			entry = it.next();
			if (entry.getWord().equals(w.getWord())) {
				found = true;
				break;
			}
		}
		if (!found) {
			entry = new WordEntry(w.getWord());
			_hashTable[idx].insertRear(entry);
		}
		entry.addPositions(w.getAllPositionsForThisWord());
	}

	public WordEntry getWordEntryFor(String word) throws Exception {
		int idx = getHashIndex(word);
		Iterator<WordEntry> it = _hashTable[idx].iterator();
		WordEntry entry = null;
		while (it.hasNext()) {
			entry = it.next();
			if (entry.getWord().equals(word)) {
				return entry;
			}
		}
		throw new Exception("No webpage contains word "+word);
	}
}
