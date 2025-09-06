
/*
 * File: Pyramid.java
 * Name: 
 * Section Leader: 
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

	/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

	/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

	/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;

	public void run() {
		// program places 14 bricks at the bottom, then moves y coordinate up by one
		// brick height
		// and x coordinate half of a brick width to the right and places 14-1 bricks
		// and so on until only 1 brick is placed
		for (int j = BRICKS_IN_BASE; j > 0; j--) {
			for (int i = 0; i < j; i++) {
				GRect Brick = new GRect(
						getWidth() / 2 - BRICKS_IN_BASE * BRICK_WIDTH / 2 + (BRICKS_IN_BASE - j) * BRICK_WIDTH / 2
								+ i * BRICK_WIDTH,
						getHeight() - BRICK_HEIGHT - (BRICKS_IN_BASE - j) * BRICK_HEIGHT, BRICK_WIDTH, BRICK_HEIGHT);
				add(Brick);
			}
		}
	}
}
