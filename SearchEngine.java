import java.util.Iterator;

public class SearchEngine {
	private InvertedPageIndex _invPgIdx;
	private MyLinkedList<PageEntry> _webpageDatabase;

	SearchEngine() {
		_invPgIdx = new InvertedPageIndex();
		_webpageDatabase = new MyLinkedList<PageEntry>();
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
		MySet<String> webpages = new MySet<>();
		Iterator<Position> it = wEntry.getAllPositionsForThisWord().iterator();
		while (it.hasNext()) {
			webpages.addElement(it.next().getPageEntry().getPageName());
		}
		MyLinkedList<String> sortedPages = new MyLinkedList<>();
		double invDocFreq = Math.log(_webpageDatabase.size())-Math.log(webpages.size());
		while (!webpages.isEmpty()) {
			String maxRelPage = "";
			double maxRel = -1.0;
			Iterator<String> iter = webpages.iterator();
			while (iter.hasNext()) {
				String page = iter.next();
				double rel = wEntry.getTermFrequency(page)*invDocFreq;
				if (rel > maxRel) {
					maxRel = rel;
					maxRelPage = page;
				}
			}
			sortedPages.insertRear(maxRelPage);
			try {
				webpages.deleteElement(maxRelPage);
			} catch (Exception e) {
				System.err.println("Error in pagesWhichConatinWord");
				break;
			}
		}
		String pages = "";
		Iterator<String> iter = sortedPages.iterator();
		while (iter.hasNext()) {
			pages += iter.next();
			if (iter.hasNext()) {
				pages += ", ";
			}
		}
		return pages;
	}

	private String positionOfWordInAPage(String word, String pageName) {
		Iterator<PageEntry> it = _webpageDatabase.iterator();
		PageEntry pEntry = null;
		boolean foundPage = false;
		while (it.hasNext()) {
			pEntry = it.next();
			if (pEntry.equals(pageName)) {
				foundPage = true;
				break;
			}
		}
		if (foundPage) {
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

	public void performAction(String actionMessage) {
		String tokens[] = actionMessage.trim().split("\\s+");
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
