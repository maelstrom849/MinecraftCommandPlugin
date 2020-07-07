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
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.YELLOW + "Only players can use this command.");
			return true;
		}
		if (!(sender.hasPermission("all"))) {
			sender.sendMessage(ChatColor.YELLOW + "You do not have permission to use this command.");
			return true;
		}

		if (args.length != 3) {
			sender.sendMessage(ChatColor.YELLOW + "You must provide 3 arguments for this to work.");
			return false;
		}

		double x_change = GetDouble.getDouble(args[0]);
		double y_change = GetDouble.getDouble(args[1]);
		double z_change = GetDouble.getDouble(args[2]);
		
		if (x_change == Double.NaN || y_change == Double.NaN || z_change == Double.NaN) {
			sender.sendMessage(ChatColor.YELLOW + "One or more of the arguments you provided is not a number.");
			return false;
		}

		Player p = (Player) sender;
		Location currentLocation = p.getLocation();
		p.teleport(new Location(currentLocation.getWorld(), currentLocation.getBlockX() + x_change,
				currentLocation.getBlockY() + y_change, currentLocation.getBlockZ() + z_change));
		return true;
	}

}
