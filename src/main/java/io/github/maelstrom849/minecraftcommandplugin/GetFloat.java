package io.github.maelstrom849.minecraftcommandplugin;

// Author: maelstrom849
// This class takes a string as input and returns the value
// of the string as a float if possible, and NaN if not

public class GetFloat {
	
	// Empty constructor because it is not needed
	public GetFloat() {}
	
	// Static method so it can be used without creating an instance of the class
	// Takes one string as params
	public static float getFloat(String s) {
		
		// Tries to convert the string to a double, returns it if possible
		try {
			float num = Float.parseFloat(s);
			if (num == Float.NEGATIVE_INFINITY || num == Float.POSITIVE_INFINITY)
				return Float.NaN;
			return num;
			
		// If it cannot make it a double, it instead returns NaN
		} catch (NumberFormatException e) {
			return Float.NaN;
		}
	}
}
