
/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here does nothing.
 * When you finish writing it, it should solve the "repair the quad"
 * problem from Assignment 1.  In addition to editing the program,
 * you should be sure to edit this comment so that it no longer
 * indicates that the program does nothing.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {
	public void run() {
		repairColumns();
	}

	private void repairColumns() {
		fillAndMove();
		fillColumn();
		moveBack();
	}

//	karel repairs every column except the last one
//	pre-condition: 1x1 facing east
//	post-condition: (1+4k)x1 facing east (not having repaired the last column)
	private void fillAndMove() {
		while (frontIsClear()) {
			fillColumn();
			moveToNextColumn();
		}
	}

//	fills a single column with beepers 
//	pre-condition: (1+4k)x1 facing east
//	post-condition: the highest point of the (1+4k)th column facing north
	private void fillColumn() {
		turnLeft();
		while (frontIsClear()) {
			putBeeperIfNoBeeper();
			move();
		}
		putBeeperIfNoBeeper();
	}

//puts beepers if none is present
//pre-condition: (1+4k)x1 column facing north
//post-condition: top of the (1+4k)th column facing north having put all the beepers
	private void putBeeperIfNoBeeper() {
		if (noBeepersPresent()) {
			putBeeper();
		}
	}

//karel moves to the next column
//pre-conditon: top of the (1+4k)th column facing north
//post-condition: (1+4(k+1))x1 facing east
	private void moveToNextColumn() {
		moveBack();
		if (frontIsClear()) {
			goFourBlocks();
		}
	}

//karel moves by 4 blocks
//pre-condition: (1+4k)x1 facing east
//post condition: (1+4(k+1))x1 facing east
	private void goFourBlocks() {
		for (int i = 0; i < 4; i++) {
			move();
		}

	}

//karel moves down from the top of the column
//pre-condition: top of the (1+4k)th column facing north
//post-condition: (1+4k)x1 facing east
	private void moveBack() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnLeft();
	}
//	pre-condition: 1x1 facing east
//	post-condition: nx1 facing east
}
