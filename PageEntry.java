import java.io.*;
import java.util.Iterator;

public class PageEntry {
	private String _pageName;
	private PageIndex _pageIndex;
	private MyAVLTree<Position> _allPositions;
	private int _numWords;

	PageEntry(String pageName) throws Exception {
		_pageName = pageName;
		_pageIndex = new PageIndex();
		_allPositions = new MyAVLTree<>();
		File file = new File("./webpages/"+pageName);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String str;
		int idx = 1;
		while ((str = br.readLine()) != null) {
			str = fixString(str);
			if (str.equals("")) {
				continue;
			}
			String words[] = str.split("\\s+");
			for (int i = 0; i < words.length; ++i, ++idx) {
				if (isConnector(words[i])) {
					continue;
				}
				switch (words[i]) {
					case "stacks":
						words[i] = "stack";
						break;
					case "structures":
						words[i] = "structure";
						break;
					case "applications":
						words[i] = "application";
						break;
					default:
						break;
				}
				Position p = new Position(this, idx);
				_pageIndex.addPositionForWord(words[i], p);
				_allPositions.insert(p);
			}
		}
		_numWords = idx-1;
	}

	private boolean isConnector(String word) {
		switch (word) {
			case "a":
			case "an":
			case "the":
			case "they":
			case "these":
			case "this":
			case "for":
			case "is":
			case "are":
			case "was":
			case "of":
			case "or":
			case "and":
			case "does":
			case "will":
			case "whose":
				return true;
			default:
				return false;
		}
	}

	private String fixString(String str) {
		char arr[] = str.toCharArray();
		for (int i = 0; i < arr.length; ++i) {
			switch (arr[i]) {
				case '{':
				case '}':
				case '[':
				case ']':
				case '<':
				case '>':
				case '=':
				case '(':
				case ')':
				case '.':
				case ',':
				case ';':
				case '\'':
				case '\"':
				case '?':
				case '#':
				case '!':
				case '-':
				case ':':
				case '\t':
					arr[i] = ' ';
					break;
				default:
					break;
			}
		}
		return String.valueOf(arr).trim().toLowerCase();
	}

	public WordEntry getWordEntryFor(String word) throws Exception {
		Iterator<WordEntry> it = _pageIndex.getWordEntries().iterator();
		while (it.hasNext()) {
			WordEntry w = it.next();
			if (w.equals(word)) {
				return w;
			}
		}
		throw new Exception("Webpage "+_pageName+" does not contain word "+word);
	}

	private boolean isWordAtPosition(String word, Position p) {
		try {
			WordEntry w = getWordEntryFor(word);
			if (w.getPositionTree().find(p) != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public double getPhraseTermFrequency(String str[]) {
		double m = 0.0;
		WordEntry wEntry = null;
		try {
			wEntry = getWordEntryFor(str[0]);
		} catch (Exception e) {
			return 0.0;
		}
		Iterator<Position> it = wEntry.getAllPositionsForThisWord().iterator();
		while (it.hasNext()) {
			Position p = it.next();
			boolean ok = true;
			for (int i = 1; i < str.length; ++i) {
				p = _allPositions.inOrderSuccessor(p);
				if (p == null || !isWordAtPosition(str[i], p)) {
					ok = false;
					break;
				}
			}
			if (ok) {
				++m;
			}
		}
		return m/(_numWords-(str.length-1)*m);
	}

	public PageIndex getPageIndex() {
		return _pageIndex;
	}

	public String getPageName() {
		return _pageName;
	}

	public int getNumWords() {
		return _numWords;
	}

	public boolean equals(PageEntry other) {
		return _pageName.equals(other._pageName);
	}

	public boolean equals(String pageName) {
		return _pageName.equals(pageName);
	}
}
