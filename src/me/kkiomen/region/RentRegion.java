package me.kkiomen.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.kkiomen.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class RentRegion implements Listener {

    private WorldGuardPlugin worldGuardPlugin;
    private Main plugin;

    public RentRegion(Main plugin, WorldGuardPlugin worldGuardPlugin){
        this.plugin = plugin;
        this.worldGuardPlugin = worldGuardPlugin;
    }


    @EventHandler
    public void onSignChange(SignChangeEvent e) throws SQLException  {
        Player p = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase("[wynajem]"))
            if (p.hasPermission("region.rent")) {
                if (e.getLine(1).isEmpty()) {
                    p.sendMessage(ChatColor.RED + "Napisz nazwe regionu");
                    e.setCancelled(true);
                } else {
                    String nameRegion = e.getLine(1);

                    Integer cost = Integer.parseInt(e.getLine(2));
                    Integer day = Integer.parseInt(e.getLine(3));

                    String sql2 = "INSERT INTO `regions_lists`(id, region_name, cost, day) VALUES (NULL,?,?,?);";
                    PreparedStatement stmt2 = plugin.database.connection.prepareStatement(sql2);
                    stmt2.setString(1, nameRegion);
                    stmt2.setInt(2, cost);
                    stmt2.setInt(3, day);
                    stmt2.executeUpdate();



                    e.setLine(0, "§1[Wynajem]");
                    e.setLine(1, ChatColor.BLACK + "Kliknij i zobacz");
                    e.setLine(2, ChatColor.BLACK + "szczegóły");
                    e.setLine(3, nameRegion);
                    p.sendMessage(ChatColor.GREEN + "Poprawnie dodano tabliczke");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permission to create!");
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev) throws SQLException {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = ev.getClickedBlock();
            Player p = ev.getPlayer();
            if ((b.getType() == Material.OAK_SIGN) || (b.getType() == Material.OAK_WALL_SIGN)) {
                Sign s = (Sign) b.getState();
                String nl = "\n";
                String nameRegion = s.getLine(3);
                Integer cost = 0;
                Integer day = 0;
                String nick = "";
                String finishDate = "";

                String sqlSelect = "SELECT * FROM regions_lists WHERE region_name=?;";
                PreparedStatement stmt2 = plugin.database.connection.prepareStatement(sqlSelect);
                stmt2.setString(1, nameRegion);
                ResultSet results = stmt2.executeQuery();

                Double tmpValue = 0.00;
                while (results.next()){
                    cost = results.getInt("cost");
                    day = results.getInt("day");
                    nick = results.getString("user");
                    finishDate = results.getString("rentDayFinish");

                }


                try{
                    if(s.getLine(0).equalsIgnoreCase("§1[Wynajem]") && ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Bankot 1$") &&
                            ev.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.PURPLE_DYE)&&
                            !ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(null) &&
                            ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null){


                        int countMoneyInInventory = 0;

                        PlayerInventory inventoryy = p.getInventory();
                        ItemStack[] items = inventoryy.getContents();
                        for (ItemStack item : items)
                        {
                            if (item != null && item.getType().equals(Material.PURPLE_DYE) ) {
                                countMoneyInInventory += item.getAmount();
                            }
                        }
                        if(countMoneyInInventory >= cost){

                            if(!nick.equalsIgnoreCase("") && !finishDate.equalsIgnoreCase("")){
                                String commant = "money remove " + String.valueOf(p.getName()) + " " + String.valueOf(cost);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commant);
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                Date nowDate = sdf2.parse(finishDate);
                                Calendar c = Calendar.getInstance();
                                c.setTime(nowDate);
                                c.add(Calendar.DATE, day);
                                Date currentDatePlusOne = c.getTime();


                                String sql3 = "UPDATE `regions_lists` SET rentDayFinish=? WHERE region_name = ?;";
                                PreparedStatement stmt3 = plugin.database.connection.prepareStatement(sql3);
                                stmt3.setString(1, sdf2.format(currentDatePlusOne));
                                stmt3.setString(2, nameRegion);
                                stmt3.executeUpdate();

                                p.sendMessage(ChatColor.GREEN + "Pomyślnie przedłużono wynajem regionu");

                            }else{
                                String commant = "money remove " + String.valueOf(p.getName()) + " " + String.valueOf(cost);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commant);
                                World world = p.getWorld();
                                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                                RegionManager regions = container.get(new BukkitWorld(world));
                                ProtectedRegion region = regions.getRegion(nameRegion);
                                DefaultDomain dd = region.getMembers();
                                dd.addPlayer(p.getName());
                                region.setMembers(dd);


                                Date nowDate = new Date();
                                Calendar c = Calendar.getInstance();
                                c.setTime(nowDate);
                                c.add(Calendar.DATE, day);
                                Date currentDatePlusOne = c.getTime();
                                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                                String sql3 = "UPDATE `regions_lists` SET user=?,rentBool=?,rentDay=?,rentDayFinish=? WHERE region_name = ?;";
                                PreparedStatement stmt3 = plugin.database.connection.prepareStatement(sql3);
                                stmt3.setString(1, p.getName());
                                stmt3.setInt(2, 1);
                                stmt3.setString(3, sdf2.format(nowDate));
                                stmt3.setString(4, sdf2.format(currentDatePlusOne));
                                stmt3.setString(5, nameRegion);
                                stmt3.executeUpdate();

                                p.sendMessage(ChatColor.GREEN + "Pomyślnie wynajeto region");
                                p.sendMessage(ChatColor.GRAY + "Aby przedłużyć okres wynajmu, kliknij tabliczke jeszcze raz pieniędzmi");
                            }



                        }else{
                            p.sendMessage(ChatColor.RED + "Nie posiadasz tyle pieniedzy");
                        }




                    }else{

                    }
                }catch (NullPointerException | ParseException nu){



                    try {
                        if (s.getLine(0).equalsIgnoreCase("§1[Wynajem]") && s.getLine(1).equalsIgnoreCase(ChatColor.BLACK + "Kliknij i zobacz") ) {

                            if(!nick.equalsIgnoreCase("") && !finishDate.equalsIgnoreCase("")){
                                p.sendMessage(
                                        ChatColor.BLUE + "[WYNAJEM]" + nl +
                                                ChatColor.DARK_GRAY + "Obiekt: " + ChatColor.RED + "ZAJETY"  + nl +
                                                ChatColor.DARK_GRAY + "Wynajmujacy: " + ChatColor.WHITE + nick + nl +
                                                ChatColor.DARK_GRAY + "Nazwa regionu: " + ChatColor.WHITE + nameRegion + nl +
                                                ChatColor.DARK_GRAY + "Obiekt wynajęty do: " + ChatColor.WHITE +  String.valueOf(finishDate)
                                );
                            }else{
                                p.sendMessage(
                                        ChatColor.BLUE + "[WYNAJEM]" + nl +
                                                ChatColor.DARK_GRAY + "Aby wynajac ten obiekt nacisnij tabliczke banknotami" + nl +
                                                ChatColor.DARK_GRAY + "Nazwa regionu: " + ChatColor.WHITE + nameRegion + nl +
                                                ChatColor.DARK_GRAY + "Koszt: " + ChatColor.WHITE +  cost + " PLN" +  nl +
                                                ChatColor.DARK_GRAY + "Okres wynajmu: " + ChatColor.WHITE +  day + " dni"
                                );
                            }




                        }
                    } catch (NullPointerException e) {
                    }
                }

            }
        }
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Block b = event.getBlock();
        if ((b.getType() == Material.OAK_SIGN) || (b.getType() == Material.OAK_WALL_SIGN)) {
            Sign s = (Sign) b.getState();
            Player p = event.getPlayer();
            if (!p.hasPermission("region.rent")) {
                if (s.getLine(0).equalsIgnoreCase("§1[Wynajem]")) {
                    event.setCancelled(true);
                    p.sendMessage(ChatColor.RED + "Nie możesz tego zrobić");
                }
            }
        }
    }



}
