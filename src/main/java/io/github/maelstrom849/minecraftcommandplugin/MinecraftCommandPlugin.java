package io.github.maelstrom849.minecraftcommandplugin;

/*
 * Author: maelstrom849
 * This plugin is a custom-built plugin that fulfills many of the common wants
 * of people who run minecraft servers, and is updated as features are requested*/

import org.bukkit.plugin.java.JavaPlugin;

// The needed extension of the java plugin class
public final class MinecraftCommandPlugin extends JavaPlugin {
	
	// onEnable is run whenever the plugin is started
	@Override
	public void onEnable() {
		// Each command is tied to the proper Executor class here
		this.getCommand("spawn").setExecutor(new SpawnExecutor(this));
		this.getCommand("tpa").setExecutor(new TPAExecutor(this));
		this.getCommand("setspawnpoint").setExecutor(new SetSpawnPointExecutor(this));
		this.getCommand("tp").setExecutor(new TPExecutor(this));
		this.getCommand("playerupdate").setExecutor(new PlayerUpdateExecutor(this));
		this.getCommand("tpo").setExecutor(new TPOExecutor(this));
		this.getCommand("tpm").setExecutor(new TPMExecutor(this));
		
		// set up the listener for the chat
		// TODO ADD THIS IN LATER UPDATE
		//Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), this);
	}
	
	@Override
	public void onDisable() {
		// Nothing really happens here yet
	}
}
