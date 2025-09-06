
/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {

	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
		// You fill this in
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		if (currentMessage != null) {
			remove(currentMessage);
		}
		currentMessage = new GLabel(msg);
		currentMessage.setFont(MESSAGE_FONT);
		double x = getWidth() / 2 - currentMessage.getWidth() / 2;
		double y = getHeight() - BOTTOM_MESSAGE_MARGIN;
		add(currentMessage, x, y);
	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		if (profile != null) {
			displayName(profile);
			displayPicture(profile);
			displayStatus(profile);
			displayFriend(profile);
		}
	}

	//displays label "friends" and friendlist
	private void displayFriend(FacePamphletProfile profile) {
		GLabel friends = new GLabel("Friends: ", getWidth() / 2, IMAGE_MARGIN);
		add(friends);

		Iterator<String> it = profile.getFriends();
		double y = IMAGE_MARGIN + friends.getAscent() + 5;
		while (it.hasNext()) {
			GLabel label = new GLabel(it.next(), getWidth() / 2, y);
			add(label);
			y += label.getAscent() + 5;
		}

	}

	//displays status if it exists, displays message accordingly
	private void displayStatus(FacePamphletProfile profile) {
		double x = LEFT_MARGIN;
		double y = TOP_MARGIN + IMAGE_MARGIN + IMAGE_HEIGHT + STATUS_MARGIN;
		if (profile.getStatus() == null) {
			GLabel status = new GLabel("No current status", x, y);
			status.setFont(PROFILE_STATUS_FONT);
			add(status);
		} else {
			GLabel status = new GLabel(profile.getName() + " is " + profile.getStatus());
			status.setFont(PROFILE_STATUS_FONT);
			add(status, x, y);
			showMessage("Status update to " + profile.getStatus());
		}

	}

	//displays profile picture or empty rectangle
	private void displayPicture(FacePamphletProfile profile) {
		if (profile.getImage() == null) {
			addRect();
		} else {
			addImage(profile.getImage());
		}

	}

	//adds profile picture
	private void addImage(GImage image) {
		image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
		add(image, LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN);

	}

	//adds empty rectangle
	private void addRect() {
		GRect rect = new GRect(LEFT_MARGIN,TOP_MARGIN + IMAGE_MARGIN, IMAGE_WIDTH, IMAGE_HEIGHT);
		add(rect);

		GLabel text = new GLabel("No image");
		double x = LEFT_MARGIN + rect.getWidth() / 2 - text.getWidth() / 2;
		double y = IMAGE_MARGIN + rect.getHeight() / 2 + text.getAscent() / 2;
		add(text, x, y);
	}

	//display user's name
	private void displayName(FacePamphletProfile profile) {
		GLabel name = new GLabel(profile.getName());
		name.setColor(Color.blue);
		name.setFont(PROFILE_NAME_FONT);
		add(name, LEFT_MARGIN, TOP_MARGIN);
	}

	GLabel currentMessage;
}
