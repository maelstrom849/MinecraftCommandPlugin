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
		if (delta_x == Double.NaN || delta_z == Double.NaN) {
			sender.sendMessage(ChatColor.YELLOW + "One or both of the arguments you provided are not numbers. They must be numbers for this command to work.");
			return false;
		}

		// 
		Player p = (Player) sender;
		double xBlocks = delta_x * 512;
		double zBlocks = delta_z * -512;
		String xDirection, zDirection;
		if (xBlocks < 0) {
			xDirection = "West";
		} else {
			xDirection = "East";
		}
		if (zBlocks < 0) {
			zDirection = "South";
		} else {
			zDirection = "North";
		}
		if (xBlocks == 0) {
			sender.sendMessage(ChatColor.AQUA + "You are being teleported " + Math.abs(delta_z) + " map units (" + 
					Math.abs(zBlocks) + " blocks) " + zDirection + ".");
		} else if (zBlocks == 0) {
			sender.sendMessage(ChatColor.AQUA + "You are being teleported " + Math.abs(delta_x) + " map units (" + 
					Math.abs(xBlocks) + " blocks) " + xDirection + ".");
		} else {
			sender.sendMessage(ChatColor.AQUA + "You are being teleported " + Math.abs(delta_x) + " map units (" + 
					Math.abs(xBlocks) + " blocks) " + xDirection + " and " + Math.abs(delta_z) + "map units (" +
					Math.abs(zBlocks) + " blocks)" + zDirection + ".");
		}
		
		Location current = p.getLocation();
		
		int x = (int) Math.abs(current.getBlockX() + xBlocks);
		int z = (int) Math.abs(current.getBlockZ() + zBlocks);
		int y = current.getWorld().getHighestBlockYAt(x, z);
		
		Location destination = new Location(current.getWorld(), x, y, z);
		
		do {
			destination.getChunk().load();
		} while (!destination.getChunk().isLoaded());
		return true;
	}
}
