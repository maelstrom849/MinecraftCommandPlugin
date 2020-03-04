package io.github.maelstrom849.minecraftcommandplugin;

public class DatabaseInfo {
	
	private static String host = "mcdbs.ggservers.com";
	private static String port = "3306";
	private static String name = "user_46714?useSSL=false";
	private static String user = "user_46714";
	private static String pass = "63e1b3eb1f";
	
	public static String getAddress() {
		return "jdbc:mysql://" + host + ":" + port + "/" + name;
	}
	public static String getUsername() {
		return user;
	}
	
	public static String getPassword(){
		return pass;
	}
}
