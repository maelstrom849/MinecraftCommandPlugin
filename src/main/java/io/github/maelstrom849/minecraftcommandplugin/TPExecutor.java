package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

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
			sender.sendMessage(ChatColor.YELLOW + "You must be a player to use this command");
			return true;
		}

		// make sure the player has the most basic permissions
		if (!(sender.hasPermission("all"))) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have this permission.");
			return true;
		}

		// make sure the correct number of arguments is provided
		if (args.length != 3 && args.length != 2) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have the correct number of arguments.");
			sender.sendMessage(ChatColor.YELLOW + "You must provide either two or three numbers for the (x, z) or (x, y, z) coordinates.");
			return true;
		}

		// Make sure all of the arguments provided are numbers. This is done with my GetDouble class,
		// which returns Double.NaN if an error occurs due to them not being numbers.
		// I don't assign them yet because the command can be executed with two or three inputs
		for (int i=0;i<args.length;i++) {
			double test = GetDouble.getDouble(args[i]);
			if (test == Double.NaN) {
				sender.sendMessage(ChatColor.YELLOW + "One or more of the arguments you provided are not numbers.");
				return false;
			}
		}

		// This code executes if two arguments are provided. They will represent the x and z coordinates, the y
		// coordinate will be deduced from the world
		if (args.length == 2) {
			
			// cast both to double and then to int. Int is necessary so that the function
			// getHighestBlockYAt works properly
			int x = (int) Math.abs(GetDouble.getDouble(args[0]));
			int z = (int) Math.abs(GetDouble.getDouble(args[1]));

			// Cast sender to class Player so that teleport can be used
			Player p = (Player) sender;

			// Built-in function to find highest non-air block at a location
			int y = p.getWorld().getHighestBlockYAt(x, z);

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

			// Casting to double is fine here because finding the y block is unnecessary
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
				sender.sendMessage(ChatColor.YELLOW + "Teleporting to " + x + ", " + y + ", " + z + " will suffocate you.");
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
			
			// If somehow the code gets to here, it's because it got past all the checks I could think of.
			// I'll need to work out exactly what happened to get it here.
			sender.sendMessage(ChatColor.YELLOW + "Wow, something went wrong in a way I didn't foresee.");
			sender.sendMessage(ChatColor.YELLOW + "Let me know (Milo in the Discord) if you see this message.");
			return false;
		}
	}
}