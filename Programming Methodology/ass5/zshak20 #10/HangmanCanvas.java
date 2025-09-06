
/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	private GLabel guesses;
	private GLabel incorrectChars;
	private String incorrectGuesses = "";

	/** Resets the display so that only the scaffold appears */
	public void reset() {
		removeAll();
		drawGibbet();
		guesses = null;
	}

	/**
	 * Updates the word on the screen to correspond to the current state of the
	 * game. The argument string shows what letters have been guessed so far;
	 * unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String word) {
		if (guesses == null) {
			guesses = new GLabel(word, LABEL_X, LABEL_Y);
			guesses.setFont("Rockwell-25");
			add(guesses);
		} else {
			guesses.setLabel(word);
		}
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the user. Calling
	 * this method causes the next body part to appear on the scaffold and adds the
	 * letter to the list of incorrect guesses that appears at the bottom of the
	 * window.
	 */
	public void noteIncorrectGuess(char letter, int lives) {
		if (incorrectGuesses.indexOf(letter) == -1) {
			incorrectGuesses = incorrectGuesses + letter + ", ";
		}
		if (incorrectChars == null) {
			incorrectChars = new GLabel(incorrectGuesses, LABEL_X, CHAR_LABEL_Y);
			incorrectChars.setFont("Rockwell-15");
			add(incorrectChars);
		} else {
			incorrectChars.setLabel(incorrectGuesses);
		}
		if (lives == 7) {
			drawHead();
		}
		if (lives == 6) {
			drawBody();
		}
		if (lives == 5) {
			drawLeftHand();
		}
		if (lives == 4) {
			drawRightHand();
		}
		if (lives == 3) {
			drawLeftLeg();
		}
		if (lives == 2) {
			drawRightLeg();
		}
		if (lives == 1) {
			drawLeftFoot();
		}
		if (lives == 0) {
			drawRightFoot();
		}
	}

	// draws head
	private void drawHead() {
		double HeadX = getWidth() / 8 + BEAM_LENGTH - HEAD_RADIUS / 2;
		double HeadY = getWidth() / 8 + ROPE_LENGTH;
		GOval head = new GOval(HeadX, HeadY, HEAD_RADIUS, HEAD_RADIUS);
		add(head);
	}

	// draws body
	private void drawBody() {
		double bodyX = getWidth() / 8 + BEAM_LENGTH;
		double bodyY1 = getWidth() / 8 + ROPE_LENGTH + HEAD_RADIUS;
		double bodyY2 = bodyY1 + BODY_LENGTH;
		GLine body = new GLine(bodyX, bodyY1, bodyX, bodyY2);
		add(body);
	}

	// draws left hand
	private void drawLeftHand() {
		double armX1 = getWidth() / 8 + BEAM_LENGTH;
		double armY = getWidth() / 8 + ROPE_LENGTH + HEAD_RADIUS + ARM_OFFSET_FROM_HEAD;
		double armX2 = armX1 - UPPER_ARM_LENGTH;
		double handY = armY + LOWER_ARM_LENGTH;
		GLine arm = new GLine(armX1, armY, armX2, armY);
		GLine hand = new GLine(armX2, armY, armX2, handY);
		add(arm);
		add(hand);
	}

	// draws right hand
	private void drawRightHand() {
		double armX1 = getWidth() / 8 + BEAM_LENGTH;
		double armY = getWidth() / 8 + ROPE_LENGTH + HEAD_RADIUS + ARM_OFFSET_FROM_HEAD;
		double armX2 = armX1 + UPPER_ARM_LENGTH;
		double handY = armY + LOWER_ARM_LENGTH;
		GLine arm = new GLine(armX1, armY, armX2, armY);
		GLine hand = new GLine(armX2, armY, armX2, handY);
		add(arm);
		add(hand);
	}

	// draws left leg
	private void drawLeftLeg() {
		double menjisZvaliX1 = getWidth() / 8 + BEAM_LENGTH;
		double menjisZvaliY = getWidth() / 8 + ROPE_LENGTH + BODY_LENGTH + HEAD_RADIUS;
		double menjisZvaliX2 = menjisZvaliX1 - HIP_WIDTH / 2;
		double legY = menjisZvaliY + LEG_LENGTH;
		GLine hip = new GLine(menjisZvaliX1, menjisZvaliY, menjisZvaliX2, menjisZvaliY);
		GLine leg = new GLine(menjisZvaliX2, menjisZvaliY, menjisZvaliX2, legY);
		add(hip);
		add(leg);
	}

	// draws right leg
	private void drawRightLeg() {
		double menjisZvaliX1 = getWidth() / 8 + BEAM_LENGTH;
		double menjisZvaliY = getWidth() / 8 + ROPE_LENGTH + BODY_LENGTH + HEAD_RADIUS;
		double menjisZvaliX2 = menjisZvaliX1 + HIP_WIDTH / 2;
		double legY = menjisZvaliY + LEG_LENGTH;
		GLine hip = new GLine(menjisZvaliX1, menjisZvaliY, menjisZvaliX2, menjisZvaliY);
		GLine leg = new GLine(menjisZvaliX2, menjisZvaliY, menjisZvaliX2, legY);
		add(hip);
		add(leg);
	}

	// draws left foot
	private void drawLeftFoot() {
		double footX1 = getWidth() / 8 + BEAM_LENGTH - HIP_WIDTH / 2;
		double footY = getWidth() / 8 + ROPE_LENGTH + BODY_LENGTH + HEAD_RADIUS + LEG_LENGTH;
		double footX2 = footX1 - FOOT_LENGTH;
		GLine foot = new GLine(footX1, footY, footX2, footY);
		add(foot);
	}

	// draws right foot
	private void drawRightFoot() {
		double footX1 = getWidth() / 8 + BEAM_LENGTH + HIP_WIDTH / 2;
		double footY = getWidth() / 8 + ROPE_LENGTH + BODY_LENGTH + HEAD_RADIUS + LEG_LENGTH;
		double footX2 = footX1 + FOOT_LENGTH;
		GLine foot = new GLine(footX1, footY, footX2, footY);
		add(foot);
	}

	// draws gibbet (scaffold, beam, rope)
	private void drawGibbet() {
		double scaffoldX = getWidth() / 2 - BEAM_LENGTH;
		double scaffoldY1 = getWidth() / 8;
		double scaffoldY2 = scaffoldY1 + SCAFFOLD_HEIGHT;
		double beamX = scaffoldY1 + BEAM_LENGTH;
		double ropeY = scaffoldY1 + ROPE_LENGTH;
		GLine scaffold = new GLine(scaffoldX, scaffoldY1, scaffoldX, scaffoldY2);
		GLine beam = new GLine(scaffoldX, scaffoldY1, beamX, scaffoldY1);
		GLine rope = new GLine(beamX, scaffoldY1, beamX, ropeY);
		add(scaffold);
		add(beam);
		add(rope);

	}

	/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 72;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
	private static final int LABEL_Y = 450;
	private static final int LABEL_X = 30;
	private static final int CHAR_LABEL_Y = 480;
}
