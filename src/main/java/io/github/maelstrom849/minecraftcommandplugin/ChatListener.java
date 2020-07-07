package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ChatListener implements Listener {

	@EventHandler
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (event.getMessage().contains("yiff")) {
			player.sendMessage("Yiff yourself, pal.");
			Bukkit.broadcastMessage("Shame on " + player.getName());
		}
	}
}
