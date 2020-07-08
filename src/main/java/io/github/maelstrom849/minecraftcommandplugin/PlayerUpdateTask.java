package io.github.maelstrom849.minecraftcommandplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerUpdateTask extends BukkitRunnable {

	private Connection connection;
	private final MinecraftCommandPlugin mcp;
	private Player player;

	public PlayerUpdateTask(MinecraftCommandPlugin mcp, Player p) {
		this.mcp = mcp;
		this.player = p;
	}

	@Override
	public void run() {
		ConversationFactory factory = new ConversationFactory(this.mcp);
		try {
			try {
				// Try opening a connection with the server.
				openConnection();
			} catch (SQLException e) {
				// If it can't be made, print the error to the console and send
				// a message to the user
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage("Connection failed.");
				return;
			}

			// General SQL statement creation
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery("SELECT PlayerName FROM Players;");
			rs.first();
			ArrayList<String> players = new ArrayList<>();
			do {
				players.add(rs.getString(1));
			} while (rs.next());
			
			
			if (!(players.contains(this.player.getName()))) {
				statement.executeUpdate("INSERT INTO Players (PlayerName) VALUES ('" + this.player.getName() + "');");
			}
			
			rs = statement.executeQuery("SELECT PersonRName FROM Players WHERE PlayerName = '" + this.player.getName() + ";");
			rs.first();
			String realName = rs.getString(1);
			if (realName == null) {
				Conversation conv = factory
						.withFirstPrompt(new RealNameInquiryPrompt(this.player))
						.withEscapeSequence("n")
						.withTimeout(45)
						.buildConversation((Player) this.player);
				conv.begin();
			} else {
				this.player.sendMessage(ChatColor.DARK_AQUA + "Welcome back " + this.player.getName() + "!");
			}
			connection.close();
			return;
			// Check for errors
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("Class not found.");
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("SQL error.");
		}

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
