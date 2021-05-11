package me.kkiomen.economy;

import me.kkiomen.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Money implements CommandExecutor {
    private Main plugin;

    public Money(Main plugin){
        this.plugin = plugin;
    }


    public static int getAmount(Player player, ItemStack itemCurrent)
    {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] items = inventory.getContents();
        int has = 0;
        for (ItemStack item : items)
        {
            if ((item != null) && (item.getType().equals(itemCurrent)) && (item.getAmount() > 0))
            {
                has += item.getAmount();
            }
        }
        return has;
    }


    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {

        if(cmd.getName().equalsIgnoreCase("money")){
            if(sender.hasPermission("money")){
                ItemStack money = new ItemStack(Material.PURPLE_DYE);
                ItemMeta moneyMeta = money.getItemMeta();
                moneyMeta.setDisplayName(ChatColor.GREEN + "Bankot 1$");
                ArrayList<String> moneyLore = new ArrayList<>();
                moneyLore.add(ChatColor.GRAY + "Oficjalna waluta serwera");
                moneyLore.add(ChatColor.WHITE + "Na banknocie widnieje NOTCH");
                moneyMeta.setLore(moneyLore);
                moneyMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,
                        ItemFlag.HIDE_DESTROYS,
                        ItemFlag.HIDE_ENCHANTS,
                        ItemFlag.HIDE_PLACED_ON,
                        ItemFlag.HIDE_POTION_EFFECTS,
                        ItemFlag.HIDE_UNBREAKABLE);
                money.setItemMeta(moneyMeta);

                if(args.length > 0 ){
                    if(args[0].equalsIgnoreCase("give")){
                        if(args.length > 1 ) {

                            Player currentPlayer = Bukkit.getPlayer(args[1]);
                            if (!args[2].equals(null)) {
                                try{
                                    int countsNumer = Integer.parseInt(args[2]);
                                    for (int i = 0; i < countsNumer; i++) {
                                        currentPlayer.getInventory().addItem(money);
                                    }
                                }catch(NumberFormatException ex){
                                    sender.sendMessage(ChatColor.RED + "Wprowadz liczbe");
                                }
                            }
                        }else{
                        }

                    }else if(args[0].equalsIgnoreCase("remove")){
                        if(args.length > 1 ) {

                            if (!args[1].equals(null)) {

                                Player currentPlayer = Bukkit.getPlayer(args[1]);

                                try{
                                    int amount = Integer.parseInt(args[2]);
                                    Inventory inventory = currentPlayer.getInventory();
                                    if (amount <= 0)
                                        sender.sendMessage("Nie podano wartosci");
                                    int size = inventory.getSize();

                                    int countMoneyInInventory = 0;

                                    PlayerInventory inventoryy = currentPlayer.getInventory();
                                    ItemStack[] items = inventoryy.getContents();
                                    for (ItemStack item : items)
                                    {
                                        if (item != null && item.getType().equals(Material.PURPLE_DYE) ) {
                                            countMoneyInInventory += item.getAmount();
                                        }
                                    }
                                    if(countMoneyInInventory >= amount){
                                        for (int slot = 0; slot < size; slot++) {
                                            ItemStack is = inventory.getItem(slot);
                                            if (is == null) continue;
                                            if (money.getType() == is.getType()) {
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
                                    }else{
                                        currentPlayer.sendMessage(ChatColor.RED + "Nie posiadasz tyle pieniędzy w ekwipunku");
                                    }

                                }catch(NumberFormatException ex){
                                    sender.sendMessage(ChatColor.RED + "Wprowadz liczbe");
                                }






                            }
                        }else{
                            sender.sendMessage("Podaj ilosc");
                        }



                    }else{
                        sender.sendMessage("FFFF");
                    }
                }




            }

        }


        return false;
    }
}
