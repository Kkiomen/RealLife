package me.kkiomen.items;

import me.kkiomen.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Tablet implements CommandExecutor {
    private Main plugin;

    public Tablet(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {
        Player player = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("tablet")){
            if(player.hasPermission("table")){
                ItemStack tablet = new ItemStack(Material.BLUE_DYE);
                ItemMeta tabletMeta = tablet.getItemMeta();
                tabletMeta.setDisplayName(ChatColor.BLUE + "TABLET");
                ArrayList<String> tabletLore = new ArrayList<>();
                tabletLore.add(ChatColor.GRAY + "Marka: Szajsung");
                tabletMeta.setLore(tabletLore);
                tablet.setItemMeta(tabletMeta);

                if(args.length > 0 ){
                    if(args[0].equalsIgnoreCase("give")){
                        if(args.length > 1 ) {
                            if (!args[1].equals(null)) {
                                try{
                                    int countsNumer = Integer.parseInt(args[1]);
                                    for (int i = 0; i < countsNumer; i++) {
                                        player.getInventory().addItem(tablet);
                                    }
                                }catch(NumberFormatException ex){
                                    player.sendMessage(ChatColor.RED + "Wprowadz liczbe");
                                }
                            }
                        }else{
                            player.getInventory().addItem(tablet);
                        }

                    }else if(args[0].equalsIgnoreCase("remove")){
                        if(args.length > 1 ) {
                            if (!args[1].equals(null)) {

                                try{
                                    int amount = Integer.parseInt(args[1]);
                                    Inventory inventory = player.getInventory();
                                    if (amount <= 0)
                                        player.sendMessage("Nie podano wartosci");
                                    int size = inventory.getSize();
                                    for (int slot = 0; slot < size; slot++) {
                                        ItemStack is = inventory.getItem(slot);
                                        if (is == null) continue;
                                        if (tablet.getType() == is.getType()) {
                                            int newAmount = is.getAmount() - amount;
                                            if (newAmount > 0) {
                                                is.setAmount(newAmount);
                                                break;
                                            } else {
                                                inventory.clear(slot);
                                                amount = -newAmount;
                                                if (amount == 0) break;
                                            }
                                        }
                                    }
                                }catch(NumberFormatException ex){
                                    player.sendMessage(ChatColor.RED + "Wprowadz liczbe");
                                }






                            }
                        }else{
                            player.sendMessage("Podaj ilosc");
                        }



                    }else{
                        player.sendMessage("FFFF");
                    }
                }




            }

        }


        if(cmd.getName().equalsIgnoreCase("tableturzad")){
            if(player.hasPermission("table")){
                ItemStack tablet = new ItemStack(Material.BLUE_DYE);
                ItemMeta tabletMeta = tablet.getItemMeta();
                tabletMeta.setDisplayName(ChatColor.BLUE + "TABLET - URZAD");
                ArrayList<String> tabletLore = new ArrayList<>();
                tabletLore.add(ChatColor.GRAY + "Marka: Szajsung");
                tabletMeta.setLore(tabletLore);
                tablet.setItemMeta(tabletMeta);

                if(args.length > 0 ){
                    if(args[0].equalsIgnoreCase("give")){
                        if(args.length > 1 ) {
                            if (!args[1].equals(null)) {
                                try{
                                    int countsNumer = Integer.parseInt(args[1]);
                                    for (int i = 0; i < countsNumer; i++) {
                                        player.getInventory().addItem(tablet);
                                    }
                                }catch(NumberFormatException ex){
                                    player.sendMessage(ChatColor.RED + "Wprowadz liczbe");
                                }
                            }
                        }else{
                            player.getInventory().addItem(tablet);
                        }

                    }else if(args[0].equalsIgnoreCase("remove")){
                        if(args.length > 1 ) {

                            if (!args[1].equals(null)) {

                                try{
                                    int amount = Integer.parseInt(args[1]);
                                    Inventory inventory = player.getInventory();
                                    if (amount <= 0)
                                        player.sendMessage("Nie podano wartosci");
                                    int size = inventory.getSize();
                                    for (int slot = 0; slot < size; slot++) {
                                        ItemStack is = inventory.getItem(slot);
                                        if (is == null) continue;
                                        if (tablet.getType() == is.getType()) {
                                            int newAmount = is.getAmount() - amount;
                                            if (newAmount > 0) {
                                                is.setAmount(newAmount);
                                                break;
                                            } else {
                                                inventory.clear(slot);
                                                amount = -newAmount;
                                                if (amount == 0) break;
                                            }
                                        }
                                    }
                                }catch(NumberFormatException ex){
                                    player.sendMessage(ChatColor.RED + "Wprowadz liczbe");
                                }






                            }
                        }else{
                            player.sendMessage("Podaj ilosc");
                        }



                    }else{
                        player.sendMessage("FFFF");
                    }
                }




            }

        }
        return false;
    }
}
