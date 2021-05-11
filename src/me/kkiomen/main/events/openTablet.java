package me.kkiomen.main.events;

import me.kkiomen.main.FilesManager;
import me.kkiomen.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class openTablet implements Listener {

    private Main plugin;

    public openTablet(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void openCurrentTablet(PlayerInteractEvent e) throws SQLException {
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) ||  e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
        {

            //NORMAL TABLET
            try{
                if( e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "TABLET") &&
                        !e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(null) &&
                        e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null
                )
                {

                    String typeTABLETtable = "openTablet";

                    Player p = e.getPlayer();
                    Inventory gui = Bukkit.createInventory(p, 9, "TABLET");
                    ItemStack item1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                    ItemMeta item1_meta = item1.getItemMeta();
                    String nameItemsTablet = ChatColor.translateAlternateColorCodes('&', FilesManager.getMessage.getString("message.infoItemsTablet"));
                    item1_meta.setDisplayName(nameItemsTablet);
                    ArrayList<String> itemLore = new ArrayList<>();
                    itemLore.add(ChatColor.GRAY + "Gdy wejdziesz do panelu, odswierz strone!");
                    itemLore.add(ChatColor.GREEN + "Aby teblet dzialal na stronie,");
                    itemLore.add(ChatColor.GREEN + "musisz go miec otworzonego na serwerze");
                    item1_meta.setLore(itemLore);
                    item1.setItemMeta(item1_meta);

                    gui.setItem(0,item1);
                    gui.setItem(1,item1);
                    gui.setItem(2,item1);
                    gui.setItem(3,item1);
                    gui.setItem(4,item1);
                    gui.setItem(5,item1);
                    gui.setItem(6,item1);
                    gui.setItem(7,item1);
                    gui.setItem(8,item1);
                    p.openInventory(gui);
                    //p.sendMessage(ChatColor.GREEN + "OPEN");

                    String sql = "UPDATE users SET "+ typeTABLETtable + "=? WHERE nick=?;";
                    PreparedStatement stmt = plugin.database.connection.prepareStatement(sql);
                    stmt.setString(1, "true");
                    stmt.setString(2, String.valueOf(p.getName()));
                    stmt.executeUpdate();

                    Bukkit.getServer().getPluginManager().registerEvents(new CancelTakeItemFromInventoryGUI(gui, plugin, typeTABLETtable),  plugin);
                }
            }catch (NullPointerException nu){}
            //NORMAL TABLET

            //OFFICE TABLET
            try{
                if( e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.BLUE + "TABLET - URZAD") &&
                        !e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(null) &&
                        e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null
                )
                {

                    String typeTABLETtable = "openTabletOffice";

                    Player p = e.getPlayer();
                    Inventory gui = Bukkit.createInventory(p, 9, "TABLET - URZAD");
                    ItemStack item1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                    ItemMeta item1_meta = item1.getItemMeta();
                    String nameItemsTablet = ChatColor.translateAlternateColorCodes('&', FilesManager.getMessage.getString("message.infoItemsTablet"));
                    item1_meta.setDisplayName(nameItemsTablet);
                    ArrayList<String> itemLore = new ArrayList<>();
                    itemLore.add(ChatColor.GRAY + "Gdy wejdziesz do panelu, odswierz strone!");
                    itemLore.add(ChatColor.GREEN + "Aby teblet dzialal na stronie,");
                    itemLore.add(ChatColor.GREEN + "musisz go miec otworzonego na serwerze");
                    item1_meta.setLore(itemLore);
                    item1.setItemMeta(item1_meta);

                    gui.setItem(0,item1);
                    gui.setItem(1,item1);
                    gui.setItem(2,item1);
                    gui.setItem(3,item1);
                    gui.setItem(4,item1);
                    gui.setItem(5,item1);
                    gui.setItem(6,item1);
                    gui.setItem(7,item1);
                    gui.setItem(8,item1);
                    p.openInventory(gui);
                    //p.sendMessage(ChatColor.GREEN + "OPEN");

                    String sql = "UPDATE users SET "+ typeTABLETtable + "=? WHERE nick=?;";
                    PreparedStatement stmt = plugin.database.connection.prepareStatement(sql);
                    stmt.setString(1, "true");
                    stmt.setString(2, String.valueOf(p.getName()));
                    stmt.executeUpdate();

                    Bukkit.getServer().getPluginManager().registerEvents(new CancelTakeItemFromInventoryGUI(gui, plugin, typeTABLETtable),  plugin);
                }
            }catch (NullPointerException nu){}
            //OFFICE TABLET




        }
    }
}
