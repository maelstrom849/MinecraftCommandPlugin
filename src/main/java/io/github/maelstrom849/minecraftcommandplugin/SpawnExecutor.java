package io.github.maelstrom849.minecraftcommandplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/* Author: maelstrom849
 * This class executes the /spawn command, which simply teleports the user to spawn*/

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnExecutor implements CommandExecutor {

	private Connection connection;

	// Grab the full plugin in case it is necessary
	public final MinecraftCommandPlugin mcp;

	public SpawnExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	// onCommand actually executes the command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// Make sure that the user of this command is a player so that they can be
		// teleported
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.YELLOW + "Only players can use this command");
			return true;
		}

		// Make sure sender has the basic permissions
		if (!(sender.hasPermission("all"))) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have permission to use this command.");
			return false;
		}

		// Cast sender to player so that the teleport method can be accessed
		Player player = (Player) sender;

		try {
			try {
				// Try opening a connection with the server.
				openConnection();
			} catch (SQLException e) {
				// If it can't be made, print the error to the console and send
				// a message to the user
				e.printStackTrace();
				sender.sendMessage(ChatColor.YELLOW + "Connection failed. Sending you to original spawn.");
				// If default spawn has not been set yet, it is set to the location I stored in
				// the Spawn class
				Location spawn = new Location(Bukkit.getServer().getWorld("Pizza Time"), 251.5, 64, 56.5, 270, 0);

				// Load default spawn if it is not loaded
				sender.sendMessage(ChatColor.GOLD + "Loading...");
				do {
					spawn.getChunk().load();
				} while (!(spawn.getChunk().isLoaded()));

				// Teleport the player to the default spawn as stored in the Spawn class
				player.teleport(spawn);
				return true;
			}
			
			Statement statement = connection.createStatement();
			sender.sendMessage(ChatColor.GOLD + "Getting spawn from database...");
			ResultSet id_spawn_list = statement.executeQuery("SELECT idSpawn FROM Spawn;");
			id_spawn_list.last();
			int most_recent_spawn_id = id_spawn_list.getInt("idSpawn");
			ResultSet spawn_info = statement.executeQuery("SELECT * FROM Spawn WHERE idSpawn = " + most_recent_spawn_id + ";");
			spawn_info.first();
			
			String world = spawn_info.getString("SpawnWorld");
			double x = spawn_info.getDouble("SpawnX");
			double y = spawn_info.getDouble("SpawnY");
			double z = spawn_info.getDouble("SpawnZ");
			float pitch = spawn_info.getFloat("SpawnPitch");
			float yaw = spawn_info.getFloat("SpawnYaw");
			
			Location spawn = new Location(Bukkit.getServer().getWorld(world), x, y, z, pitch, yaw);
			
			// Load spawn if it is not loaded
			sender.sendMessage(ChatColor.GOLD + "Loading...");
			do {
				spawn.getChunk().load();
			} while (!(spawn.getChunk().isLoaded()));

			// Teleport the player to the default spawn as stored in the Spawn class
			player.teleport(spawn);
			connection.close();
			return true;
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.YELLOW + "Class not found.");
		} catch (SQLException e) {
			e.printStackTrace();
			sender.sendMessage(ChatColor.YELLOW + "SQL error after connecting.");
		}
		return true;
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