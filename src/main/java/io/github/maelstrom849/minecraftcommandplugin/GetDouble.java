package io.github.maelstrom849.minecraftcommandplugin;

// Author: maelstrom849
// This class takes a string as input and returns the value
// of the string as a double if possible, and NaN if not

public class GetDouble {
	
	// Empty constructor because it is not needed
	public GetDouble() {}
	
	// Static method so it can be used without creating an instance of the class
	// Takes one string as params
	public static double getDouble(String s) {
		
		// Tries to convert the string to a double, returns it if possible
		try {
			double num = Double.parseDouble(s);
			// For the purposes of this plugin, we don't want infinites
			if (num == Double.POSITIVE_INFINITY || num == Double.NEGATIVE_INFINITY)
				return Double.NaN;
			return num;
			
		// If it cannot make it a double, it instead returns NaN
		} catch (NumberFormatException e) {
			return Double.NaN;
		}
	}
}
