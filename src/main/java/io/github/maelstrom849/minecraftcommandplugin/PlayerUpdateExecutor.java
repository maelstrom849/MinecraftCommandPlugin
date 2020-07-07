package io.github.maelstrom849.minecraftcommandplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerUpdateExecutor implements CommandExecutor {
	private Connection connection;

	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public PlayerUpdateExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// This command can be sent both by a player with the admin privilege or by the console
		if (sender.hasPermission("admin") || sender.getName().equalsIgnoreCase("maelstrom849")) {
			
			// Here we create blank lists of online and offline players.
			// Unfortunately because of how bukkit stores player types, we can't combine
			// them into one list. Maybe in the future.
			OfflinePlayer[] offline_players = Bukkit.getServer().getOfflinePlayers();
			Collection<? extends Player> online_players = Bukkit.getServer().getOnlinePlayers();
			try {
				try {
					// Try opening a connection with the server.
					openConnection();
				} catch (SQLException e) {
					// If it can't be made, print the error to the console and send
					// a message to the user
					e.printStackTrace();
					sender.sendMessage("Connection failed.");
					return true;
				}
				
				// General SQL statement creation
				Statement statement = connection.createStatement();
				
				// This number tracks how many new players will be added to the database
				int added = 0;
				
				// Iterate through offline players
				for (int i = 0; i < offline_players.length; i++) {
					// Get the row corresponding with the player name
					ResultSet rs = statement.executeQuery(
							"SELECT PlayerName FROM Players WHERE PlayerName = '" + offline_players[i].getName() + "';");
					
					// The first() method returns true if there is a first element in the list, or false
					// if there is not. Since each playername is unique, the call above will always return either
					// 1 or 0 entries.
					boolean alreadyExists = rs.first();
					// If player does not already exist, add them to the table
					if (alreadyExists == false) {
						statement.executeUpdate(
								"INSERT INTO Players (PlayerName) VALUES ('" + offline_players[i].getName() + "');");
						// Increase number of players added
						added++;
					}
				}
				// Do the same for online players. The syntax is different because of how the players are stored in the system.
				for (Object o : online_players) {
					// Cast (? extends Player) to be Player
					Player p = (Player) o;
					ResultSet rs = statement
							.executeQuery("SELECT PlayerName FROM Players WHERE PlayerName = '" + p.getName() + "';");
					boolean alreadyExistsOnline = rs.first();
					if (alreadyExistsOnline == false) {
						statement.executeUpdate("INSERT INTO Players (PlayerName) VALUES ('" + p.getName() + "');");
						added++;
					}
				}
				
				// Alert sender about how many players were added.
				sender.sendMessage(
						"The update has been completed. " + added + " new players were added to the database.");
				connection.close();
				return true;
				// Check for errors
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				sender.sendMessage("Class not found.");
			} catch (SQLException e) {
				e.printStackTrace();
				sender.sendMessage("SQL error.");
			}
			return false;
		} else {
			// This bit of code is executed if a player who does not have permission tries to use the command
			sender.sendMessage("You do not have permission to use this command.");
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
