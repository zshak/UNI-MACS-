
/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {

	public void run() {
		moveToTheDoor();
		pickUpNewspaper();
		moveBack();
	}

	// karel moves in front of the door facing east
	// pre-condition: top left corner of the house (3x4 in the assignment world
	// version)
	// post-condition: in front of the door facing east (5x3)
	private void moveToTheDoor() {
		moveFront();
		turnRight();
		standAtTheDoor();

	}

	// karel moves to the opposite wall
	// pre-condition: anywhere, facing any direction
	// post-condition: the opposite wall facing in the same direciton
	private void moveFront() {
		while (frontIsClear()) {
			move();
		}
	}

	// karel moves along the wall and turns east at the door
	// pre-condition: top right corner 5x4 facing south
	// post-condition: 5x3 facing east
	private void standAtTheDoor() {
		while (leftIsBlocked()) {
			move();
		}
		turnLeft();

	}

	// karel moves front by a block distance, picks beeper and returns to the start
	// state
	// pre-condition: in front of the door facing east (5x3), beeper placed in front
	// (6x3)
	// post-condition: 5x3 facing north
	private void pickUpNewspaper() {
		move();
		pickBeeper();
		turnAround();
		move();
		turnRight();
	}

	// karel returns to the starting state
	// pre-condition: in front of the door facing north (5x3)
	// post-condition: starting state, top corner of the house (3x4) facing east
	private void moveBack() {
		moveFront();
		turnLeft();
		moveFront();
		turnAround();
	}

	// pre-condition: 3x4 facing east
	// post-condition: 3x4 facing east
}
