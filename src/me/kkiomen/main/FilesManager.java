package me.kkiomen.main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FilesManager {
    public static File configFile;
    public static FileConfiguration config;

    public static File messageFile;
    public static FileConfiguration getMessage;

    public static void base(Main main){
        if(!main.getDataFolder().exists()){
            main.getDataFolder().mkdirs();
        }

        configFile = new File(main.getDataFolder(), "config.yml");
        if(!configFile.exists()){
            main.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        messageFile = new File(main.getDataFolder(), "message.yml");
        if(!messageFile.exists()){
            main.saveResource("message.yml", false);
        }
        getMessage = YamlConfiguration.loadConfiguration(messageFile);

    }
}

