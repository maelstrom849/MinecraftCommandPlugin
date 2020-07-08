package io.github.maelstrom849.minecraftcommandplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckNameExecutor implements CommandExecutor {

	private Connection connection;
	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public CheckNameExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender.hasPermission("all"))) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have permission to use this command.");
			return true;
		}

		if (args.length != 1) {
			sender.sendMessage(ChatColor.YELLOW + "You must provide a player name to check the real name of.");
			return false;
		}
		try {
			try {
				openConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				sender.sendMessage(ChatColor.YELLOW + "Could not connect to the database.");
				return true;
			}
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT PersonRName FROM Players WHERE PlayerName = '" + args[0] + "';");
			if (rs.first()) {
				sender.sendMessage(ChatColor.DARK_GREEN + args[0] + ChatColor.RED + " is " + 
						ChatColor.DARK_GREEN + rs.getString("PersonRName"));
			} else {
				sender.sendMessage(ChatColor.YELLOW + "This user has not entered their real name in the database yet.");
			}
			return true;
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.YELLOW + "Class not found.");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.YELLOW + "Error. This most likely means that the username you entered does not exist in the database.");
			return true;
		}
	}

	// This function opens the connection to the database and maintains it.
	public void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}

		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DatabaseInfo.getAddress(), DatabaseInfo.getUsername(),
					DatabaseInfo.getPassword());
		}
	}
}
