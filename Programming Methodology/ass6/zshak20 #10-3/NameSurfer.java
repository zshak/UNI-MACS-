
/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;

public class NameSurfer extends Program implements NameSurferConstants {

	/* Method: init() */
	/**
	 * This method has the responsibility for reading in the data base and
	 * initializing the interactors at the bottom of the window.
	 */
	public void init() {
		data = new NameSurferDataBase(NAMES_DATA_FILE);
		graph = new NameSurferGraph();
		add(graph);

		JLabel name = new JLabel("name");
		add(name, SOUTH);

		field = new JTextField(20);
		add(field, SOUTH);

		JButton graph = new JButton("Graph");
		add(graph, SOUTH);

		JButton clear = new JButton("Clear");
		add(clear, SOUTH);

		addActionListeners(this);
		field.addActionListener(this);
	}

	/* Method: actionPerformed(e) */
	/**
	 * This class is responsible for detecting when the buttons are clicked, so you
	 * will have to define a method to respond to button actions.
	 */
	public void actionPerformed(ActionEvent e) {
		String text = field.getText();
		if (e.getActionCommand().equals("Graph") || e.getSource() == field) {
			NameSurferEntry Entry = data.findEntry(text);
			graph.addEntry(Entry);
			field.setText("");
		} else if (e.getActionCommand().equals("Clear")) {
			graph.clear();
		}

	}

	NameSurferDataBase data;
	private JTextField field;
	private NameSurferGraph graph;
}
