package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class TPMExecutor implements CommandExecutor {

	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public TPMExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player && sender.hasPermission("all")) {
			if (args.length == 2 && GetDouble.getDouble(args[0]) != Double.NaN
					&& GetDouble.getDouble(args[1]) != Double.NaN) {
				Player p = (Player) sender;
				double xChange = GetDouble.getDouble(args[0]) * 512;
				double zChange = GetDouble.getDouble(args[1]) * 512;
				sender.sendMessage(xChange + " " + zChange);
				Location currentLocation = p.getLocation();
				int newY = 50;
				Location newLocation = new Location(currentLocation.getWorld(), currentLocation.getBlockX() + xChange,
						newY, currentLocation.getBlockZ() + zChange);
				Location newHeadLocation = new Location(currentLocation.getWorld(),
						currentLocation.getBlockX() + xChange, newY + 1, currentLocation.getBlockZ() + zChange);
				// Make sure the player's head is not in a block or underwater
				while (!((newLocation.getBlock().isEmpty()) || (newLocation.getBlock().getType() == Material.WATER))
						&& !(newHeadLocation.getBlock().isEmpty())) {
					newY++;
					newLocation.setY(newY);
					newHeadLocation.setY(newY + 1);
				}
				Location finalLocation = new Location(currentLocation.getWorld(), currentLocation.getX() + xChange,
						newY+1, currentLocation.getZ() + zChange);
				p.setInvulnerable(true);
				p.sendMessage("invulnerable.");
				finalLocation.getChunk().load();
				p.sendMessage("loading chunk.");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					p.sendMessage("Uh oh.");
					e.printStackTrace();
				}
				p.teleport(finalLocation);
				p.sendMessage("teleporting");
				/*try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					p.sendMessage("Uh oh.");
					e.printStackTrace();
				}*/
				p.setInvulnerable(false);
				p.sendMessage("no longer invulnerable.");
				return true;
			} else {
				sender.sendMessage(ChatColor.YELLOW + "Something went wrong with your arguments.");
				sender.sendMessage(ChatColor.YELLOW + "Please ensure that you are submitting 2 numerical arguments.");
				return true;
			}
		} else {
			sender.sendMessage(
					"Either you're not a person, or you don't have permission to use this. So you're basically not a person.");
			return true;
		}
	}
}
