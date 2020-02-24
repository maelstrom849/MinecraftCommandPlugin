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
		if (sender instanceof Player) {

			// make sure the player has the most basic permissions
			if (sender.hasPermission("all")) {

				// make sure the correct number of arguments is provided and all are numbers
				if (args.length == 3 && GetFloat.getFloat(args[0]) != Float.NaN
						&& GetFloat.getFloat(args[1]) != Float.NaN && GetFloat.getFloat(args[0]) != Float.NaN) {

					// cast sender to Player class so that we can use the getWorld(), getVehicle()
					// and teleport() methods
					Player p = (Player) sender;

					// create Location destination
					Location destination = new Location(p.getWorld(), GetFloat.getFloat(args[0]),
							GetFloat.getFloat(args[1]), GetFloat.getFloat(args[2]), p.getLocation().getPitch(),
							p.getLocation().getYaw());

					// Create a version of location up one block
					Location dest_check = new Location(p.getWorld(), GetFloat.getFloat(args[0]),
							GetFloat.getFloat(args[1]) + 1, GetFloat.getFloat(args[2]));

					// This version is to check whether the block where the player's head will be is
					// either water or air. If it is neither of those things, it will not let the
					// player teleport (they'll suffocate, or die in lava)
					if (dest_check.getBlock().isEmpty() || dest_check.getBlock().getType() == Material.WATER) {

						// check whether the player is riding a vehicle
						if (p.getVehicle() != null)

							// if they are, teleport the vehicle to the destination as well
							p.getVehicle().teleport(destination);

						// teleport player to destination
						p.teleport(destination);
						return true;
					} else {
						sender.sendMessage("Teleporting to that block will suffocate you.");
						return false;
					}

				} else {
					sender.sendMessage("You must provide 3 numbers for the xyz coordinates");
					return false;
				}
			} else {
				sender.sendMessage("You do not have this permission.");
				return false;
			}
		} else {
			sender.sendMessage("You must be a player to use this command");
			return false;
		}
	}
}