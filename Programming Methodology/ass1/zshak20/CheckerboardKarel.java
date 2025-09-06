
/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {
	public void run() {
		fillRow();
		goBack();
		fillTheBoard();
	}

//karel fills the whole world expect the row he is standing on
//pre-condition: 1x1 facing east
//post condition: 1xn facing east
	private void fillTheBoard() {
		while (leftIsClear()) {
			goUp();
			fillRow1();
			goBack();
			fillUpperRow();
		}
	}

// karel goes 1 row up
// pre-condition: 1xb facing east
// post-condition: 1x(b+1) facing east
	private void goUp() {

		turnLeft();
		move();
		turnRight();

	}

	// karel fills a row like a checkerboard starting with putting a beeper
	// pre-condtion:1xb facing east
	// post-condition: nxb facing east
	private void fillRow() {
		putBeeper();
		while (frontIsClear()) {
			move();
			checkWall();
		}
	}

	// karel fills a row like a checkerboard not starting with putting a beeper
	// pre-condition: 1xb facing east
	// post-condition: nxb facing east
	private void fillRow1() {
		if (frontIsClear()) {
			move();
			fillRow();
		}
	}

//karel checks if he can fill the upper row and does the task
//pre-condition: 1xb facing east
//post-condition: 1x(b+1) facing east or 1xb facing east
	private void fillUpperRow() {
		if (leftIsClear()) {
			goUp();
			fillRow();
			goBack();
		}
	}

//karel checks the wall and either puts a beeper in front or stops
//pre-condition: axb facing east
//post-condition: (a+1)xb facing east or axb facing east
	private void checkWall() {
		if (frontIsClear()) {
			move();
			putBeeper();

		}
	}

//karel goes from the end of a row to the start of a row
//pre-condition: nxb facing east
//post-condition: 1xb facing east
	private void goBack() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnAround();

	}

//pre-condition: 1x1 facing east
//post-condition: 1xm facing east

}
