package me.kkiomen.main.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String s, final String[] args) {


        if(command.getName().equalsIgnoreCase("pomoc")){
            Player player = (Player)sender;
            if (args.length == 0) {
                player.sendMessage(ChatColor.GRAY + "/pomoc chat - " + ChatColor.DARK_GRAY + " - Pomoc zwiazana z chatem");
                return false;
            }
            if(args[0].equalsIgnoreCase("chat")){
                player.sendMessage(ChatColor.GRAY + "/szept <wiadomosc> - " + ChatColor.DARK_GRAY + " - Wysyla tekst do osob w obrebie 5 kratek");
                player.sendMessage(ChatColor.GRAY + "/krzyk <wiadomosc> - " + ChatColor.DARK_GRAY + " - Wysyla tekst do osob w obrebie 20 kratek");
                player.sendMessage(ChatColor.GRAY + "/me <wiadomosc> - " + ChatColor.DARK_GRAY + " -  Komenda sluzy do opisywania wlasnych akcji na dystans 15 kratek");
            }else{
                player.sendMessage(ChatColor.RED + "Niestety nie ma takiej komendy. Wpisz /pomoc");
            }

        }else if(command.getName().equalsIgnoreCase("powiadomienie")){
            if(sender.hasPermission("consola")){

                if (args.length >= 1) {
                    String message = "";
                    Player currentPlayer = Bukkit.getPlayer(args[0]);
                    for (int i = 1; i < args.length; i++) {
                        message = message + args[i] + " ";
                    }
                    if (message.length() == 0) {
                        return false;
                    }
                    currentPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&',message));
                }

                }
        }






        return true;

    }


}
