package me.kkiomen.main;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import me.kkiomen.economy.Bank;
import me.kkiomen.economy.Money;
import me.kkiomen.items.IdPersonal;
import me.kkiomen.items.Tablet;
import me.kkiomen.items.WeaponsPermit;
import me.kkiomen.main.command.HelpCommand;
import me.kkiomen.main.command.Scream;
import me.kkiomen.main.command.Whisper;
import me.kkiomen.main.command.YourselfAction;
import me.kkiomen.main.events.openTablet;
import me.kkiomen.region.RentRegion;
import me.kkiomen.user.Disease;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends JavaPlugin {

    public MYSQL database = new MYSQL();

    public WorldGuardPlugin worldGuardPlugin;

    @Override
    public void onEnable() {
        super.onEnable();
        worldGuardPlugin = getWorldGuard();
        FilesManager.base(this);
        database.connect();

        Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this),  this);
        Bukkit.getServer().getPluginManager().registerEvents(new openTablet(this),  this);
        Bukkit.getServer().getPluginManager().registerEvents(new Bank(this),  this);
        Bukkit.getServer().getPluginManager().registerEvents(new RentRegion(this, worldGuardPlugin),  this);
        Bukkit.getServer().getPluginManager().registerEvents(new Disease(this),  this);



        getCommand("pomoc").setExecutor(new HelpCommand());
        getCommand("szept").setExecutor(new Whisper(this));
        getCommand("krzyk").setExecutor(new Scream(this));
        getCommand("me").setExecutor(new YourselfAction(this));

        getCommand("powiadomienie").setExecutor(new HelpCommand());

        getCommand("money").setExecutor(new Money(this));
        getCommand("idpersonal").setExecutor(new IdPersonal(this));
        getCommand("idpermit").setExecutor(new WeaponsPermit(this));
        getCommand("tablet").setExecutor(new Tablet(this));
        getCommand("tableturzad").setExecutor(new Tablet(this));
        getCommand("lek").setExecutor(new Disease(this));



        getCommand("bank").setExecutor(new Bank(this));




        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){

            @Override
            public void run() {

                try{
                    String sqlSelect = "SELECT * FROM regions_lists WHERE rentDayFinish=?;";
                    PreparedStatement stmt2 = null;
                    stmt2 = database.connection.prepareStatement(sqlSelect);
                    Date nowDate = new Date();
                    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    stmt2.setString(1, sdf2.format(nowDate));
                    ResultSet results = stmt2.executeQuery();
                    String regionName = "";
                    while (results.next()){
                        regionName = results.getString("region_name");
                        String sql3 = "UPDATE `regions_lists` SET user=?,rentBool=?,rentDay=?,rentDayFinish=? WHERE region_name = ?;";
                        PreparedStatement stmt3 = database.connection.prepareStatement(sql3);
                        stmt3.setString(1, "");
                        stmt3.setInt(2, 0);
                        stmt3.setString(3, "");
                        stmt3.setString(4, "");
                        stmt3.setString(5, regionName);
                        stmt3.executeUpdate();
                    }




                }catch (SQLException throwables) {
                    throwables.printStackTrace();
                }



            }
        }, 1, 20 * 60 * 1);
    }




    @Override
    public void onDisable() {
        super.onDisable();
        database.close();
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = this.getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

}
