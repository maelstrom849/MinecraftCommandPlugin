package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPExecutor implements CommandExecutor {

	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public TPExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// This command can only be used by a player
		if (!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to use this command");
			return true;
		}

		// make sure the player has the most basic permissions
		if (!(sender.hasPermission("all"))) {
			sender.sendMessage("You do not have this permission.");
			return true;
		}

		// make sure the correct number of arguments is provided
		if (args.length != 3 && args.length != 2) {
			sender.sendMessage("You do not have the correct number of arguments.");
			sender.sendMessage("You must provide either two or three numbers for the (x, z) or (x, y, z) coordinates.");
			return true;
		}

		for (int i=0;i<args.length;i++) {
			double test = GetDouble.getDouble(args[i]);
			if (test == Double.NaN) {
				sender.sendMessage("One or more of the arguments you provided are not numbers.");
				return false;
			}
		}

		if (args.length == 2) {
			double x = GetDouble.getDouble(args[0]);
			double z = GetDouble.getDouble(args[1]);

			Player p = (Player) sender;

			int y = p.getWorld().getHighestBlockYAt((int) Math.round(x), (int) Math.round(z));

			// create Location destination
			Location destination = new Location(p.getWorld(), x, y, z);

			// check whether the player is riding a vehicle
			if (p.getVehicle() != null)

				// if they are, teleport the vehicle to the destination as well
				p.getVehicle().teleport(destination);

			// teleport player to destination
			p.teleport(destination);
			return true;
		} else if (args.length == 3) {

			double x = GetDouble.getDouble(args[0]);
			double y = GetDouble.getDouble(args[1]);
			double z = GetDouble.getDouble(args[2]);

			// cast sender to Player class so that we can use the getWorld(), getVehicle()
			// and teleport() methods
			Player p = (Player) sender;

			// create Location destination
			Location destination = new Location(p.getWorld(), x, y, z);

			// Create a version of location up one block
			Location dest_check = new Location(p.getWorld(), x, y+1, z);

			// This version is to check whether the block where the player's head will be is
			// either water or air. If it is neither of those things, it will not let the
			// player teleport (they'll suffocate, or die in lava)
			if (!(dest_check.getBlock().isEmpty() || dest_check.getBlock().getType() == Material.WATER)) {
				sender.sendMessage("Teleporting to " + x + ", " + y + ", " + z + " will suffocate you.");
				return true;
			}

			// check whether the player is riding a vehicle
			if (p.getVehicle() != null)

				// if they are, teleport the vehicle to the destination as well
				p.getVehicle().teleport(destination);

			// teleport player to destination
			p.teleport(destination);
			return true;
		} else {
			sender.sendMessage("Wow, something went wrong in a way I didn't foresee.");
			sender.sendMessage("Let me know (Milo in the Discord) if you see this message.");
			return false;
		}
	}
}