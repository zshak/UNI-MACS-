
/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.event.*;
import javax.swing.*;

public class FacePamphlet extends Program implements FacePamphletConstants {

	/**
	 * This method has the responsibility for initializing the interactors in the
	 * application, and taking care of any other initialization that needs to be
	 * performed.
	 */
	public void init() {
		canvas = new FacePamphletCanvas();
		add(canvas);

		name = new JTextField(TEXT_FIELD_SIZE);
		add(name, NORTH);

		JButton add = new JButton("Add");
		add(add, NORTH);

		JButton delete = new JButton("Delete");
		add(delete, NORTH);

		JButton lookUp = new JButton("LookUp");
		add(lookUp, NORTH);

		status = new JTextField(TEXT_FIELD_SIZE);
		add(status, WEST);

		JButton statusButton = new JButton("Change Status");
		add(statusButton, WEST);

		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		picture = new JTextField(TEXT_FIELD_SIZE);
		add(picture, WEST);

		JButton pictureButton = new JButton("Change Picture");
		add(pictureButton, WEST);

		add(new JLabel(EMPTY_LABEL_TEXT), WEST);

		friend = new JTextField(TEXT_FIELD_SIZE);
		add(friend, WEST);

		JButton friendButton = new JButton("Add Friend");
		add(friendButton, WEST);

		addActionListeners(this);
		name.addActionListener(this);
		status.addActionListener(this);
		friend.addActionListener(this);
		picture.addActionListener(this);
	}

	/**
	 * This class is responsible for detecting when the buttons are clicked or
	 * interactors are used, so you will have to add code to respond to these
	 * actions.
	 */
	public void actionPerformed(ActionEvent e) {
		String temp = e.getActionCommand();
		String search = name.getText();
		if (temp.equals("Add")) {
			addProfile(search);
		}
		if (temp.equals("Delete")) {
			deleteProfile(search);
		}
		if (temp.equals("LookUp")) {
			lookUp(search);
		}
		if (temp.equals("Change Status") || e.getSource() == status) {
			changeStatus(status.getText());
		}
		if (temp.equals("Change Picture") || e.getSource() == picture) {
			changePicture(picture.getText());
		}
		if (temp.equals("Add Friend") || e.getSource() == friend && currentProfile != null) {
			addFriend(friend.getText());
		}
	}

	// searches profile if it displays it unless it doesn't exist
	private void lookUp(String search) {
		if (data.containsProfile(search)) {
			currentProfile = data.getProfile(search);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("Displaying " + search);
		} else {
			canvas.removeAll();
			currentProfile=null;
			canvas.showMessage("profile with name " + search + " doesn't exist");
		}

	}

	// deletes profile if it exists
	private void deleteProfile(String search) {
		if (data.containsProfile(search)) {
			data.deleteProfile(search);
			currentProfile = null;
			canvas.removeAll();
			canvas.showMessage("profile deleted");
		} else {
			canvas.showMessage("profile doesn't exist");
		}

	}

	// adds profile if it doesn't exist already
	private void addProfile(String search) {
		if (data.containsProfile(search)) {
			currentProfile = data.getProfile(search);
			canvas.displayProfile(currentProfile);
			canvas.showMessage("profile already exists");
		} else {
			FacePamphletProfile user = new FacePamphletProfile(search);
			data.addProfile(user);
			currentProfile = user;
			canvas.displayProfile(currentProfile);
			canvas.showMessage("new profile created");
		}

	}

	// changes status if there is an active profile
	private void changeStatus(String temp) {
		if (currentProfile != null) {
			if (!(temp.equals("")))
				currentProfile.setStatus(temp);
			canvas.displayProfile(currentProfile);
		}
	}

	// changes profile, if filename can be found
	private void changePicture(String temp) {
		if (currentProfile != null) {
			GImage image = null;
			try {
				image = new GImage(temp);
			} catch (ErrorException ex) {
			}
			if (image != null) {
				currentProfile.setImage(image);
				canvas.displayProfile(currentProfile);
				canvas.showMessage("Picture updated");
			}else {
				canvas.showMessage("Unable to open image file: " + temp);
			}
		}else {
			canvas.showMessage("Please select a profile");
		}
	}

	//adds searched user to current profile's friendlist/ adds current profile to searched user's friendlsit
	private void addFriend(String friend) {
		if (data.containsProfile(friend)) {
			if (currentProfile.addFriend(friend)) {
				data.getProfile(friend).addFriend(currentProfile.getName());
				canvas.displayProfile(currentProfile);
				canvas.showMessage(friend + " Added as a friend");
			} else {
				canvas.showMessage(currentProfile.getName() + " already has " + friend + " as a friend");
			}
		} else {
			canvas.showMessage(friend + " doesn't exist");
		}
	}

	private FacePamphletDatabase data = new FacePamphletDatabase();
	private FacePamphletProfile currentProfile = null;
	private JTextField name;
	private JTextField status;
	private JTextField picture;
	private JTextField friend;
	private FacePamphletCanvas canvas;
}
