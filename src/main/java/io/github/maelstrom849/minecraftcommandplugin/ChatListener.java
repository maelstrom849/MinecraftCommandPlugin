package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ChatListener implements Listener {

	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (event.getMessage().contains("yiff")) {
			player.sendMessage("Yiff yourself, pal.");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Bukkit.broadcastMessage("Shame on " + player.getName());
			player.teleport(new Location(player.getWorld(), player.getLocation().getBlockX(),
					player.getLocation().getBlockY() + 10, player.getLocation().getBlockZ()));
		}
	}
}
