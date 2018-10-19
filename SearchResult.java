public class SearchResult implements Comparable<SearchResult> {
	private PageEntry _pEntry;
	private double _relevance;
	SearchResult(PageEntry p, double r) {
		_pEntry = p;
		_relevance = r;
	}

	public PageEntry getPageEntry() {
		return _pEntry;
	}

	public double getRelevance() {
		return _relevance;
	}

	public int compareTo(SearchResult other) {
		if (_relevance > other._relevance) {
			return 1;
		} else if (_relevance < other._relevance) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public boolean equals(SearchResult other) {
		return _pEntry.equals(other._pEntry);
	}
}
