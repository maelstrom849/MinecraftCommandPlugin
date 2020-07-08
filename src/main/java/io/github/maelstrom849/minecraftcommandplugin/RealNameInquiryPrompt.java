package io.github.maelstrom849.minecraftcommandplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class RealNameInquiryPrompt implements Prompt {
	private Player p;
	private Connection connection;
	
	public RealNameInquiryPrompt(Player player) {
		super();
		this.p = player;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return "What is your real name? (This is for the /checkname command so we all know who we are irl)";
	}

	@Override
	public boolean blocksForInput(ConversationContext context) {
		return true;
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		if (input.length() > 50) {
			input = input.substring(0, 50);
		}
		try {
			try {
				// Try opening a connection with the server.
				openConnection();
			} catch (SQLException e) {
				// If it can't be made, print the error to the console and send
				// a message to the user
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage("Connection failed.");
				return END_OF_CONVERSATION;
			}
			Statement s = connection.createStatement();
			s.executeUpdate("UPDATE Players SET PersonRName = '" + input + "' WHERE PlayerName = '" + p.getName() + "';");
			p.sendMessage("Thanks!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("Class not found.");
			return END_OF_CONVERSATION;
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("SQL error.");
			return END_OF_CONVERSATION;
		}
		return END_OF_CONVERSATION;
	}
	
	// This function opens the connection to the database and maintains it.
	public void openConnection() throws SQLException, ClassNotFoundException {
		if (connection != null && !connection.isClosed()) {
			return;
		}

		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DatabaseInfo.getAddress(), DatabaseInfo.getUsername(),
					DatabaseInfo.getPassword());
		}
	}
}
