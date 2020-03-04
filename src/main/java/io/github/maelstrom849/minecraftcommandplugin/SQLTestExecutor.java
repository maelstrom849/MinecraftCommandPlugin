package io.github.maelstrom849.minecraftcommandplugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SQLTestExecutor implements CommandExecutor {
	private Connection connection;
	
	// All executors pass a copy of the plugin itself in case it is needed
	public final MinecraftCommandPlugin mcp;

	public SQLTestExecutor(MinecraftCommandPlugin mcp) {
		this.mcp = mcp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		OfflinePlayer[] offline_players = Bukkit.getServer().getOfflinePlayers();
		Collection online_players = Bukkit.getServer().getOnlinePlayers();
		try {
			try {
				openConnection();
			} catch (SQLException e) {
				e.printStackTrace();
				sender.sendMessage("Connection failed.");
				return true;
			}
			sender.sendMessage("Connection succeeded.");
			Statement statement = connection.createStatement();
			for (int i = 0; i < offline_players.length; i++) {
				statement.executeUpdate("INSERT INTO Players (PlayerName) VALUES ('" + offline_players[i].getName() + "');");
				sender.sendMessage("Added " + i);
			}
			for (Object o : online_players) {
				Player p = (Player) o;
				statement.executeUpdate("INSERT INTO Players (PlayerName) Values ('" + p.getName()+ "');");
				sender.sendMessage("Added online");
			}
			sender.sendMessage("The operation was done.");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			sender.sendMessage("Class not found.");
		} catch (SQLException e) {
			e.printStackTrace();
			sender.sendMessage("SQL error.");
		}
		return false;
	}
	
	public void openConnection() throws SQLException, ClassNotFoundException{
		if (connection != null && !connection.isClosed()) {
			return;
		}
		
		synchronized (this) {
			if (connection != null && !connection.isClosed()) {
				return;
			}
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(DatabaseInfo.getAddress(), DatabaseInfo.getUsername(), DatabaseInfo.getPassword());
		}
	}
}
