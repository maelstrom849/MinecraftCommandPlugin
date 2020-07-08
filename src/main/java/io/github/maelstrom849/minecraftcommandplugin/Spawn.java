package io.github.maelstrom849.minecraftcommandplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* Author: maelstrom849
 * This class acts as a custom way to store spawn details.
 * This mainly exists to avoid errors we were having with
 * the default way of storing spawn details and to store
 * pitch and yaw in addition to the x y z coords.*/

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class Spawn extends Location {
	private Connection connection;

	// It uses a static variable defaultSpawn to store the current spawn location
	// This can be upgraded in the future to an array, allowing the storage of multiple spawn 
	// points
	private static Spawn defaultSpawn;

	// There are 3 constructors for this class. This first one takes no arguments and just sets
	// the spawn to what it originally was in our original world
	public Spawn() {
		super(Bukkit.getServer().getWorld("Pizza Time"), 251.5, 64, 56.5, 270, 0);
	}

	// This constructor sets spawn to the location specified by its world and xyz coordinates
	public Spawn(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	// This constructor does the same as above, but adds pitch (x rotation) and yaw (y rotation)
	// to further specify the location
	public Spawn(World world, double x, double y, double z, float pitch, float yaw) {
		super(world, x, y, z, pitch, yaw);
	}

	public static void setDefaultSpawn(Spawn s) {
		defaultSpawn = s;
	}

	public static Spawn getDefaultSpawn() {
		return defaultSpawn;
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
