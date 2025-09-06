
/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import acm.util.*;
import acmx.export.java.io.FileReader;

public class HangmanLexicon {
	private ArrayList<String> lexicon;

	// reads text file and adds it to array list
	public HangmanLexicon() {
		lexicon = new ArrayList<String>();
		try {
			BufferedReader rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				lexicon.add(line);
			}
			rd.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return lexicon.size();
	}

	/** Returns the word at the specified index. */
	public String getWord(int index) {
		return lexicon.get(index);
	}
}
