import java.io.*;

public class PageEntry {
	private String _pageName;
	private PageIndex _pageIndex;

	PageEntry(String pageName) throws Exception {
		_pageName = pageName;
		_pageIndex = new PageIndex();
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
				_pageIndex.addPositionForWord(words[i], new Position(this, idx));
			}
		}
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

	public PageIndex getPageIndex() {
		return _pageIndex;
	}

	public String getPageName() {
		return _pageName;
	}

	public boolean equals(PageEntry other) {
		return _pageName.equals(other._pageName);
	}

	public boolean equals(String pageName) {
		return _pageName.equals(pageName);
	}
}
