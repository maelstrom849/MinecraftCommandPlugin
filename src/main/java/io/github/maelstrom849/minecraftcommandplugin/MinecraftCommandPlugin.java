package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftCommandPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getCommand("spawn").setExecutor(new SpawnExecutor(this));
		this.getCommand("tpa").setExecutor(new TPAExecutor(this));
	}
	
	@Override
	public void onDisable() {
		// Nothing really happens here yet
	}
}
