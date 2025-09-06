
/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {
	private final double Radius1 = 72;
	private final double Radius2 = Radius1 * 165 / 254;
	private final double Radius3 = Radius1 * 76 / 254;

	public void run() {
		drawCircle1();
		drawCircle2();
		drawCircle3();
	}

	private void drawCircle1() { // draws circle with the same centre as the other 2 circles.
		GOval circle1 = new GOval(getWidth() / 2 - Radius1, getHeight() / 2 - Radius1, 2 * Radius1, 2 * Radius1);
		add(circle1);
		circle1.setFilled(true);
		circle1.setColor(Color.red);
		circle1.setFillColor(Color.red);
	}

	private void drawCircle2() { // same centre, different radius
		GOval circle2 = new GOval(getWidth() / 2 - Radius2, getHeight() / 2 - Radius2, 2 * Radius2, 2 * Radius2);
		add(circle2);
		circle2.setFilled(true);
		circle2.setColor(Color.white);
		circle2.setFillColor(Color.white);
	}

	private void drawCircle3() { // same centre, smaller radius
		GOval circle3 = new GOval(getWidth() / 2 - Radius3, getHeight() / 2 - Radius3, 2 * Radius3, 2 * Radius3);
		add(circle3);
		circle3.setFilled(true);
		circle3.setColor(Color.red);
		circle3.setFillColor(Color.red);
	}
}
