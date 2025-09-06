
/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.ArrayList;
import java.util.Arrays;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	private static final int BONUS = 63;

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	// creates matrix for categories, starts interaction with user, determines
	// winner
	private void playGame() {
		setCategories();
		askPlayer();
		finalizeGame();
	}

	// creates matrix, sets -1 as default value
	private void setCategories() {
		categories = new int[N_CATEGORIES][nPlayers];
		for (int i = 0; i < N_CATEGORIES - 1; i++) {
			for (int j = 0; j < nPlayers; j++) {
				categories[i][j] = -1;
			}
		}
	}

	// starts interaction with user, asks for roll and reroll
	private void askPlayer() {
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			for (int j = 0; j < nPlayers; j++) {
				rollMessage();
				display.waitForPlayerToClickRoll(j + 1);
				rollDice();
				display.displayDice(dice);

				rerollMessage(j);
				display.waitForPlayerToSelectDice();
				reroll();

				rerollMessage(j);
				display.waitForPlayerToSelectDice();
				reroll();

				categoryMessage(j);
				chooseCategory(j);
			}
		}
	}

	// sets random value of dice
	private void rollDice() {
		for (int i = 0; i < dice.length; i++) {
			int randNumber = rgen.nextInt(1, 6);
			dice[i] = randNumber;
		}
		display.displayDice(dice);
	}

	// rerolls dice the players has chosen
	private void reroll() {
		for (int i = 0; i < dice.length; i++) {
			if (display.isDieSelected(i)) {
				dice[i] = rgen.nextInt(1, 6);
			}
		}
		display.displayDice(dice);
	}

	// roll dice message
	private void rollMessage() {
		display.printMessage("click ''roll dice'' to roll ");
	}

	// choose category message
	private void categoryMessage(int j) {
		display.printMessage("play " + playerNames[j] + " choose category");
	}

	// reroll dice message
	private void rerollMessage(int j) {
		display.printMessage("player " + playerNames[j] + " choose dice you want to reroll, click ''Roll again''");
	}

	/*
	 * asks player to choose category, if category is chosen player is asked again
	 * checks for default value if category was chosen
	 */
	private void chooseCategory(int j) {
		while (true) {
			int category = display.waitForPlayerToSelectCategory();
			boolean p = checkCategory(category);
			if (categories[category - 1][j] != -1) {
				display.printMessage("choose another category");
			} else {
				update(p, category, j);
				break;
			}
		}
	}

	// updates scorecard, sets new value for matrix
	private void update(boolean p, int category, int j) {
		if (p) {
			int score = categoryScore(category);
			display.updateScorecard(category, j + 1, score);
			categories[category - 1][j] = score;
			int total = categories[TOTAL - 1][j] + score;
			categories[TOTAL - 1][j] = total;
			display.updateScorecard(TOTAL, j + 1, total);
		} else {
			display.updateScorecard(category, j + 1, 0);
			categories[category - 1][j] = 0;
		}
	}

	// checks for every category
	private int categoryScore(int category) {
		if (category <= 6)
			return score(category);
		if (category == 9)
			return scoreSum();
		if (category == 10)
			return scoreSum();
		if (category == 11)
			return FULLHOUSE;
		if (category == 12)
			return SMALLSTRAIGHT;
		if (category == 13)
			return LARGESTRAIGHT;
		if (category == 14)
			return YAHTZEE;
		return scoreSum();
	}

	// returns sum of certain dice
	private int score(int category) {
		int result = 0;
		for (int i = 0; i < dice.length; i++) {
			if (dice[i] == category) {
				result += category;
			}
		}
		return result;
	}

	// sums up values of dice
	private int scoreSum() {
		int result = 0;
		for (int i = 0; i < dice.length; i++) {
			result += dice[i];
		}
		return result;
	}

	// checks if dice values satisfy chosen category
	private boolean checkCategory(int category) {
		if (category <= 6)
			return true;
		if (category == 9)
			return threeOfAKind();
		if (category == 10)
			return fourOfAKind();
		if (category == 11)
			return fullHouse();
		if (category == 12)
			return smallStraight();
		if (category == 13)
			return largeStraight();
		if (category == 14)
			return yahtzee();
		return true;
	}

	// checks if at least 3 values of dice are the same
	private boolean threeOfAKind() {
		int temp = 0;
		for (int i = 0; i < dice.length; i++) {
			for (int j = i; j < dice.length; j++) {
				if (dice[i] == dice[j])
					temp++;
				if (temp == 3)
					return true;
			}
			temp = 0;
		}
		return false;
	}

	// checks if at least 4 values of dice are the same
	private boolean fourOfAKind() {
		int temp = 0;
		for (int i = 0; i < dice.length; i++) {
			for (int j = i; j < dice.length; j++) {
				if (dice[i] == dice[j]) {
					temp++;
				}
				if (temp == 4) {
					return true;
				}
			}
			temp = 0;
		}
		return false;
	}

	/*
	 * checks full house: checks value of dice that are repeated 3 times and
	 * remembers that dice stores other 2 values in arraylist checks if those 2
	 * values are the same
	 */
	private boolean fullHouse() {
		ArrayList<Integer> array = new ArrayList<>();
		int temp = 0;
		int element = 0;
		for (int i = 0; i < dice.length; i++) {
			for (int j = i; j < dice.length; j++) {
				if (dice[i] == dice[j])
					temp++;
				if (temp == 3) {
					element = dice[i];
					break;
				}
			}
			temp = 0;
		}
		for (int i = 0; i < dice.length; i++) {
			if (element != dice[i]) {
				array.add(dice[i]);
			}
		}
		if (array.size() == 2 && array.get(0) == array.get(1)) {
			return true;
		}
		return false;
	}

	/*
	 * checks small straight copies dice array and sorts it in ascending numerical
	 * order checks if 4 of the elements create arithmetic progression
	 */
	private boolean smallStraight() {
		int[] coppiedArray = dice.clone();
		Arrays.sort(coppiedArray);
		int n = 0;
		for (int i = 0; i < coppiedArray.length - 1; i++) {
			if ((coppiedArray[i] + 1) == coppiedArray[i + 1] && coppiedArray[i] != coppiedArray[i + 1]) {
				n++;
			}
		}
		if (n >= 3) {
			return true;
		}
		return false;
	}

	/*
	 * checks large straight copies dice array and sorts it in ascending numerical
	 * order checks if all of the elements create arithmetic progression
	 */
	private boolean largeStraight() {
		int[] coppiedArray = dice.clone();
		Arrays.sort(coppiedArray);
		for (int i = 0; i < coppiedArray.length - 1; i++) {
			if ((coppiedArray[i] + 1) != coppiedArray[i + 1]) {
				return false;
			}
		}
		return true;
	}

	// checks if ell the elements are the same
	private boolean yahtzee() {
		int a = dice[0];
		for (int i = 1; i < dice.length; i++) {
			if (a != dice[i]) {
				return false;
			}
		}
		return true;
	}

	// sums scores, determines winner
	private void finalizeGame() {
		for (int i = 0; i < nPlayers; i++) {
			int upperScore = calculateUpperScore(i);
			int bonus = calculateBonus(upperScore);
			int lowerScore = calculateLowerScore(i);
			int total = upperScore + bonus + lowerScore;
			categories[TOTAL - 1][i] = total;
			display.updateScorecard(UPPER_SCORE, i + 1, upperScore);
			display.updateScorecard(UPPER_BONUS, i + 1, bonus);
			display.updateScorecard(LOWER_SCORE, i + 1, lowerScore);
			display.updateScorecard(TOTAL, i + 1, total);
		}
		determineWinner();
	}

	// calculates lower score
	private int calculateLowerScore(int j) {
		int result = 0;
		for (int i = THREE_OF_A_KIND - 1; i < LOWER_SCORE - 1; i++) {
			result += categories[i][j];
		}
		return result;
	}

	// calculates upper score
	private int calculateUpperScore(int j) {
		int result = 0;
		for (int i = 0; i < UPPER_SCORE - 1; i++) {
			result += categories[i][j];
		}
		return result;
	}

	// calculates if player has enough points for bonus
	private int calculateBonus(int n) {
		int result = 0;
		if (n >= BONUS) {
			result = 35;
		}
		return result;
	}

	// calculates maximum score and determines winner
	private void determineWinner() {
		int winningScore = 0;
		for (int i = 0; i < categories[16].length; i++) {
			if (categories[16][i] > winningScore) {
				winningScore = categories[16][i];
			}
		}
		String winner = "";
		for (int i = 0; i < categories[16].length; i++) {
			if (winningScore == categories[16][i]) {
				winner += playerNames[i] + " ";
			}
		}
		display.printMessage("winners: " + winner + "(score:" + winningScore + ")");
	}

	// private constants
	private static final int FULLHOUSE = 25;
	private static final int SMALLSTRAIGHT = 30;
	private static final int LARGESTRAIGHT = 40;
	private static final int YAHTZEE = 50;
	/* Private instance variables */
	private int[] dice = new int[N_DICE];
	private int nPlayers;
	private int[][] categories;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}
