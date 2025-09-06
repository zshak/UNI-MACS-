
/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class ProgramHierarchy extends GraphicsProgram {
	private int rectWidth = 150;
	private int rectHeight = 100;
	private int topToBottom = 50;
	private int topToSides = 25;
	private int height = 500;
	private int width = 1000;

	public void run() {
		this.setSize(width, height); // sets canvas size
		drawHierarchy();
	}

	private void drawHierarchy() { // draws hierarchy
		drawTop();
		drawBottom();
		addText();
	}

	private void drawTop() { // draws top part of hierarchy
		int topRectX = (width - rectWidth) / 2; // x coordinate is half of rectangle width to left
		int topRectY = topToBottom; // this distance is arbitrary
		GRect rect = new GRect(rectWidth, rectHeight);
		add(rect, topRectX, topRectY);
	}

	private void drawBottom() { // draws bottom part of hierarchy
		int botHeight = topToBottom + rectHeight + topToBottom; // height of the 3 rectangles are the same
		int botRectX1 = (width - rectWidth) / 2 - topToSides - rectWidth; // sets the x coordinate for the first
																			// rectangle
		for (int i = 0; i < 3; i++) { // places 3 rectangle at the same height
			GRect rect = new GRect(botRectX1 + i * (rectWidth + topToSides), botHeight, rectWidth, rectHeight);
			add(rect);
		}
		connect();
	}

	private void connect() { // connects 4 rectangles
		int lineX = (width - rectWidth) / 2 + rectWidth / 2; // starting x and y coordinate for the lines are the same
		int lineY = topToBottom + rectHeight;
		int lineYBot = 2 * topToBottom + rectHeight; // final y coordinate is also the same
		int lineX1 = lineX - rectWidth - topToSides;
		for (int i = 0; i < 3; i++) { // draws 3 lines
			GLine line = new GLine(lineX, lineY, lineX1 + i * (rectWidth + topToSides), lineYBot);
			add(line);
		}

	}

	private void addText() { // adds text in the renctangles
		GLabel program = new GLabel("Program");
		int topTextX = (int) (width / 2 - program.getWidth() / 2);
		int topTextY = (int) (topToBottom + rectHeight / 2 + program.getHeight() / 2);
		add(program, topTextX, topTextY);

		GLabel graphics = new GLabel("GraphicsProgram");
		int botTextY = (int) (2 * topToBottom + rectHeight + rectHeight / 2 + graphics.getHeight() / 2);
		int botTextX1 = (int) (width / 2 - topToSides - rectWidth - graphics.getWidth() / 2);
		add(graphics, botTextX1, botTextY);

		GLabel console = new GLabel("ConsoleProgram");
		int botTextX2 = (int) (width / 2 - console.getWidth() / 2);
		add(console, botTextX2, botTextY);

		GLabel dialog = new GLabel("DialogProgram");
		int botTextX3 = (int) (width / 2 + topToSides + rectWidth - dialog.getWidth() / 2);
		add(dialog, botTextX3, botTextY);

	}

}
