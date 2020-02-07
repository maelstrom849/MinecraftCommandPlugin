package io.github.maelstrom849.minecraftcommandplugin;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class TeleportAcceptPrompt implements Prompt {
	private CommandSender sender;
	private Player destinationPlayer;
	
	public TeleportAcceptPrompt(CommandSender sender, Player destinationPlayer) {
		super();
		this.sender = sender;
		this.destinationPlayer = destinationPlayer;
		
	}

	@Override
	public Prompt acceptInput(ConversationContext arg0, String input) {
		if (input.equalsIgnoreCase("y")) {
			Player psend = (Player) sender;
			Location destination = destinationPlayer.getLocation();
			psend.teleport(destination);
		}
		return END_OF_CONVERSATION;
	}

	@Override
	public boolean blocksForInput(ConversationContext context) {
		// Returns true to wait for input from user
		return true;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return sender + " would like to teleplort to you. Allow this? (y or n)";
	}
}