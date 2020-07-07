package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class TPOExecutor implements CommandExecutor {

	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public TPOExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		// Sender must be a player to use this command
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.YELLOW + "Only players can use this command.");
			return true;
		}
		
		// Sender must have the "all" permission to use this command
		if (!(sender.hasPermission("all"))) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have permission to use this command.");
			return true;
		}

		// Sender must provide 3 arguments (x, y, z)
		if (args.length != 3) {
			sender.sendMessage(ChatColor.YELLOW + "You must provide 3 arguments for this to work.");
			return false;
		}

		// Cast all to double so they can be used to teleport
		double x_change = GetDouble.getDouble(args[0]);
		double y_change = GetDouble.getDouble(args[1]);
		double z_change = GetDouble.getDouble(args[2]);
		
		// Make sure that all of them are actually numbers
		if (Double.isNaN(x_change) || Double.isNaN(y_change) || Double.isNaN(z_change)) {
			sender.sendMessage(ChatColor.YELLOW + "One or more of the arguments you provided is not a number.");
			return false;
		}

		// Cast sender to player so that teleport can be used
		Player p = (Player) sender;
		
		// Get current location so you don't have to get it 4 times in the next line, then add
		// the differences
		Location currentLocation = p.getLocation();
		Location destination = new Location(currentLocation.getWorld(), currentLocation.getBlockX() + x_change,
				currentLocation.getBlockY() + y_change, currentLocation.getBlockZ() + z_change);
		
		// Load destination
		sender.sendMessage(ChatColor.GOLD + "Loading...");
		do {
			destination.getChunk().load();
		} while (!(destination.getChunk().isLoaded()));
		
		// Teleport
		p.teleport(destination);
		return true;
	}
}
