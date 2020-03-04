# MinecraftCommandPlugin
Plugin for Minecraft which implements commands


Custom-made for a specific server, but can be adapted easily by substituting the world name in the Spawn file.

Current commands:

/setspawnpoint (set point to spawn at) (ADMIN ONLY)

/spawn (teleport to spawn)

/tp (teleport to coordinates)

/tpa (prompt another player to teleport to them)



Currently in version 0.5

To use the plugin, drop the .jar file contained in the target folder into your plugin folder in your server.



Editing the database address:

Download the project and open it in your Java/Maven editor (I use Eclipse). Open /src/main/java/io.github.maelstrom849/minecraftcommandplugin/DatabaseInfo.java. In this file change the variable host to the host name or IP address of your server. Port you can leave the same unless your database does not use the default port for SQL (3306). Name is the name of the database on the server. Mine does not currently use SSL, which is why ?useSSL=false is attached to the name. Finally, change user (username) and pass (password) to match your needs. Run the project as Maven Clean, then Maven Install, and move the newly created .jar file into the plugins folder on your server.
