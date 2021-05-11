package me.kkiomen.main.command;

import me.kkiomen.main.FilesManager;
import me.kkiomen.main.Main;
import me.kkiomen.main.help.EntityLocation;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Whisper implements CommandExecutor {

    private Main plugin;

    public Whisper(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {
        Player player = (Player)sender;


        int distanceWhisper = FilesManager.config.getInt("distanceWhisper");
        String currentCommant = "/szept";

        String distanceToString = String.valueOf(distanceWhisper);

        String messageErrorWrongCommand = ChatColor.translateAlternateColorCodes('&', FilesManager.getMessage.getString("message.chat.used").replace("%currentCommant%", currentCommant));
        String infoDistance = ChatColor.translateAlternateColorCodes('&', FilesManager.getMessage.getString("message.chat.infoDistance").replace("%distance%", distanceToString));

        String infoDistanceNoUser = ChatColor.translateAlternateColorCodes('&', FilesManager.getMessage.getString("message.chat.infoDistanceNoUsers").replace("%distance%", distanceToString));


        if (args.length == 0) {
            //player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("path.to.message").replace("%player%", player.getName()));
            player.sendMessage(messageErrorWrongCommand);
            player.sendMessage(infoDistance);
            return false;
        }
        if (args.length >= 1){
            String message = "";

            for (int i = 0; i < args.length; i++) {
                message = message + args[i] + " ";
            }
            if (message.length() == 0){
                player.sendMessage(messageErrorWrongCommand);
                player.sendMessage(infoDistance);
                return false;
            }

            EntityLocation entityloc = new EntityLocation(player);
            int countPlayer = 0;
            for (Player p : player.getWorld().getPlayers()) {
                Integer distance = entityloc.getDistance(p);
                if (distance < distanceWhisper) {
                    countPlayer++;
                    String viewChat = null;
                    try {
                        String nameUser =  "[" + plugin.database.getID(player.getUniqueId()) + "] " + plugin.database.getFirstname(player.getUniqueId()) + " " + plugin.database.getSecondName(player.getUniqueId());

                        viewChat = ChatColor.translateAlternateColorCodes('&', FilesManager.getMessage.getString("message.viewChat.whisperSendMessage").replace("%playerName%", nameUser).replace("%message%", message));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    p.sendMessage(viewChat);
                }
            }

            if(countPlayer == 1){
                player.sendMessage(infoDistanceNoUser);
            }

            return true;
        }


        return true;

    }
}
