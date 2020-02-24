package io.github.maelstrom849.minecraftcommandplugin;

/* Author: maelstrom849
 * This class implements a mechanism to allow players
 * to teleport to each other*/

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

public class TPAExecutor implements CommandExecutor {
	
	public MinecraftCommandPlugin mcp;
	// Grab a copy of the plugin itself in case it is needed
	public TPAExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}
	
	ConversationFactory factory = new ConversationFactory(mcp);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//Make sure there is 1 argument: The name of the player the sender is teleporting to
		if (args.length != 1) {
			sender.sendMessage("That is not the correct number of arguments.");
			return false;
		}
		Player destinationPlayer = Bukkit.getServer().getPlayer(args[0]);
		//Make sure the destination player is online before executing the rest
		if (destinationPlayer == null) {
			sender.sendMessage(args[0] + " is not online.");
			return false;
		}
		//Make sure the sender has permissions for this command
		if (sender.hasPermission("MinecraftCommandPlugin.all") == false) {
			sender.sendMessage("You do not have permissions for this command");
			return false;
		}
		//Ensure the sender is a player, not the console, and start teleportation sequence
		if (sender instanceof Player) {
			//This creates a conversation between the plugin and player
			//The conversation starts with the TeleportAcceptPrompt
			//This shows the prompt text and if accepted teleports the 
			//Player to the destination player's location
			//Else nothing happens
			Conversation conv = factory
					.withFirstPrompt(new TeleportAcceptPrompt(sender, destinationPlayer))
					.withEscapeSequence("n")
					.buildConversation((Player) destinationPlayer);
			conv.begin();
			return true;
		} else {
			sender.sendMessage("Only players can use this command.");
		}
		return false;
	}

}
