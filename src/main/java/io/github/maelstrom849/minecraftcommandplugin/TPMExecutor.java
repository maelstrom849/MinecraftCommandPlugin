package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class TPMExecutor implements CommandExecutor {

	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public TPMExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// Command can only be run by players
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.YELLOW + "You must be a player to use this command.");
			return true;
		}

		// Sender must have basic permissions
		if (!(sender.hasPermission("all"))) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have permission to use this command.");
			return true;
		}

		// Command must have 2 arguments
		if (args.length != 2) {
			sender.sendMessage(ChatColor.YELLOW + "You must have two arguments of the form (# of map units north-south, # of map units east-west).");
			return false;
		}

		// Cast both values now so it's not done twice if they pass the check
		double delta_x = GetDouble.getDouble(args[0]);
		double delta_z = GetDouble.getDouble(args[1]);


		// Both arguments must be numbers
		if (Double.isNaN(delta_x) || Double.isNaN(delta_z)) {
			sender.sendMessage(ChatColor.YELLOW + "One or both of the arguments you provided are not numbers. They must be numbers for this command to work.");
			return false;
		}

		// Cast sender to class Player so they can be teleported
		Player p = (Player) sender;
		
		// Each map unit is 512 x 512 blocks, so this converts between them. The z axis is flipped in
		// the map (positive numbers go north, negatives go south) compared to how it is in the 
		// actual game, so that's why it's multiplied by -512. They are converted to int at this
		// point to make the function to find the highest solid Y block easier, as that requires int
		int xBlocks = (int) Math.round(delta_x * 512);
		int zBlocks = (int) Math.round(delta_z * -512);
		
		// This block figures out which direction you are heading in to display.
		String xDirection, zDirection;
		if (xBlocks < 0) {
			xDirection = "west";
		} else {
			xDirection = "east";
		}
		if (zBlocks > 0) {
			zDirection = "south";
		} else {
			zDirection = "north";
		}
		
		// This affects the message sent, which is different depending on whether you are going in one or two dimensions.
		if (xBlocks == 0) {
			sender.sendMessage(ChatColor.AQUA + "You are being teleported " + Math.abs(delta_z) + " map units (" + 
					Math.abs(zBlocks) + " blocks) " + zDirection + ".");
		} else if (zBlocks == 0) {
			sender.sendMessage(ChatColor.AQUA + "You are being teleported " + Math.abs(delta_x) + " map units (" + 
					Math.abs(xBlocks) + " blocks) " + xDirection + ".");
		} else {
			sender.sendMessage(ChatColor.AQUA + "You are being teleported " + Math.abs(delta_x) + " map units (" + 
					Math.abs(xBlocks) + " blocks) " + xDirection + " and " + Math.abs(delta_z) + " map units (" +
					Math.abs(zBlocks) + " blocks) " + zDirection + ".");
		}

		// Get the current location to manipulate
		Location current = p.getLocation();

		// Set new x and z coordinates
		int x = current.getBlockX() + xBlocks;
		int z = current.getBlockZ() + zBlocks;
		
		// Built-in function to find the highest solid block at that point. Adding one so player is
		// place on top of it
		int y = current.getWorld().getHighestBlockYAt(x, z) + 1;

		// Create destination Location
		Location destination = new Location(current.getWorld(), x, y, z);

		// Make sure destination is loaded
		sender.sendMessage(ChatColor.GOLD + "Loading...");
		do {
			destination.getChunk().load();
		} while (!destination.getChunk().isLoaded());

		// check whether the player is riding a vehicle
		if (p.getVehicle() != null)

			// if they are, teleport the vehicle to the destination as well
			p.getVehicle().teleport(destination);

		// teleport player to destination
		p.teleport(destination);
		return true;
	}
}
