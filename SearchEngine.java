import java.util.Iterator;
import java.util.ArrayList;

public class SearchEngine {
	private InvertedPageIndex _invPgIdx;
	private MyLinkedList<PageEntry> _webpageDatabase;
	private MySort<SearchResult> sorter;

	SearchEngine() {
		_invPgIdx = new InvertedPageIndex();
		_webpageDatabase = new MyLinkedList<PageEntry>();
		sorter = new MySort<>();
	}
	
	private void addPage(String pageName) throws Exception {
		PageEntry pEntry;
		Iterator<PageEntry> it = _webpageDatabase.iterator();
		while (it.hasNext()) {
			pEntry = it.next();
			if (pEntry.equals(pageName)) {
				return;
			}
		}
		try {
			pEntry = new PageEntry(pageName);
		} catch (Exception e) {
			throw new Exception("Error - Webpage "+pageName+" not found");
		}
		_invPgIdx.addPage(pEntry);
		_webpageDatabase.insertRear(pEntry);
	}

	private String pagesWhichContainWord(String word) {
		WordEntry wEntry = null;
		try {
			wEntry = _invPgIdx.getWordEntryFor(word);
		} catch (Exception e) {
			return e.getMessage();
		}
		MySet<PageEntry> web = _invPgIdx.getPagesWhichContainWord(word);
		MySet<SearchResult> webpages = new MySet<>();
		Iterator<PageEntry> it = web.iterator();
		double invDocFreq = Math.log(_webpageDatabase.size())-Math.log(web.size());
		while (it.hasNext()) {
			PageEntry pEntry = it.next();
			double rel = wEntry.getTermFrequency(pEntry.getPageName())*invDocFreq;
			webpages.addElement(new SearchResult(pEntry, rel));
		}

		ArrayList<SearchResult> list = sorter.sortThisList(webpages);
		String pages = "";
		for (int i = 0; i < list.size(); ++i) {
			pages += list.get(i).getPageEntry().getPageName();
			if (i+1 < list.size()) {
				pages += ", ";
			}
		}
		return pages;
	}

	private PageEntry findPage(String pageName) {
		Iterator<PageEntry> it = _webpageDatabase.iterator();
		while (it.hasNext()) {
			PageEntry p = it.next();
			if (p.equals(pageName)) {
				return p;
			}
		}
		return null;
	}

	private String positionOfWordInAPage(String word, String pageName) {
		PageEntry pEntry = findPage(pageName);
		if (pEntry != null) {
			MyLinkedList<WordEntry> wEntries = pEntry.getPageIndex().getWordEntries();
			WordEntry wEntry = null;
			Iterator<WordEntry> iter = wEntries.iterator();
			boolean foundWord = false;
			while (iter.hasNext()) {
				wEntry = iter.next();
				if (wEntry.equals(word)) {
					foundWord = true;
					break;
				}
			}
			if (foundWord) {
				MyLinkedList<Position> pos = wEntry.getAllPositionsForThisWord();
				Iterator<Position> it2 = pos.iterator();
				String positions = "";
				while (it2.hasNext()) {
					Position p = it2.next();
					positions += p.getWordIndex();
					if (it2.hasNext()) {
						positions += ", ";
					}
				}
				return positions;
			} else {
				return "Webpage "+pageName+" does not contain word "+word;
			}
		} else {
			return "No webpage "+pageName+" found";
		}
	}

	private String pagesWhichContainAllWords(String words[]) {
		WordEntry wEntries[] = new WordEntry[words.length];
		double invDocFreq[] = new double[words.length];
		MySet<String> web = new MySet<>();
		for (int i = 0; i < words.length; ++i) {
			try {
				wEntries[i] = _invPgIdx.getWordEntryFor(words[i]);
			} catch (Exception e) {
				return "No webpage contains all these words";
			}
			MySet<String> set = new MySet<>();
			Iterator<Position> it = wEntries[i].getAllPositionsForThisWord().iterator();
			while (it.hasNext()) {
				set.addElement(it.next().getPageEntry().getPageName());
			}
			invDocFreq[i] = Math.log(_webpageDatabase.size())-Math.log(set.size());
			web = web.intersection(set);
		}
		if (web.isEmpty()) {
			return "No webpage contains all these words";
		}
		MySet<SearchResult> webpages = new MySet<>();
		Iterator<String> it = web.iterator();
		while (it.hasNext()) {
			String pageName = it.next();
			PageEntry pEntry = findPage(pageName);
			double rel = 0.0;
			for (int i = 0; i < words.length; ++i) {
				rel += wEntries[i].getTermFrequency(pageName)*invDocFreq[i];
			}
			webpages.addElement(new SearchResult(pEntry, rel));
		}
		
		ArrayList<SearchResult> list = sorter.sortThisList(webpages);
		String pages = "";
		for (int i = 0; i < list.size(); ++i) {
			pages += list.get(i).getPageEntry().getPageName();
			if (i+1 < list.size()) {
				pages += ", ";
			}
		}
		return pages;
	}

	private String pagesWhichContainAnyOfTheseWords(String words[]) {
		WordEntry wEntries[] = new WordEntry[words.length];
		double invDocFreq[] = new double[words.length];
		MySet<String> web = new MySet<>();
		for (int i = 0; i < words.length; ++i) {
			try {
				wEntries[i] = _invPgIdx.getWordEntryFor(words[i]);
			} catch (Exception e) {
				wEntries[i] = null;
				invDocFreq[i] = 0.0;
			}
			MySet<String> set = new MySet<>();
			Iterator<Position> it = wEntries[i].getAllPositionsForThisWord().iterator();
			while (it.hasNext()) {
				set.addElement(it.next().getPageEntry().getPageName());
			}
			invDocFreq[i] = Math.log(_webpageDatabase.size())-Math.log(set.size());
			web = web.union(set);
		}
		if (web.isEmpty()) {
			return "No webpage contains any of these words";
		}
		MySet<SearchResult> webpages = new MySet<>();
		Iterator<String> it = web.iterator();
		while (it.hasNext()) {
			String pageName = it.next();
			PageEntry pEntry = findPage(pageName);
			double rel = 0.0;
			for (int i = 0; i < words.length; ++i) {
				if (wEntries[i] == null) continue;
				rel += wEntries[i].getTermFrequency(pageName)*invDocFreq[i];
			}
			webpages.addElement(new SearchResult(pEntry, rel));
		}
		
		ArrayList<SearchResult> list = sorter.sortThisList(webpages);
		String pages = "";
		for (int i = 0; i < list.size(); ++i) {
			pages += list.get(i).getPageEntry().getPageName();
			if (i+1 < list.size()) {
				pages += ", ";
			}
		}
		return pages;
	}

	private String pagesWhichContainPhrase(String words[]) {
		MySet<PageEntry> web = _invPgIdx.getPagesWhichContainPhrase(words);
		if (web.isEmpty()) {
			return "No webpage contains the given phrase";
		}
		MySet<SearchResult> webpages = new MySet<>();
		Iterator<PageEntry> it = web.iterator();
		double invDocFreq = Math.log(_webpageDatabase.size())-Math.log(web.size());
		while (it.hasNext()) {
			PageEntry pEntry = it.next();
			double rel = pEntry.getPhraseTermFrequency(words)*invDocFreq;
			webpages.addElement(new SearchResult(pEntry, rel));
		}

		ArrayList<SearchResult> list = sorter.sortThisList(webpages);
		String pages = "";
		for (int i = 0; i < list.size(); ++i) {
			pages += list.get(i).getPageEntry().getPageName();
			if (i+1 < list.size()) {
				pages += ", ";
			}
		}
		return pages;
	}

	public void performAction(String actionMessage) {
		String tokens[] = actionMessage.trim().split("\\s+");
		String words[] = new String[tokens.length-1];
		String answer = null, x, y;
		try {
			switch (tokens[0]) {
				case "addPage":
					if (tokens.length < 2) {
						throw new Exception("Error - Insufficient arguments");
					} else if (tokens.length > 2) {
						throw new Exception("Error - Too many arguments");
					}
					x = tokens[1];
					addPage(x);
					break;
				case "queryFindPagesWhichContainWord":
					if (tokens.length < 2) {
						throw new Exception("Error - Insufficient arguments");
					} else if (tokens.length > 2) {
						throw new Exception("Error - Too many arguments");
					}
					x = fixWord(tokens[1]);
					answer = pagesWhichContainWord(x);
					break;
				case "queryFindPositionsOfWordInAPage":
					if (tokens.length < 3) {
						throw new Exception("Error - Insufficient arguments");
					} else if (tokens.length > 3) {
						throw new Exception("Error - Too many arguments");
					}
					x = fixWord(tokens[1]);
					y = tokens[2];
					answer = positionOfWordInAPage(x, y);
					break;
				case "queryFindPagesWhichContainAllWord":
					if (tokens.length < 2) {
						throw new Exception("Error - At least one word needed");
					}
					for (int i = 0; i < words.length; ++i) {
						words[i] = fixWord(tokens[i+1]);
					}
					answer = pagesWhichContainAllWords(words);
					break;
				case "queryFindPagesWhichContainAnyOfTheseWords":
					if (tokens.length < 2) {
						throw new Exception("Error - At least one word needed");
					}
					for (int i = 0; i < words.length; ++i) {
						words[i] = fixWord(tokens[i+1]);
					}
					answer = pagesWhichContainAnyOfTheseWords(words);
					break;
				case "queryFindPagesWhichContainPhrase":
					if (tokens.length < 2) {
						throw new Exception("Error - At least one word needed");
					}
					for (int i = 0; i < words.length; ++i) {
						words[i] = fixWord(tokens[i+1]);
					}
					answer = pagesWhichContainPhrase(words);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			answer = e.toString();
		}
		if (answer != null) {
			System.out.println(answer);
		}
	}

	private String fixWord(String w) {
		String word = w;
		word = word.toLowerCase();
		switch (word) {
			case "stacks":
				word = "stack";
				break;
			case "structures":
				word = "structure";
				break;
			case "applications":
				word = "application";
				break;
			default:
				break;
		}
		return word;
	}
}
