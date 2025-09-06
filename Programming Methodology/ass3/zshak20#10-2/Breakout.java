
/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import acm.graphics.GLabel;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;
	private static final Color RED_CLR = Color.RED;
	private static final Color ORANGE_CLR = Color.ORANGE;
	private static final Color YELLOW_CLR = Color.YELLOW;
	private static final Color GREEN_CLR = Color.GREEN;
	private static final Color CYAN_CLR = Color.CYAN;

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		initializeGame();
		waitForClick();
		startGame();
	}

	private void initializeGame() {
		// bricks,ball and paddle are added. Game waits for player to start.
		drawBricks();
		drawPaddle();
		drawBall();
	}

	private void startGame() {
		// after a mouse click ball and paddle start moving
		addMouseListeners();
		moveBall();
	}

	private void drawBricks() {
		/*
		 * bricks are set up in a row. they take color according to their row number
		 * every new y coordinate increases by separation distance + brick width
		 */
		for (int j = 1; j < 11; j++) {
			for (int i = 0; i < NBRICKS_PER_ROW; i++) {
				double brickXOffset = BRICK_SEP + i * (BRICK_WIDTH + BRICK_SEP);
				GRect brick = new GRect(brickXOffset, BRICK_Y_OFFSET + j * (BRICK_HEIGHT + BRICK_SEP), BRICK_WIDTH,
						BRICK_HEIGHT);
				brick.setColor(color(j));
				brick.setFilled(true);
				add(brick);
			}
		}
	}

	private Color color(int j) {
		/*
		 * bricks take color according to their row number 1 and 2 - red, 3 and 4 -
		 * orange, 5 and 6 - yellow, 7 and 8 - green, 9 and 10 - cyan
		 */
		if (j % 10 == 1 || j % 10 == 2) {
			return RED_CLR;
		} else if (j % 10 == 3 || j % 10 == 4) {
			return ORANGE_CLR;
		} else if (j % 10 == 5 || j % 10 == 6) {
			return YELLOW_CLR;
		} else if (j % 10 == 7 || j % 10 == 8) {
			return GREEN_CLR;
		}
		return CYAN_CLR;
	}

	private void drawPaddle() {
		// adds paddle
		Paddle = new GRect(getWidth() / 2 - PADDLE_WIDTH / 2, getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT,
				PADDLE_WIDTH, PADDLE_HEIGHT);
		Paddle.setFilled(true);
		add(Paddle);
	}

	public void mouseMoved(MouseEvent e) {
		/*
		 * only x coordinate of the mouse is considered when moving paddle if mouse
		 * moves so that paddle goes out of the window, it is automatically set in a
		 * correct location
		 */
		int x = e.getX();
		int y = getHeight() - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		if (x < PADDLE_WIDTH / 2) {
			Paddle.setLocation(0, y);
		} else if (x > getWidth() - PADDLE_WIDTH / 2) {
			Paddle.setLocation(getWidth() - PADDLE_WIDTH, y);
		} else {
			Paddle.setLocation(x - PADDLE_WIDTH / 2, y);
		}
	}

	private void drawBall() {
		// adds ball
		Ball = new GOval(getWidth() / 2 - BALL_RADIUS, getHeight() / 2 - BALL_RADIUS, 2 * BALL_RADIUS, 2 * BALL_RADIUS);
		Ball.setFilled(true);
		add(Ball);
	}

	private void moveBall() {
		/*
		 * ball is given a starting velocity randomly it has to stop moving if the
		 * player has no turns left or if they have already won the game
		 */
		vx = rgen.nextDouble(1.0, 3.0);
		vy = 3.0;
		if (rgen.nextBoolean(0.5))
			vx = -vx;
		while (true) {
			if (turns == 0) {
//				add(text("you lose!"));
				break;
			} else if (brickCount == 0) {
//				add(text("you win!"));
				break;
			}
			ballMovement();
		}
	}

	private void ballMovement() {
		/*
		 * ball changes vx when it hits left or right wall it changes vy when it hits
		 * upper wall if it hits lower wall, the turns are decreased the movement ends
		 * when there are no more turns or bricks left
		 */
		Ball.move(vx, vy);
		pause(10);
		breakBricks();
		if (hitRightWall() || hitLeftWall()) {
			vx = -vx;
		}
		if (hitUpWall()) {
			vy = -vy;
		}
		if (hitDownWall()) {
			turns--;
			remove(Paddle);
			remove(Ball);
			drawPaddle();
			drawBall();
			pause(2000);
			moveBall();
		}
	}

	// returns whether the ball hit a certain wall
	private boolean hitRightWall() {
		return Ball.getX() + 2 * BALL_RADIUS >= getWidth();
	}

	private boolean hitLeftWall() {
		return Ball.getX() <= 0;
	}

	private boolean hitUpWall() {
		return Ball.getY() <= 0;
	}

	private boolean hitDownWall() {
		return Ball.getY() >= getHeight();
	}

	private GObject getCollidingObject() {
		// returns object that is at any one of the four rectangle points that surround
		// circle
		double point1x = Ball.getX();
		double point1y = Ball.getY();
		double point2x = point1x + 2 * BALL_RADIUS;
		double point2y = point1y + 2 * BALL_RADIUS;
		GObject obj1 = getElementAt(point1x, point1y);
		GObject obj2 = getElementAt(point2x, point1y);
		GObject obj3 = getElementAt(point1x, point2y);
		GObject obj4 = getElementAt(point2x, point2y);
		if (obj1 != null) {
			return obj1;
		}
		if (obj2 != null) {
			return obj2;
		}
		if (obj3 != null) {
			return obj3;
		}
		return obj4;
	}

	private void breakBricks() {
		/*
		 * if circle hits any object except paddle, that object is removed. corners are
		 * considered points where ball's centre x coordinate is smaller or larger than
		 * paddle's x coordinates
		 */
		GObject collider = getCollidingObject();
		double pointCentreX = Ball.getX() + BALL_RADIUS;
		double pointCentreY = Ball.getY() + BALL_RADIUS;
		boolean corner1 = pointCentreX < Paddle.getX() & vx > 0;
		boolean corner2 = vx < 0 & pointCentreX > (Paddle.getX() + PADDLE_WIDTH);
		// checks if the centre of the ball is in the paddle range
		boolean paddleRange = pointCentreX >= Paddle.getX() & pointCentreX <= (Paddle.getX() + PADDLE_WIDTH);
		/*
		 * vy>0 is needed so that ball doesn't get stuck in a paddle the ball drops into
		 * the void if it hits the side of the paddle
		 */
		if (vy > 0 & vx > 0 & collider == Paddle & corner1) {
			vx = -vx;
		} else if (vy > 0 & vx < 0 & collider == Paddle & corner2) {
			vx = -vx;
			/*
			 * program needs to check if the centre of the ball is lower than paddle surface
			 * so that it doesn't return the ball from the void
			 */
		} else if (vy > 0 & !(pointCentreY > Paddle.getY()) & collider == Paddle & (paddleRange)) {
			vy = -vy;
		} else if (collider != null & collider != Paddle ) {
			remove(collider);
			brickCount--;
			vy = -vy;
		}
	}

	private GRect Paddle;
	private GOval Ball;
	private double vx, vy;
	private int turns = NTURNS;
	private int brickCount = NBRICKS_PER_ROW * NBRICK_ROWS;
	private RandomGenerator rgen = RandomGenerator.getInstance();
}
