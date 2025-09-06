
/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	private final int sentinel = 0;

	public void run() {
		println("This program finds the largest and smallest numbers");
		int n = readInt();
		int max = n;
		int min = n;
		if (n == sentinel) {
			print("no number has been entered");
		} else { // if entered number is larger than "max" it takes that value, else it give
					// "min" that value
			while (true) {
				if (n == sentinel) {
					break;
				}
				if (n > max) {
					max = n;
				}
				if (n < max) {
					min = n;
				}
				n = readInt();
			}
			println("smallest=" + min);
			println("largest=" + max);
		}

	}

}