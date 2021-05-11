package me.kkiomen.region;

import me.kkiomen.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class RentRegion implements Listener {

    private Main plugin;

    public RentRegion(Main plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase("[wynajem]"))
            if (p.hasPermission("region.rent")) {
                if (e.getLine(1).isEmpty()) {
                    p.sendMessage(ChatColor.RED + "Napisz nazwe regionu");
                    e.setCancelled(true);
                } else {
                    String nameRegion = e.getLine(1);
                    e.setLine(0, "§1[Wynajem]");
                    e.setLine(1, ChatColor.DARK_GRAY +"Kliknij i sprawdź");
                    e.setLine(2, ChatColor.DARK_GRAY +"szcegóły");
                    e.setLine(3, nameRegion);
                    p.sendMessage(ChatColor.GREEN + "Poprawnie dodano tabliczke");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permission to create!");
                e.setCancelled(true);
            }
    }




}
