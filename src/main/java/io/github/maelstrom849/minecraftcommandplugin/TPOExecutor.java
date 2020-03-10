package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class TPOExecutor implements CommandExecutor {

	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public TPOExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && sender.hasPermission("all")) {
			if (args.length == 3 && GetDouble.getDouble(args[0]) != Double.NaN &&
					GetDouble.getDouble(args[1]) != Double.NaN &&
					GetDouble.getDouble(args[2]) != Double.NaN) {
				Player p = (Player) sender;
				double xChange = GetDouble.getDouble(args[0]);
				double yChange = GetDouble.getDouble(args[1]);
				double zChange = GetDouble.getDouble(args[2]);
				Location currentLocation = p.getLocation();
				p.teleport(new Location(currentLocation.getWorld(), currentLocation.getBlockX() + xChange,
						currentLocation.getBlockY() + yChange, currentLocation.getBlockZ() + zChange));
				return true;
			} else {
				sender.sendMessage(ChatColor.YELLOW + "Something went wrong with your arguments.");
				sender.sendMessage(ChatColor.YELLOW + "Please ensure that you are submitting 3 numerical arguments.");
				return true;
			}
		} else {
			sender.sendMessage("Either you're not a person, or you don't have permission to use this. So you're basically not a person.");
			return true;
		}
	}

}
