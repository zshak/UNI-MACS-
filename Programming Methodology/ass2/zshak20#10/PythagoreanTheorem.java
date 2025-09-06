
/*
 * File: PythagoreanTheorem.java
 * Name: 
 * Section Leader: 
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	public void run() {
		println("Enter values to compute Pythagorean Theorem:");
		int a = readInt();
		int b = readInt();
		double aSquared = a * a;
		double bSquared = b * b;
		double cSquared = aSquared + bSquared; // adds squared values
		double c = Math.sqrt(cSquared); // takes square root from the squared value
		println(c);
	}
}
