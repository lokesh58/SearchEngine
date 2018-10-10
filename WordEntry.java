import java.util.Iterator;

public class WordEntry {
	private String _word;
	private MyLinkedList<Position> _positions;

	WordEntry(String word) {
		_word = word;
		_positions = new MyLinkedList<Position>();
	}

	public void addPosition(Position position) {
		_positions.insertRear(position);
	}

	public void addPositions(MyLinkedList<Position> positions) {
		Iterator<Position> it = positions.iterator();
		while (it.hasNext()) {
			_positions.insertRear(it.next());
		}
	}

	public String getWord() {
		return _word;
	}

	public MyLinkedList<Position> getAllPositionsForThisWord() {
		return _positions;
	}

	public float getTermFrequency(String pageName) {
		int freq = 0;
		//TODO
		return freq;
	}

	public boolean equals(String word) {
		return _word.equals(word);
	}

	public boolean equals(WordEntry other) {
		return _word.equals(other._word) && _positions == other._positions;
	}
}
