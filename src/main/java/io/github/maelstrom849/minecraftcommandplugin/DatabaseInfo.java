package io.github.maelstrom849.minecraftcommandplugin;

public class DatabaseInfo {
	
	// host is the IP address or host name for the MySQL server
	private static String host = "mcdbs.ggservers.com";
	// We use default SQL port 3306
	private static String port = "3306";
	// This is the name of the database to use. ?useSSL=false after the name specifies 
	//not to use SSL to connect since the server does not have that set up
	private static String name = "user_46714?useSSL=false";
	// Username to connect to the server
	private static String user = "user_46714";
	// Password to connect to the server
	private static String pass = "**********";
	
	// Combines host, port, and name to get the full address of our database
	public static String getAddress() {
		return "jdbc:mysql://" + host + ":" + port + "/" + name;
	}
	
	// Returns the string username
	public static String getUsername() {
		return user;
	}
	
	// Returns the string username
	public static String getPassword(){
		return pass;
	}
}
