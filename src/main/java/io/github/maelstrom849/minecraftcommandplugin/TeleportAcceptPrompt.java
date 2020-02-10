package io.github.maelstrom849.minecraftcommandplugin;

/* Author: maelstrom849
 * This class is the prompt that players will have to answer 
 * about whether they want another player to teleport to them.
 * If they answer "y" then the player will be teleported,
 * otherwise they will not */

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

	// This method gets the input from the user, which is either "y" (teleport)
	// or not (don't teleport). Here is where the teleportation happens
	@Override
	public Prompt acceptInput(ConversationContext arg0, String input) {
		if (input.equalsIgnoreCase("y")) {
			Player psend = (Player) sender;
			Location destination = destinationPlayer.getLocation();
			psend.teleport(destination);
		}
		// Regardless of the answer, there are no further prompts here, so 
		// this is the end of the conversation
		return END_OF_CONVERSATION;
	}

	// This method tells the prompt whether to wait for input or not
	@Override
	public boolean blocksForInput(ConversationContext context) {
		// Returns true to wait for input from user
		return true;
	}

	// this prompt returns the text to display for the beginning prompt
	@Override
	public String getPromptText(ConversationContext arg0) {
		return sender + " would like to teleplort to you. Allow this? (y or n)";
	}
}