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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Project 2 will read a file InputFile1.dat and add the words in it to the fsa,
 * as a keyword. it will then read the Project2.java file and analyze each word
 * in the file to see if the word is in the fsa. If it isn't it will print the
 * word followed by a ? into the output file, and then it will add the word to
 * the fsa. If the word is in the fsa, then it will print the word followed by
 * either a * for a key word from InputFile1, or a @ if it wasn't a key word.
 * 
 * @author David Scianni
 * 
 */
public class Project2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/**
		 * myFile will be used to read the InputFile1.dat file
		 */
		File myFile = new File("InputFile1.dat");
		/**
		 * out will be used to write to the output to the text file output.txt
		 */
		PrintWriter out = null;
		try {
			out = new PrintWriter("output.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/**
		 * fsa is the DynamicFSA that holds all the data from the files, and
		 * represents a dynamic fsa.
		 */
		DynamicFSA fsa = new DynamicFSA();

		try {

			/**
			 * iFile is the scanner that will read from the file myFile, to get
			 * the information about the FSAs
			 */
			Scanner iFile = new Scanner(myFile);

			while (iFile.hasNext()) {
				fsa.initialCheck(iFile.next());
			}
			iFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		myFile = new File(
				"src\\edu\\csupomona\\cs\\cs311\\Proj2\\DynamicFSA.java");

		try {

			/**
			 * iFile is the scanner that will read from the file myFile, to get
			 * the information about the FSAs
			 */
			Scanner iFile = new Scanner(myFile);
			/**
			 * line will hold the next line from the file, while temp will hold
			 * the word to be printed to the output file.
			 */
			String line, temp = null;
			/**
			 * lineSplit will hold the String array that is made up of the split
			 * string that makes up the line from the file.
			 */
			String[] lineSplit;

			while (iFile.hasNext()) {
				line = iFile.nextLine();
				line = line.replaceAll("\\.", " ");
				line = line.replaceAll("\\;", " ");
				line = line.replaceAll("\\(", " ");
				line = line.replaceAll("\\)", " ");
				line = line.replaceAll("\\[", " ");
				line = line.replaceAll("\\]", " ");
				lineSplit = line.split(" ");
				for (int i = 0; i < lineSplit.length; i++) {
					lineSplit[i] = lineSplit[i].replaceAll("\\s", "");
					lineSplit[i] = lineSplit[i].replaceAll("\t", "");
					// System.out.println("\n***" + lineSplit[i] + "***");
					temp = fsa.check(lineSplit[i]);
					if (temp != null) {
						out.print(temp);
					}
				}
				out.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		out.println("Transition Table:");
		out.println();
		out.println("Switch\t\t\tSymbol\tNext");
		int c = 97;
		for (int i = 0; i < 26; i++) {
			out.printf("%d\t%c\t\t%2d  %c\t%d%n", fsa.getFirstSymbol(i),
					Character.toChars(c++)[0], i, fsa.getSymbol(i),
					fsa.getNext(i));
		}
		c = 65;
		for (int i = 26; i < 52; i++) {
			out.println(fsa.getFirstSymbol(i) + "\t"
					+ Character.toChars(c++)[0] + "\t\t" + i + "  "
					+ fsa.getSymbol(i) + "\t" + fsa.getNext(i));
		}
		out.println(fsa.getFirstSymbol(52) + "\t_\t\t52  " + fsa.getSymbol(52)
				+ "\t" + fsa.getNext(52));
		out.println(fsa.getFirstSymbol(53) + "\t$\t\t53  " + fsa.getSymbol(53)
				+ "\t" + fsa.getNext(53));
		for (int i = 54; i < 1300; i++) {
			out.println("\t\t\t" + i + "  " + fsa.getSymbol(i) + "\t"
					+ fsa.getNext(i));
		}
		out.close();
	}
}
