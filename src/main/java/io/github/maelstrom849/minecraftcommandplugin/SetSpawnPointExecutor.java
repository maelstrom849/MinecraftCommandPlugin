package io.github.maelstrom849.minecraftcommandplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Author: maelstrom849
// This class provides methods for setting a new spawn point in the world manually.

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnPointExecutor implements CommandExecutor {

	private Connection connection;
	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public SetSpawnPointExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	// The class overrides the onCommand function to apply its effects
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Make sure sender has permissions to send command
		if (!(sender.hasPermission("admin") || sender.getName().equalsIgnoreCase("maelstrom849"))) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have permission to use this command");
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to use this command.");
			return true;
		}
		
		Player player = (Player) sender;

		// If there are 3 arguments it is [x] [y] [z]
		// This command can be used by players or on the console
		if (args.length == 3) {
		

			// Get the user's world
			World world = player.getLocation().getWorld();


			// Cast the inputs (Strings) to double. The method getDouble returns NaN if the
			// string
			// cannot be cast to a double
			double x = GetDouble.getDouble(args[0]);
			double y = GetDouble.getDouble(args[1]);
			double z = GetDouble.getDouble(args[2]);

			// Check that none of the values of x, y, z are NaN. If they are, tell the user
			// and
			// stop execution
			if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) {
				sender.sendMessage(ChatColor.YELLOW + "One or more of the numbers provided is not a number.");
				return false;
			}

			// Now that all foreseen roadblocks have been passed, create a new spawn
			// location and set
			// it as default spawn

			try {
				try {
					// Try opening a connection with the server.
					openConnection();
				} catch (SQLException e) {
					// If it can't be made, print the error to the console and send
					// a message to the user
					e.printStackTrace();
					sender.sendMessage(ChatColor.YELLOW + "Connection failed. New spawn was unable to be stored.");
				}
				Statement stmt = connection.createStatement();
				ResultSet spawns = stmt.executeQuery("SELECT idSpawn FROM Spawn;");
				spawns.last();
				int entry_num = spawns.getInt("idSpawn")+1;
				stmt.executeUpdate("INSERT INTO Spawn (idSpawn, SpawnWorld, SpawnX, SpawnY, SpawnZ) VALUES (" + entry_num + ", '" + world.getName() + "', "
						+ x + ", " + y + ", " + z + ");");

				// After setting the default spawn location in the database, set it for the
				// server
				// so that new players and dead players are also affected, not just those who
				// use the
				// /spawn command
				Bukkit.getServer().getWorld(world.getName()).setSpawnLocation(new Location (world, x, y, z));

				sender.sendMessage(ChatColor.GOLD + "The new spawn point has been set in the server and stored in the plugin's database.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				sender.sendMessage("Class not found.");
			} catch (SQLException e) {
				e.printStackTrace();
				sender.sendMessage("SQL error after connecting.");
			}
			return true;

		} else if (args.length == 5) {

			// If there are arguments they are of the form [x] [y] [z] [pitch] [yaw]
			// This command can be used by players or console
			// This case is the same as if args.length == 3 with the added float arguments
			// of pitch and yaw. getFloat works the same as getDouble so they are just added
			// in.
			
			// Get the user's world
			World world = player.getLocation().getWorld();
			double x = GetDouble.getDouble(args[0]);
			double y = GetDouble.getDouble(args[1]);
			double z = GetDouble.getDouble(args[2]);
			float pitch = GetFloat.getFloat(args[3]);
			float yaw = GetFloat.getFloat(args[4]);
			if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z) || Float.isNaN(pitch) || Float.isNaN(yaw)) {
				sender.sendMessage("One or more of the arguments provided is not a number.");
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
					sender.sendMessage(ChatColor.YELLOW + "Connection failed. New spawn was unable to be stored.");
				}
				Statement stmt = connection.createStatement();
				ResultSet spawns = stmt.executeQuery("SELECT idSpawn FROM Spawn;");
				spawns.last();
				int entry_num = spawns.getInt("idSpawn")+1;
				stmt.executeUpdate("INSERT INTO Spawn (idSpawn, SpawnWorld, SpawnX, SpawnY, SpawnZ, SpawnPitch, SpawnYaw) VALUES (" + entry_num + ", '" + world.getName() + "', "
						+ x + ", " + y + ", " + z + ", " + pitch + ", " + yaw + ");");

				// After setting the default spawn location in the database, set it for the
				// server
				// so that new players and dead players are also affected, not just those who
				// use the
				// /spawn command
				Bukkit.getServer().getWorld(world.getName()).setSpawnLocation(new Location (world, x, y, z, pitch, yaw));

				sender.sendMessage(ChatColor.GOLD + "The new spawn point has been set in the server and stored in the plugin's database.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				sender.sendMessage("Class not found.");
			} catch (SQLException e) {
				e.printStackTrace();
				sender.sendMessage("SQL error after connecting.");
			}
			return true;

		} else if (args.length == 0) {

			// Create new spawn location comprised of the player's current location
			// We don't use the constructor that just takes the player argument because
			// it is set to the x, y, z coordinates of the world spawn now.
			Location location = player.getLocation();
			World world = location.getWorld();
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			float pitch = location.getPitch();
			float yaw = location.getYaw();

			try {
				try {
					// Try opening a connection with the server.
					openConnection();
				} catch (SQLException e) {
					// If it can't be made, print the error to the console and send
					// a message to the user
					e.printStackTrace();
					sender.sendMessage(ChatColor.YELLOW + "Connection failed. New spawn was unable to be stored.");
				}
				Statement stmt = connection.createStatement();
				ResultSet spawns = stmt.executeQuery("SELECT idSpawn FROM Spawn;");
				spawns.last();
				int entry_num = spawns.getInt("idSpawn")+1;
				stmt.executeUpdate("INSERT INTO Spawn (idSpawn, SpawnWorld, SpawnX, SpawnY, SpawnZ, SpawnPitch, SpawnYaw) VALUES (" + entry_num + ", '" + world.getName() + "', "
						+ x + ", " + y + ", " + z + ", " + pitch + ", " + yaw + ");");

				// After setting the default spawn location in the database, set it for the
				// server
				// so that new players and dead players are also affected, not just those who
				// use the
				// /spawn command
				Bukkit.getServer().getWorld(world.getName()).setSpawnLocation(new Location (world, x, y, z, pitch, yaw));

				sender.sendMessage(ChatColor.GOLD + "The new spawn point has been set in the server and stored in the plugin's database.");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				sender.sendMessage("Class not found.");
			} catch (SQLException e) {
				e.printStackTrace();
				sender.sendMessage("SQL error after connecting.");
			}
			return true;
		} else {
			sender.sendMessage(ChatColor.YELLOW + "Something went wrong in a way I didn't foresee.");
			sender.sendMessage(ChatColor.YELLOW + "Send Milo something in the discord to get this fixed.");
			return false;
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