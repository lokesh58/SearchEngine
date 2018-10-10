public class Position {
	private PageEntry _p;
	private int _wordIndex;
	
	Position(PageEntry p, int wordIndex) {
		_p = p;
		_wordIndex = wordIndex;
	}

	public PageEntry getPageEntry() {
		return _p;
	}

	public int getWordIndex() {
		return _wordIndex;
	}

	public boolean equals(Position other) {
		return _p.equals(other._p) && _wordIndex == other._wordIndex;
	}
}
