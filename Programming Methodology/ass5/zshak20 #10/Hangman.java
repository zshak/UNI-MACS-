
/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {
	private static final int GUESSES = 8;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int lives;
	private HangmanCanvas canvas;
	private String word;
	private String hidenWord;

	// sets canvas size, adds new HangmanCanvas class object
	public void init() {
		setSize(700, 600);
		canvas = new HangmanCanvas();
		add(canvas);
	}

	// game of Hangman
	public void run() {
		startGame();
	}

	// selects word for a game, starts interaction with user
	private void startGame() {
		selectWord();
		playGame();
	}

	// selects word randomly from hangmanLexicon class
	private void selectWord() {
		HangmanLexicon Lexicon = new HangmanLexicon();
		word = Lexicon.getWord(rgen.nextInt(0, Lexicon.getWordCount() - 1));
//		println(word);
		hideWord(word);
		intro();
	}

	// prints introduction
	private void intro() {
		println("Welcome to Hangman");
		println("the word now looks like this " + hidenWord);
	}

	// encrypts chosen word with "-"
	private void hideWord(String word) {
		hidenWord = "";
		for (int i = 0; i < word.length(); i++) {
			hidenWord += "-";
		}
	}

	/*
	 * starts interaction with user, sets initial number of guesses 8, displays
	 * encrypted word, body parts and wrong guesses on canvas. checks if the user
	 * has entered a string of length 1, makes it into upper case, checks for
	 * correct and incorrect guesses
	 */
	private void playGame() {
		lives = GUESSES;
		canvas.reset();
		canvas.displayWord(hidenWord);
		while (!(hidenWord.equals(word) || lives == 0)) {
			String guess = readLine("your guess: ");
			guess = guess.toUpperCase();
			if (guess.length() != 1) {
				println("please enter a character");
			} else {
				char ch = guess.charAt(0);
				checkCorrectGuess(guess, ch);
				checkWrongGuess(guess, ch);
			}
		}
		restart();
	}

	// opens guessed character in the encrypted word
	private void openChar(char ch) {
		for (int i = 0; i < word.length(); i++) {
			if (word.charAt(i) == ch) {
				hidenWord = hidenWord.substring(0, i) + ch + hidenWord.substring(i + 1);
			}
		}
	}

	// checks for correct guess. in case of guessing the whole word, suggests
	// playing again
	private void checkCorrectGuess(String guess, char ch) {
		if (word.contains(guess)) {
			openChar(ch);
			if (hidenWord.equals(word)) {
				println("you guessed the word");
				canvas.displayWord(word);
				return;
			}
			printWinningMessage(hidenWord, lives);
		}
	}

	// checks for wrong guess. in case of losing the game, suggests playing again
	private void checkWrongGuess(String guess, char ch) {
		if (!(word.contains(guess))) {
			lives--;
			canvas.noteIncorrectGuess(ch, lives);
			if (lives == 0) {
				println("you are completely hung");
				println("The word was " + word);
				return;
			}
			printLosingMessage(ch, lives);
		}
	}

	// suggestion to play again
	private void restart() {
		String end = readLine("type enter to start again: ");
		if (end.equals("")) {
			startGame();
		} else {
			println("good Game!");
		}
	}

	// prints message when user makes a wrong guess
	private void printLosingMessage(char ch, int guesses) {
		println("there are no " + ch + "'s in the word");
		println("you have " + guesses + " guesses left");
		println("the word now looks like this: " + hidenWord);
	}

	// prints message when user makes a correct guess
	private void printWinningMessage(String hidenWord, int guesses) {
		println("your guess is correct");
		println("the word now looks like this : " + hidenWord);
		canvas.displayWord(hidenWord);
		println("you have " + guesses + " guesses left");
	}
}
