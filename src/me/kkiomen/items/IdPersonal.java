package me.kkiomen.items;

import me.kkiomen.main.FilesManager;
import me.kkiomen.main.Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;

public class IdPersonal implements CommandExecutor {

    private Main plugin;

    public IdPersonal(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {

            if(sender.hasPermission("rcon"))    {

                String nl = "\n";

                String firstname = "";
                String secodname = "";
                Integer age = 0;
                String pesel = "";
                String dateMaked = "";
                String birthday ="";
                String dateFinished = "";
                String uniqueCODE = "";
                Integer id_user = 0;


                if(args.length > 0){
                    firstname = args[0];
                    secodname = args[1];
                    age = Integer.parseInt(args[2]);
                    pesel = args[3];
                    dateMaked = args[4];
                    birthday = args[5];
                    dateFinished = args[6];
                    uniqueCODE = args[7];
                    id_user = Integer.parseInt(args[8]);

                    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);

                    BookMeta bookmeta = (BookMeta) book.getItemMeta();
                    bookmeta.setAuthor("Urząd Miejski");
                    bookmeta.setTitle(ChatColor.DARK_GRAY + "Dowód osobisty");

                    ArrayList<String> pages = new ArrayList<String>();
                    pages.add(0,
                            ChatColor.BLUE + "OFICJALNY DOWOD OSOBISTY" + nl + nl + ChatColor.BLACK +
                                    "ID: " + id_user + nl +
                                    "Imie: " + firstname + nl +
                                    "Nazwisko: " + secodname + nl +
                                    "Wiek: " + age + nl +
                                    "Data urodzenia: " + birthday + nl + nl +
                                    "Pesel: " + pesel + nl +nl +
                                    "Nr Dowodu: " + uniqueCODE

                    );
                    pages.add(1,
                            "Data utworzenia: " + dateMaked + nl +
                                    "Data waznosci: " + dateFinished + nl

                    );
                    bookmeta.setPages(pages);
                    book.setItemMeta(bookmeta);

                    //player.getInventory().addItem(book);

                    World world = Bukkit.getServer().getWorld("world");

                    int x = Integer.parseInt(FilesManager.config.getString("chest.office.x"));
                    int y = Integer.parseInt(FilesManager.config.getString("chest.office.y"));
                    int z = Integer.parseInt(FilesManager.config.getString("chest.office.z"));

                    Location blockLocation = new Location(world, x, y, z);
                    Block block = blockLocation.getBlock();
                    block.setType( Material.CHEST );
                    Chest chest2 = (Chest)blockLocation.getBlock().getState();

                    chest2.getInventory().addItem(book);
                }
            }



        return false;
    }
}
