
/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {

	public void run() {
		putBeeper();
		putBeeperAtTheEnd();
		checkTheWorld1();
		narrowARow();
	}

//karel puts a beeper at the end of a row
//pre-condition: 1x1 facing east
//post-condition: nx1 facing west having put down a beeper
	private void putBeeperAtTheEnd() {
		goToWall();
		putBeeper();
		turnAround();
	}

	private void goToWall() {
		while (frontIsClear()) {
			move();
		}
	}

//special case for the program is a 1xm and 2xm world, that's why karel looks for a wall.
//if the 1xm world is presented then karel picks one of the 2 beepers he has put down
//if the 2xm world is presented then karel picks the beeper and moves to the second beeper he has placed	
//if the world is different from 1xm and 2xm, karel proceeds normally 
	private void checkTheWorld1() {
		if (frontIsClear()) {
			move();
			checkTheWorld2();
		} else {
			pickBeeper();
		}
	}

	private void checkTheWorld2() {
		if (frontIsBlocked()) {
			pickBeeper();
			turnAround();
			move();
		}
	}

//karel narrows the world by putting the end beepers 1 block closer to the mid point, until he finishes the task
	private void narrowARow() {
		while (noBeepersPresent()) {
			pickTheEndBeeper();
		}
	}

//karel either finishes finding the midpoint or narrows the row
	private void pickTheEndBeeper() {
		move();
		finishTheTask();
	}

//if the end points are 1 block away from each other, karel will be standing on one of them 
//and he knows that the middle block is the midpoint 
//and finishes the task or continues narrowing 
	private void finishTheTask() {
		if (beepersPresent()) {
			pickBeeper();
			turnAround();
			move();
			move();
			moveBeeper();
		} else {
			continueNarrowing();
		}
	}

//karel puts an end beeper 1 block closer to the middle
	private void continueNarrowing() {
		while (noBeepersPresent()) {
			move();
		}
		moveBeeper();
		move();
	}

//karel moves the end beeper 1 block closer to the middle (if he is standing on it)
	private void moveBeeper() {
		pickBeeper();
		turnAround();
		move();
		putBeeper();
	}
//	program works by karel narrowing the distance between the end points 
//	until the distance becomes 1 block. karel checks a beeper after moving 2 blocks 
//	when he finally finds a beeper he knows where the middle point is
//	pre-condition:1x1 facing east
//	post-condition: if n is even: (n/2+1)x 1 facing east
//					if n is odd:  (n/2+0.5)x1 facing west
}
