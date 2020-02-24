package io.github.maelstrom849.minecraftcommandplugin;

// Author: maelstrom849
// This class provides methods for setting a new spawn point in the world manually.

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnPointExecutor implements CommandExecutor {

	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public SetSpawnPointExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	// The class overrides the onCommand function to apply its effects
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// Make sure sender has permissions to send command
		if (sender.hasPermission("admin")) {

			// If there are 4 arguments it is [world] [x] [y] [z]
			// This command can be used by players or on the console
			if (args.length == 4) {

				// Get the world specified by the user
				World world = Bukkit.getServer().getWorld(args[0]);

				// Make sure that the world specified by the user exists, otherwise it stops
				// execution
				// and says the world was not found
				if (world == null) {
					sender.sendMessage("That world was not found.");
					return false;
				}

				// Cast the inputs (Strings) to double. The method getDouble returns NaN if the
				// string
				// cannot be cast to a double
				double x = GetDouble.getDouble(args[1]);
				double y = GetDouble.getDouble(args[2]);
				double z = GetDouble.getDouble(args[3]);

				// Check that none of the values of x, y, z are NaN. If they are, tell the user
				// and
				// stop execution
				if (x == Double.NaN || y == Double.NaN || z == Double.NaN) {
					sender.sendMessage("One or more of the numbers provided is not a number.");
					return false;
				}

				// Now that all foreseen roadblocks have been passed, create a new spawn
				// location and set
				// it as default spawn
				Spawn.setDefaultSpawn(new Spawn(world, x, y, z));

				// After setting the default spawn location in the spawn class, set it for the
				// server
				// so that new players and dead players are also affected, not just those who
				// use the
				// /spawn command
				Bukkit.getServer().getWorld(world.getName()).setSpawnLocation(Spawn.getDefaultSpawn());
				return true;

			} else if (args.length == 6) {

				// If there are 6 arguments they are of the form [world] [x] [y] [z] [pitch]
				// [yaw]
				// This command can be used by players or console
				// This case is the same as if args.length == 4 with the added float arguments
				// of pitch and yaw. getFloat works the same as getDouble so they are just added
				// in.
				World world = Bukkit.getServer().getWorld(args[0]);
				if (world == null) {
					sender.sendMessage("That world was not found.");
					return false;
				}
				double x = GetDouble.getDouble(args[1]);
				double y = GetDouble.getDouble(args[2]);
				double z = GetDouble.getDouble(args[3]);
				float pitch = GetFloat.getFloat(args[4]);
				float yaw = GetFloat.getFloat(args[5]);
				if (x == Double.NaN || y == Double.NaN || z == Double.NaN || pitch == Float.NaN || yaw == Float.NaN) {
					sender.sendMessage("One or more of the numbers provided is not a number.");
					return false;
				}
				Spawn.setDefaultSpawn(new Spawn(world, x, y, z, pitch, yaw));
				Bukkit.getServer().getWorld(world.getName()).setSpawnLocation(Spawn.getDefaultSpawn());
				return true;

			} else if (args.length == 0) {
				// If there are no arguments, then use the current location of the player using
				// the command as the new spawn. This restricts use of this command with no
				// arguments to only players, not the console, so first we make sure the use
				// is a player.
				if (sender instanceof Player) {

					// Cast sender to Player class so that we can use the getLocation method
					Player player = (Player) sender;

					// Create new spawn location comprised of the player's current location
					// We don't use the constructor that just takes the player argument because
					// it is set to the x, y, z coordinates of the world spawn now.
					Spawn spawn = new Spawn(player.getLocation().getWorld(), player.getLocation().getX(),
							player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getPitch(),
							0);

					// Make the default spawn this new value
					Spawn.setDefaultSpawn(spawn);
					Bukkit.getServer().getWorld(player.getWorld().getName()).setSpawnLocation(Spawn.getDefaultSpawn());
					return true;
				} else {

					// If this command is run from the console or any other non-player entity, this
					// is send.
					sender.sendMessage("You must be a player to use this command with no arguments.");
				}
			}
		} else {
			sender.sendMessage("You do not have permission to use this command.");
		}
		return false;
	}
}