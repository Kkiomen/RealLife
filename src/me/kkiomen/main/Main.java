package me.kkiomen.main;

import me.kkiomen.economy.Bank;
import me.kkiomen.economy.Money;
import me.kkiomen.items.IdPersonal;
import me.kkiomen.items.Tablet;
import me.kkiomen.main.command.HelpCommand;
import me.kkiomen.main.command.Scream;
import me.kkiomen.main.command.Whisper;
import me.kkiomen.main.command.YourselfAction;
import me.kkiomen.main.events.openTablet;
import me.kkiomen.region.RentRegion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public MYSQL database = new MYSQL();

    @Override
    public void onEnable() {
        super.onEnable();
        FilesManager.base(this);
        database.connect();
        Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this),  this);
        Bukkit.getServer().getPluginManager().registerEvents(new openTablet(this),  this);
        Bukkit.getServer().getPluginManager().registerEvents(new Bank(this),  this);
        Bukkit.getServer().getPluginManager().registerEvents(new RentRegion(this),  this);



        getCommand("pomoc").setExecutor(new HelpCommand());
        getCommand("szept").setExecutor(new Whisper(this));
        getCommand("krzyk").setExecutor(new Scream(this));
        getCommand("me").setExecutor(new YourselfAction(this));

        getCommand("powiadomienie").setExecutor(new HelpCommand());

        getCommand("money").setExecutor(new Money(this));
        getCommand("idpersonal").setExecutor(new IdPersonal(this));
        getCommand("tablet").setExecutor(new Tablet(this));
        getCommand("tableturzad").setExecutor(new Tablet(this));



        getCommand("bank").setExecutor(new Bank(this));

    }

    @Override
    public void onDisable() {
        super.onDisable();
        database.close();
    }
}
