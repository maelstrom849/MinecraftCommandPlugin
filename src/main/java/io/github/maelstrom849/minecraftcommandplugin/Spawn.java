package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Spawn extends Location {
	public Spawn(Player player) {
		super(player.getLocation().getWorld(), 251.5, 64, 56.5, 270, 0);
	}
}
