package me.kkiomen.economy;

import me.kkiomen.main.FilesManager;
import me.kkiomen.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Bank implements CommandExecutor, Listener {

    private Main plugin;

    public Bank(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String s, final String[] args) {

        if(cmd.getName().equalsIgnoreCase("bank")){
            if(sender.hasPermission("bank")){


                if(args.length > 0 ){
                    if(args[0].equalsIgnoreCase("give")){
                        if(args.length > 1 ) {
                            if (!args[1].equals(null)) {
                                Player player = Bukkit.getPlayer(args[1]);
                                if(args[2].equalsIgnoreCase("card")){
                                    String numberAccountBank = args[3];
                                    ItemStack item = new ItemStack(Material.NETHERITE_SCRAP);
                                    ItemMeta itemMeta = item.getItemMeta();
                                    itemMeta.setDisplayName(ChatColor.DARK_GRAY + "Karta debetowa");
                                    ArrayList<String> itemLore = new ArrayList<>();
                                    itemLore.add(ChatColor.GRAY + "Indywidualny kod:");
                                    itemLore.add(numberAccountBank);
                                    itemMeta.setLore(itemLore);
                                    item.setItemMeta(itemMeta);
                                    player.sendMessage(ChatColor.GOLD + "[POCZTA] " + ChatColor.GRAY + "Została do Ciebie nadana paczka: karta debetowa z banku");
                                    player.getInventory().addItem(item);
                                }

                            }
                        }else{
                        }

                    }
                }
            }
        }



        return false;
    }


    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase("bankomat"))
            if (p.hasPermission("bankomat.create")) {
                if (e.getLine(1).isEmpty()) {
                    p.sendMessage(ChatColor.RED + "Napisz cos w 2 linii");
                    e.setCancelled(true);
                } else {
                    e.setLine(0, "§1[Bankomat]");
                    e.setLine(1, ChatColor.BLACK +"Kliknij by uzyc");
                    p.sendMessage(ChatColor.GREEN + "Utworzono bankomat");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permission to create a ATM!");
                e.setCancelled(true);
            }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent ev)
    {
        if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block b = ev.getClickedBlock();
            if ((b.getType() == Material.OAK_SIGN) || (b.getType() == Material.OAK_WALL_SIGN)) {
                Sign s = (Sign)b.getState();
                Player p = ev.getPlayer();
                try{
                    if(ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.DARK_GRAY + "Karta debetowa") &&
                            !ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(null) &&
                            ev.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName() != null){
                        if (s.getLine(0).equalsIgnoreCase("§1[Bankomat]")){




                            String typeTABLETtable = "openTablet";

                            Inventory gui = Bukkit.createInventory(p, 27, "BANKOMAT");

                            ItemStack item1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
                            ItemMeta item1_meta = item1.getItemMeta();
                            item1_meta.setDisplayName("");
                            ArrayList<String> itemLore = new ArrayList<>();
                            item1_meta.setLore(itemLore);
                            item1.setItemMeta(item1_meta);



                            ItemStack hand = ev.getPlayer().getInventory().getItemInHand();
                            ItemMeta meta = hand.getItemMeta();
                            List<String> lore = meta.getLore();

                            String currentNumberAccount = lore.get(1);
                            String sqlSelect = "SELECT * FROM bank_accounts WHERE number=?;";
                            PreparedStatement stmt2 = plugin.database.connection.prepareStatement(sqlSelect);
                            stmt2.setString(1, String.valueOf(currentNumberAccount));
                            ResultSet results = stmt2.executeQuery();

                            //TODO: NAPRAWIC ABY POBIERAŁO STAN KONTA
                            Double tmpValue = 0.00;
                            while (results.next()){
                                tmpValue = results.getDouble("value");
                            }
                            String tmpValueString = String.valueOf(tmpValue);


                            for(int i = 0; i<27 ; i++){
                                if(i == 4){
                                    ItemStack item2 = new ItemStack(Material.TARGET, 1);
                                    ItemMeta item2_meta = item2.getItemMeta();
                                    item2_meta.setDisplayName(ChatColor.GREEN + "STAN KONTA");
                                    ArrayList<String> itemLore2 = new ArrayList<>();
                                    itemLore2.add(ChatColor.GRAY + tmpValueString  + " PLN");
                                    item2_meta.setLore(itemLore2);
                                    item2.setItemMeta(item2_meta);
                                    gui.setItem(i,item2);
                                }else if(i == 17){
                                    ItemStack item3 = new ItemStack(Material.LIGHT_BLUE_TERRACOTTA, 1);
                                    ItemMeta item3_meta = item3.getItemMeta();
                                    item3_meta.setDisplayName(ChatColor.GREEN + "Wypłać");
                                    ArrayList<String> itemLore3 = new ArrayList<>();
                                    itemLore3.add(ChatColor.GRAY + "Wypłac potrzebne Ci pieniądze z banku");
                                    item3_meta.setLore(itemLore3);
                                    item3.setItemMeta(item3_meta);
                                    gui.setItem(i,item3);
                                }else if(i == 26){
                                    ItemStack item4 = new ItemStack(Material.BROWN_TERRACOTTA, 1);
                                    ItemMeta item4_meta = item4.getItemMeta();
                                    item4_meta.setDisplayName(ChatColor.GREEN + "Wpłać");
                                    ArrayList<String> itemLore4 = new ArrayList<>();
                                    itemLore4.add(ChatColor.GRAY + "Wpłać pieniądze na konto bankowe");
                                    item4_meta.setLore(itemLore4);
                                    item4.setItemMeta(item4_meta);
                                    gui.setItem(i,item4);
                                }else{
                                    gui.setItem(i,item1);
                                }
                            }
                            p.openInventory(gui);
                            //p.sendMessage(ChatColor.GREEN + "OPEN");


                        }
                    }
                }catch (NullPointerException | SQLException e){}



            }
        }
    }


    public Double getCountValueAccountBankPlayer(Player pl) throws SQLException {

        ItemStack hand = pl.getInventory().getItemInHand();
        ItemMeta meta = hand.getItemMeta();
        List<String> lore = meta.getLore();

        String currentNumberAccount = lore.get(1);
        String sqlSelect = "SELECT * FROM bank_accounts WHERE number=?;";
        PreparedStatement stmt2 = plugin.database.connection.prepareStatement(sqlSelect);
        stmt2.setString(1, String.valueOf(currentNumberAccount));
        ResultSet results = stmt2.executeQuery();

        Double tmpValue = 0.00;
        while (results.next()){
            tmpValue = results.getDouble("value");
        }
        return tmpValue;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void withdrawMoney(Player p, Integer count) throws SQLException {

        ItemStack hand = p.getInventory().getItemInHand();
        ItemMeta meta = hand.getItemMeta();
        List<String> lore = meta.getLore();
        String currentNumberAccount = lore.get(1);

        Double getMoney = getCountValueAccountBankPlayer(p);
        Double currentMoney = getMoney - count;

        String sql = "UPDATE bank_accounts SET value=? WHERE number=?;";
        PreparedStatement stmt = plugin.database.connection.prepareStatement(sql);
        stmt.setString(1, String.valueOf(round(currentMoney,2)));
        stmt.setString(2, String.valueOf(currentNumberAccount));
        stmt.executeUpdate();

        Date nowDate = new Date();
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql2 = "INSERT INTO `bank_transfers`(id, fromBankAccount, toBankAccount, commant, title, type, value, balanceAfterTransfer, created_at) VALUES (NULL,?,1,?,?,?,?,?,?);";
        PreparedStatement stmt2 = plugin.database.connection.prepareStatement(sql2);
        stmt2.setInt(1, Integer.parseInt(currentNumberAccount));
        stmt2.setString(2, "");
        stmt2.setString(3, "Wypłata pieniędzy");
        stmt2.setString(4, "withdraw");
        stmt2.setDouble(5, round(count,2));
        stmt2.setDouble(6, 0.0);
        stmt2.setString(7, sdf2.format(nowDate));
        stmt2.executeUpdate();


        String commant = "money give " + String.valueOf(p.getName()) + " " + String.valueOf(count);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commant);
        p.sendMessage(ChatColor.BLUE + "[BANKOMAT] " + ChatColor.GRAY + "Poprawnie wypłacono: " + ChatColor.GREEN +  String.valueOf(count) + " PLN");
        openInventoryWithdraw((Player) p);
    }

    public void openInventoryWithdraw(Player player) throws SQLException {
        String tmpValueString = String.valueOf(getCountValueAccountBankPlayer((Player) player));

        Inventory gui = Bukkit.createInventory(player, 27, "BANKOMAT - WYPLAC");

        ItemStack item1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta item1_meta = item1.getItemMeta();
        item1_meta.setDisplayName("");
        ArrayList<String> itemLore = new ArrayList<>();
        item1_meta.setLore(itemLore);
        item1.setItemMeta(item1_meta);




        for(int i = 0; i<27 ; i++){
            if(i == 4){
                ItemStack item2 = new ItemStack(Material.TARGET, 1);
                ItemMeta item2_meta = item2.getItemMeta();
                item2_meta.setDisplayName(ChatColor.GREEN + "STAN KONTA");
                ArrayList<String> itemLore2 = new ArrayList<>();
                itemLore2.add(ChatColor.GRAY   + tmpValueString + " PLN");
                item2_meta.setLore(itemLore2);
                item2.setItemMeta(item2_meta);
                gui.setItem(i,item2);
            }else if(i == 9){
                ItemStack item3 = new ItemStack(Material.WHITE_TERRACOTTA, 1);
                ItemMeta item3_meta = item3.getItemMeta();
                item3_meta.setDisplayName(ChatColor.GREEN + "1 PLN");
                ArrayList<String> itemLore3 = new ArrayList<>();
                itemLore3.add(ChatColor.GRAY + "Wypłac z konta 1 PLN");
                item3_meta.setLore(itemLore3);
                item3.setItemMeta(item3_meta);
                gui.setItem(i,item3);
            }else if(i == 18){
                ItemStack item5 = new ItemStack(Material.YELLOW_TERRACOTTA, 1);
                ItemMeta item5_meta = item5.getItemMeta();
                item5_meta.setDisplayName(ChatColor.GREEN + "10 PLN");
                ArrayList<String> itemLore5 = new ArrayList<>();
                itemLore5.add(ChatColor.GRAY + "Wypłac z konta 10 PLN");
                item5_meta.setLore(itemLore5);
                item5.setItemMeta(item5_meta);
                gui.setItem(i,item5);

            }else if(i == 17){
                ItemStack item7 = new ItemStack(Material.ORANGE_TERRACOTTA, 1);
                ItemMeta item7_meta = item7.getItemMeta();
                item7_meta.setDisplayName(ChatColor.GREEN + "100 PLN");
                ArrayList<String> itemLore7 = new ArrayList<>();
                itemLore7.add(ChatColor.GRAY + "Wypłac z konta 100 PLN");
                item7_meta.setLore(itemLore7);
                item7.setItemMeta(item7_meta);
                gui.setItem(i,item7);
            }else if(i == 26){
                ItemStack item4 = new ItemStack(Material.RED_TERRACOTTA, 1);
                ItemMeta item4_meta = item4.getItemMeta();
                item4_meta.setDisplayName(ChatColor.GREEN + "1000 PLN");
                ArrayList<String> itemLore4 = new ArrayList<>();
                itemLore4.add(ChatColor.GRAY + "Wypłac z konta 1000 PLN");
                item4_meta.setLore(itemLore4);
                item4.setItemMeta(item4_meta);
                gui.setItem(i,item4);
            }else{
                gui.setItem(i,item1);
            }
        }
        player.openInventory(gui);
    }

    public void openInventoryDeposit(Player player) throws SQLException {
        String tmpValueString = String.valueOf(getCountValueAccountBankPlayer((Player) player));

        Inventory gui = Bukkit.createInventory(player, 27, "BANKOMAT - WPLAC PIENIADZE");

        ItemStack item1 = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta item1_meta = item1.getItemMeta();
        item1_meta.setDisplayName("");
        ArrayList<String> itemLore = new ArrayList<>();
        item1_meta.setLore(itemLore);
        item1.setItemMeta(item1_meta);




        for(int i = 0; i<27 ; i++){
            if(i == 4){
                ItemStack item2 = new ItemStack(Material.TARGET, 1);
                ItemMeta item2_meta = item2.getItemMeta();
                item2_meta.setDisplayName(ChatColor.GREEN + "STAN KONTA");
                ArrayList<String> itemLore2 = new ArrayList<>();
                itemLore2.add(ChatColor.GRAY   + tmpValueString + " PLN");
                item2_meta.setLore(itemLore2);
                item2.setItemMeta(item2_meta);
                gui.setItem(i,item2);
            }else if(i == 9){
                ItemStack item3 = new ItemStack(Material.WHITE_TERRACOTTA, 1);
                ItemMeta item3_meta = item3.getItemMeta();
                item3_meta.setDisplayName(ChatColor.GREEN + "1 PLN");
                ArrayList<String> itemLore3 = new ArrayList<>();
                itemLore3.add(ChatColor.GRAY + "Wpłać na konto 1 PLN");
                item3_meta.setLore(itemLore3);
                item3.setItemMeta(item3_meta);
                gui.setItem(i,item3);
            }else if(i == 18){
                ItemStack item5 = new ItemStack(Material.YELLOW_TERRACOTTA, 1);
                ItemMeta item5_meta = item5.getItemMeta();
                item5_meta.setDisplayName(ChatColor.GREEN + "10 PLN");
                ArrayList<String> itemLore5 = new ArrayList<>();
                itemLore5.add(ChatColor.GRAY + "Wpłać na konto 10 PLN");
                item5_meta.setLore(itemLore5);
                item5.setItemMeta(item5_meta);
                gui.setItem(i,item5);

            }else if(i == 17){
                ItemStack item7 = new ItemStack(Material.ORANGE_TERRACOTTA, 1);
                ItemMeta item7_meta = item7.getItemMeta();
                item7_meta.setDisplayName(ChatColor.GREEN + "100 PLN");
                ArrayList<String> itemLore7 = new ArrayList<>();
                itemLore7.add(ChatColor.GRAY + "Wpłać na konto 100 PLN");
                item7_meta.setLore(itemLore7);
                item7.setItemMeta(item7_meta);
                gui.setItem(i,item7);
            }else if(i == 26){
                ItemStack item4 = new ItemStack(Material.RED_TERRACOTTA, 1);
                ItemMeta item4_meta = item4.getItemMeta();
                item4_meta.setDisplayName(ChatColor.GREEN + "1000 PLN");
                ArrayList<String> itemLore4 = new ArrayList<>();
                itemLore4.add(ChatColor.GRAY + "Wpłać na konto 1000 PLN");
                item4_meta.setLore(itemLore4);
                item4.setItemMeta(item4_meta);
                gui.setItem(i,item4);
            }else{
                gui.setItem(i,item1);
            }
        }
        player.openInventory(gui);
    }

    public void depositMoney(Player player, Integer value) throws SQLException {
        int countMoneyInInventory = 0;

        PlayerInventory inventoryy = player.getInventory();
        ItemStack[] items = inventoryy.getContents();
        for (ItemStack item : items)
        {
            if (item != null && item.getType().equals(Material.PURPLE_DYE) ) {
                countMoneyInInventory += item.getAmount();
            }
        }

        if(countMoneyInInventory >= value){

            ItemStack hand = player.getInventory().getItemInHand();
            ItemMeta meta = hand.getItemMeta();
            List<String> lore = meta.getLore();
            String currentNumberAccount = lore.get(1);

            Double getMoney = getCountValueAccountBankPlayer(player);
            Double currentMoney = getMoney + value;

            String sql = "UPDATE bank_accounts SET value=? WHERE number=?;";
            PreparedStatement stmt = plugin.database.connection.prepareStatement(sql);
            stmt.setString(1, String.valueOf(round(currentMoney,2)));
            stmt.setString(2, String.valueOf(currentNumberAccount));
            stmt.executeUpdate();

            Date nowDate = new Date();
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sql2 = "INSERT INTO `bank_transfers`(id, fromBankAccount, toBankAccount, commant, title, type, value, balanceAfterTransfer, created_at) VALUES (NULL,?,1,?,?,?,?,?,?);";
            PreparedStatement stmt2 = plugin.database.connection.prepareStatement(sql2);
            stmt2.setInt(1, Integer.parseInt(currentNumberAccount));
            stmt2.setString(2, "");
            stmt2.setString(3, "Wpłata pieniędzy");
            stmt2.setString(4, "deposit");
            stmt2.setDouble(5, round(value,2));
            stmt2.setDouble(6, 0.0);
            stmt2.setString(7, sdf2.format(nowDate));
            stmt2.executeUpdate();

            String commant = "money remove " + String.valueOf(player.getName()) + " " + String.valueOf(value);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commant);

            player.sendMessage(ChatColor.BLUE + "[BANKOMAT] " + ChatColor.GRAY + "Poprawnie wplacono pieniadze na konto: " + ChatColor.GREEN +  String.valueOf(value) + " PLN");
            openInventoryDeposit(player);


        }else{
            player.sendMessage(ChatColor.RED + "Nie posiadasz tyle pieniędzy w ekwipunku");
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent e) throws SQLException {
        HumanEntity player = e.getWhoClicked();
        if (e.getView().getTitle().equals("BANKOMAT")) {

            if (e.getCurrentItem().getType() == Material.LIGHT_BLUE_TERRACOTTA) {



                openInventoryWithdraw((Player) player);



                e.setCancelled(true);
            }else if (e.getCurrentItem().getType() == Material.BROWN_TERRACOTTA) {
                openInventoryDeposit((Player) player);
            }

            e.setCancelled(true);
        }

        if(e.getView().getTitle().equals("BANKOMAT - WYPLAC")){
            e.setCancelled(true);
        }
        if(e.getView().getTitle().equals("BANKOMAT - WPLAC PIENIADZE")){
            e.setCancelled(true);
        }



        if (e.getView().getTitle().equals("BANKOMAT - WYPLAC")) {
            try{
                if (e.getCurrentItem().getType() == Material.WHITE_TERRACOTTA) {
                    if(getCountValueAccountBankPlayer((Player) player) >= 1){
                        withdrawMoney((Player) player, 1);
                    }else{
                        player.sendMessage(ChatColor.RED + "Nie posiadasz tyle pieniedzy w banku!");
                    }
                }
                if (e.getCurrentItem().getType() == Material.YELLOW_TERRACOTTA) {
                    if(getCountValueAccountBankPlayer((Player) player) >= 10){
                        withdrawMoney((Player) player, 10);
                    }else{
                        player.sendMessage(ChatColor.RED + "Nie posiadasz tyle pieniedzy w banku!");
                    }
                }
                if (e.getCurrentItem().getType() == Material.ORANGE_TERRACOTTA) {
                    if(getCountValueAccountBankPlayer((Player) player) >= 100){
                        withdrawMoney((Player) player, 100);
                    }else{
                        player.sendMessage(ChatColor.RED + "Nie posiadasz tyle pieniedzy w banku!");
                    }
                }
                if (e.getCurrentItem().getType() == Material.RED_TERRACOTTA) {
                    if(getCountValueAccountBankPlayer((Player) player) >= 1000){
                        withdrawMoney((Player) player, 1000);
                    }else{
                        player.sendMessage(ChatColor.RED + "Nie posiadasz tyle pieniedzy w banku!");
                    }
                }
            }catch (NullPointerException eb){}
        }

        if (e.getView().getTitle().equals("BANKOMAT - WPLAC PIENIADZE")) {
            try{
                if (e.getCurrentItem().getType() == Material.WHITE_TERRACOTTA) {
                    depositMoney((Player) player, 1);
                }
                if (e.getCurrentItem().getType() == Material.YELLOW_TERRACOTTA) {
                    depositMoney((Player) player, 10);
                }
                if (e.getCurrentItem().getType() == Material.ORANGE_TERRACOTTA) {
                    depositMoney((Player) player, 100);
                }
                if (e.getCurrentItem().getType() == Material.RED_TERRACOTTA) {
                    depositMoney((Player) player, 1000);
                }
            }catch (NullPointerException eb){}
        }



    }



}
