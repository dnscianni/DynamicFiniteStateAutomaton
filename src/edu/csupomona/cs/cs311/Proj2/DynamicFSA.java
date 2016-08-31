/**
 * CS 311: Language Translation and Automata
 * Professor: Daisy Sang
 *
 * Programming Assignment #2
 *
 * To run this project, open up Eclipse, and click file -> import.
 * From the import page, open up the General folder, and select 
 * "Existing Projects into Workspace", and click next.  From this 
 * page, select browse next to the "Select root directory: " option, 
 * and select the "DynamicFiniteStateAutomaton" folder, to import 
 * the whole project. On the left side is the Package Explorer, open 
 * up the DynamicFiniteStateAutomaton folder, then the src folder, 
 * then the edu.csupomona.cs.cs311.Proj2 package.  From there open 
 * up the Project2.java file. To compile the code and run it, from 
 * the Project2 file, click the Run button in the top list of options, 
 * and click the run option, which should have a green circle with a 
 * white arrow icon next to it. The output will be saved to a text 
 * file named output in the DynamicFiniteStateAutomaton folder.
 *
 * David Scianni
 */
package edu.csupomona.cs.cs311.Proj2;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * DynamicFSA represents a dynamic fsa that will take in words and expand its
 * list of acceptable words by adding those words that it doesn't know.
 * 
 * @author David Scianni
 * 
 */
public class DynamicFSA {

	/**
	 * firstSymbol is an array of type int, which will hold the pointer to the
	 * index in symbol and next. each index of firstSymbol represents a
	 * character, with 0-25 being a-z, 26-51 being A-Z, 52 being _ and 53 being
	 * $. for example if firstSymbol[0] = 12, then the first a word is located
	 * in index 12 of symbol.
	 */
	private int[] firstSymbol;

	/**
	 * symbol is an array of type char that will hold all the letters and end
	 * symbols of the words in the fsa, except for the first letter. The first
	 * letter does not need to be added because of the firstSymbol array, which
	 * holds the reference to the first letter.
	 */
	private char[] symbol;

	/**
	 * next is an array of type int that holds the pointer to the next word in
	 * the array symbol.
	 */
	private int[] next;

	/**
	 * flag is an int that will hold the pointer to the next available position
	 * in the symbol array.
	 */
	private int flag;

	/**
	 * the constructor will set the variables to their respective initial
	 * values.
	 */
	public DynamicFSA() {
		firstSymbol = new int[54];
		for (int i = 0; i < firstSymbol.length; i++) {
			firstSymbol[i] = -1;
		}
		symbol = new char[1300];
		next = new int[1300];
		for (int i = 0; i < next.length; i++) {
			next[i] = -1;
		}
		flag = 0;
	}

	/**
	 * initial check will read in a word and search for it in the fsa. If it is
	 * found in the fsa, it will return true. If it is an acceptable word, and
	 * is not in the fsa it will add it and return true. If it is not in the fsa
	 * and it is not an acceptable word, it return false.
	 * 
	 * @param s
	 *            the string that is being checked in the fsa.
	 * @return either true if it is added or already in the fsa, and false if it
	 *         is not an acceptable word.
	 */
	public boolean initialCheck(String s) {
		char[] w = s.toCharArray();
		char[] word = new char[w.length + 1];
		for (int i = 0; i < w.length; i++) {
			word[i] = w[i];
		}
		word[w.length] = '*';
		int nextSymbol = 0;
		int fSym = getCharValue(word[nextSymbol++]);
		if (fSym < 0) {
			return false;
		}
		int ptr = firstSymbol[fSym];
		if (ptr < 0) {
			return create(word, fSym, ptr, nextSymbol);
		} else {
			boolean exit = false;
			while (!exit) {
				if (symbol[ptr] == word[nextSymbol]) {
					if (word[nextSymbol] != '*') {
						ptr++;
						nextSymbol++;
					} else {
						return true;
					}
				} else if (next[ptr] >= 0) {
					ptr = next[ptr];
				} else {
					return create(word, fSym, ptr, nextSymbol);
				}
			}
		}
		return false;
	}

	/**
	 * Create will add the word to the fsa in one of two ways. It will either
	 * add a word that starts with a new letter, or it will add it if there is
	 * already a word with the same beginning letter. If the word is the first
	 * word with the beginning letter, then the corresponding index of the
	 * firstSymbol array will be changed to the flag, and then the word will be
	 * placed in letter by letter in the symbol array. Else, it will put the
	 * flag in the correct index of next, and then add all the letters of the
	 * word in symbol.
	 * 
	 * @param word
	 *            the word being added
	 * @param fSym
	 *            the index of firstSymbol that corresponds to the first letter
	 *            of word.
	 * @param ptr
	 *            the position in next that needs to be replaced with the flag
	 * @param nextSymbol
	 *            the index in word that was left off at while searching.
	 * @return true if it is completed.
	 */
	private boolean create(char[] word, int fSym, int ptr, int nextSymbol) {

		if (firstSymbol[fSym] < 0) {
			firstSymbol[fSym] = flag;
			for (int i = 1; i < word.length; i++) {
				symbol[flag++] = word[i];
			}
		} else {
			next[ptr] = flag;
			for (int i = nextSymbol; i < word.length; i++) {
				symbol[flag++] = word[i];
			}
		}
		return true;
	}

	/**
	 * check will do the same thing as initialCheck, except that it will return
	 * the word with the correct end symbol, which will be either *, ?, or @.
	 * 
	 * @param s
	 *            the word being checked
	 * @return the word followed by either *,?, or @.
	 */
	public String check(String s) {
		char[] w = s.toCharArray();
		char[] word = new char[w.length + 1];
		String temp;
		for (int i = 0; i < w.length; i++) {
			word[i] = w[i];
		}
		word[w.length] = '@';
		int nextSymbol = 0;
		int fSym = getCharValue(word[nextSymbol++]);
		if (fSym < 0) {
			return null;
		}
		int ptr = firstSymbol[fSym];
		if (ptr < 0) {
			temp = s + "? ";
			create(word, fSym, ptr, nextSymbol);
			return temp;
		} else {
			boolean exit = false;
			while (!exit) {
				if (symbol[ptr] == word[nextSymbol]
						|| (symbol[ptr] == '*' && word[nextSymbol] == '@')) {
					if (word[nextSymbol] != '@') {
						ptr++;
						nextSymbol++;
					} else {
						temp = s + symbol[ptr] + " ";
						return temp;
					}
				} else if (next[ptr] >= 0) {
					ptr = next[ptr];
				} else {
					temp = s + "? ";
					create(word, fSym, ptr, nextSymbol);
					return temp;
				}
			}
		}
		return null;
	}

	/**
	 * Checks the number value of the char and sends the corresponding number
	 * that the char represents in the firstSymbol array.
	 * 
	 * @param c
	 *            the char being checked
	 * @return either the number the character represents, or -1 if it is an
	 *         invalid character
	 */
	private int getCharValue(char c) {
		if (c >= 97 && c <= 122) {
			return c - 97;
		} else if (c >= 65 && c <= 90) {
			return c - 39;
		} else if (c == 95) {
			return 52;
		} else if (c == 36) {
			return 53;
		}
		return -1;
	}

	/**
	 * returns the element in firstSymbol[i]
	 * @param i the index of firstSymbol
	 * @return the element in firstSymbol
	 */
	public int getFirstSymbol(int i) {
		return firstSymbol[i];
	}

	/**
	 * returns the element in symbol[i]
	 * @param i the index of symbol
	 * @return the element in symbol
	 */
	public char getSymbol(int i) {
		return symbol[i];
	}

	/**
	 * returns the element in next[i]
	 * @param i the index of next
	 * @return the element in next
	 */
	public int getNext(int i) {
		return next[i];
	}
}
