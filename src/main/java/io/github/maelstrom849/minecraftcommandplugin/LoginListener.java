package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginListener implements Listener {
	
	private final MinecraftCommandPlugin mcp;
	
	public LoginListener(MinecraftCommandPlugin plugin) {
		this.mcp = plugin;
		this.mcp.getServer().getPluginManager().registerEvents(this, mcp);
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		new PlayerUpdateTask(this.mcp, event.getPlayer()).runTaskLater(this.mcp, 40);
	}

}
