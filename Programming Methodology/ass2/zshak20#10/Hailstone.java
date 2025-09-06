
/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	private final int sentinel = 1;

	public void run() {
		int n = readInt("Enter a natural number:");
		int n1 = 0;
		if (n <= 0) {
			print("your number isn't positive");
		} else {
			while (true) { // while breaks when "1" is reached
				if (n == sentinel) {
					break;
				}
				if (n % 2 == 0) { // if value is even command makes it half and gives "n" that value
					print(n + " is even so i take half: ");
					n = n / 2;
					n1++;
					println(n);
				} else { // if it's even it makes 3n+1 and gives "n" that value
					print(n + " is odd so i make 3n+1: ");
					n = 3 * n + 1;
					n1++;
					println(n);
				}
			}
			println("the process took " + n1 + " to reach 1");
		}
	}
}
