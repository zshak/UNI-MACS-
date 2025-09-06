
/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas implements NameSurferConstants, ComponentListener {

	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */
	public NameSurferGraph() {
		addComponentListener(this);
		// You fill in the rest //
	}

	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		Entry = new ArrayList<NameSurferEntry>();
		update();
	}

	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display. Note that
	 * this method does not actually draw the graph, but simply stores the entry;
	 * the graph is drawn by calling update.
	 */
	public void addEntry(NameSurferEntry entry) {
		if (entry != null && !(Entry.contains(entry))) {
			Entry.add(entry);
			update();
		}
	}

	// adds statistics template
	private void addBackground() {
		for (int i = 0; i < NDECADES; i++) {
			double temp = (getWidth() / NDECADES);
			double xCoordinate = temp * i;
			double yCoordinate = getHeight();
			GLine line = new GLine(xCoordinate, 0, xCoordinate, yCoordinate);
			add(line);

			int year = START_DECADE + i * 10;
			String text = String.valueOf(year);
			GLabel yearLabel = new GLabel(text);
			add(yearLabel, xCoordinate, getHeight());
		}
		for (int i = 0; i < 2; i++) {
			double y = offset + i * (getHeight() - 2 * offset);
			GLine line = new GLine(0, y, getWidth(), y);
			add(line);
		}
	}

	// draws statistics for every entry
	private void drawStatistics() {
		for (int i = 0; i < Entry.size(); i++) {
			NameSurferEntry person = Entry.get(i);
			double y = offset + (getHeight() - 2 * offset) * ((double) person.getRank(0) / 1000);
			double x = 0;
			for (int j = 1; j < NDECADES; j++) {
				int rank = person.getRank(j);
				double x1 = j * (getWidth() / NDECADES);
				double y1 = (offset + (getHeight() - 2 * offset) * ((double) rank / 1000));
				GLine line = new GLine(x, y, x1, y1);
				if (rank != 0) {
					add(line);
				}
				line.setColor(color(i));
				y = y1;
				x = x1;
			}
			for (int j = 0; j < NDECADES; j++) {
				int rank = person.getRank(j);
				double x2 = j * (getWidth() / NDECADES);
				double y2 = (offset + (getHeight() - 2 * offset) * ((double) rank / 1000));
				if (rank == 0) {
					GLabel name = new GLabel(person.getName() + "*");
					add(name, x2, getHeight() - offset);
					name.setColor(color(i));
				} else {
					GLabel name = new GLabel(person.getName() + rank);
					add(name, x2, y2);
					name.setColor(color(i));
				}
			}
		}
	}

	// returns color according to given value
	private Color color(int i) {
		if (i % 4 == 0) {
			return Color.black;
		}
		if (i % 4 == 1) {
			return Color.red;
		}
		if (i % 4 == 2) {
			return Color.blue;
		}
		return Color.yellow;

	}

	/**
	 * Updates the display image by deleting all the graphical objects from the
	 * canvas and then reassembling the display according to the list of entries.
	 * Your application must call update after calling either clear or addEntry;
	 * update is also called whenever the size of the canvas changes.
	 */
	public void update() {
		removeAll();
		addBackground();
		drawStatistics();
	}

	private static final double offset = 15;

	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
		update();
	}

	public void componentShown(ComponentEvent e) {
	}

	private ArrayList<NameSurferEntry> Entry = new ArrayList<NameSurferEntry>();
}
