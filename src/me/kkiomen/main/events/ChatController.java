package me.kkiomen.main.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatController implements Listener {

    private String value;

    public String getValue(){
        return this.value;
    }

    int tmp = 0;


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String chatMessage = event.getMessage();
        this.value = chatMessage;

        player.sendMessage("Podane imie to: e" + ChatColor.LIGHT_PURPLE + chatMessage);

        if (chatMessage.equalsIgnoreCase("Start")) {
            event.setCancelled(true);
            player.sendMessage("asdsadas");
        }
    }
}
