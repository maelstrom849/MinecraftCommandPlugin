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
import org.bukkit.entity.Player;

public class RevertSpawnPointExecutor implements CommandExecutor {

	private Connection connection;
	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public RevertSpawnPointExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// Command can only be used by a player
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.YELLOW + "You must be a player to use this command.");
			return true;
		}
		
		// Player must have admin permissions to use this command
		if (!(sender.hasPermission("admin") || sender.getName().equalsIgnoreCase("maelstrom849"))) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have permission to use this command.");
			return true;
		}
		
		// Command takes no arguments
		if (args.length != 0) {
			sender.sendMessage(ChatColor.YELLOW + "Please do not add anything after the command.");
			return false;
		}
		
		try {
			try {
				// Try opening a connection with the server.
				openConnection();
			} catch (SQLException e) {
				// If it can't be made, print the error to the console and send
				// a message to the user
				e.printStackTrace();
				sender.sendMessage(ChatColor.YELLOW + "Connection failed. New spawn was unable to be removed.");
				return true;
			}
			
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Spawn;");
			rs.last();
			int mostRecent = rs.getInt("idSpawn");
			if (mostRecent == 0) {
				sender.sendMessage(ChatColor.YELLOW + "Only one spawn location remains. Cannot be reverted further.");
				return true;
			} else {
				stmt.executeUpdate("DELETE FROM Spawn WHERE idSpawn = " + mostRecent + ";");
				sender.sendMessage(ChatColor.GOLD + "The most recent Spawn setting (" + rs.getString("SpawnX") +
						", " + rs.getString("SpawnY") + ", " + rs.getString("SpawnZ") + ") has been " + ChatColor.RED + "removed.");
				rs.previous();
				sender.sendMessage(ChatColor.GOLD + "The new/old spawn location is " + ChatColor.DARK_GREEN + 
						"(" + rs.getString("SpawnX") + ", " + rs.getString("SpawnY") + ", " + rs.getString("SpawnZ") + ")");
				return true;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.YELLOW + "Class not found.");
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.YELLOW + "SQL error after connecting.");
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
