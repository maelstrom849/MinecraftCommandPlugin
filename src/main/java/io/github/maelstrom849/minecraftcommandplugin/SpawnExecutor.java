package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnExecutor implements CommandExecutor {
	
	public final MinecraftCommandPlugin mcp;
	
	public SpawnExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("spawn")) {
			Player player = (Player) sender;
			World defaultWorld = player.getLocation().getWorld();
			float yaw = 270;
			float pitch = 0;
			Location spawn = new Location(defaultWorld, 251.5, 64, 56.5, yaw, pitch);
			player.teleport(spawn);
			return true;
		}
		return false;
	}
}