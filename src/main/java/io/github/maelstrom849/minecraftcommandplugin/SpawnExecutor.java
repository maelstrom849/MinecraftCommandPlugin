package io.github.maelstrom849.minecraftcommandplugin;

/* Author: maelstrom849
 * This class executes the /spawn command, which simply teleports the user to spawn*/

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnExecutor implements CommandExecutor {

	// Grab the full plugin in case it is necessary
	public final MinecraftCommandPlugin mcp;

	public SpawnExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	// onCommand actually executes the command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// Make sure that the user of this command is a player so that they can be
		// teleported
		if (sender instanceof Player) {
			
			// Cast sender to player so that the teleport method can be accessed
			Player player = (Player) sender;

			// If default spawn has not been set yet, it is set to the location I stored in
			// the Spawn class
			if (Spawn.getDefaultSpawn() == null)
				Spawn.setDefaultSpawn(new Spawn());

			// Teleport the player to the default spawn as stored in the Spawn class
			player.teleport(Spawn.getDefaultSpawn());
			return true;
		} else {
			sender.sendMessage("Only players can use this command");
			return false;
		}
	}
}