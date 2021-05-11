package me.kkiomen.main.events;


import me.kkiomen.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class CancelTakeItemFromInventoryGUI implements Listener {

    Main plugin;
    Inventory inv;
    String type;

    public CancelTakeItemFromInventoryGUI(Inventory inv, Main m, String type){
        this.inv = inv;
        this.plugin = m;
        this.type = type;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) throws SQLException {
        Inventory invClose = event.getInventory();
        Player player = (Player) event.getPlayer();
        if (invClose.equals(inv) && inv != null && !inv.equals(null)){
            //player.sendMessage("you close your inventory");
            String sql = "UPDATE users SET "+ type + "=? WHERE nick=?;";
            PreparedStatement stmt = plugin.database.connection.prepareStatement(sql);
            stmt.setString(1, "false");
            stmt.setString(2, String.valueOf(player.getName()));
            stmt.executeUpdate();
        }else{
            //player.sendMessage("cos innego zamknules");
        }
    }
}
