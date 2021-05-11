package me.kkiomen.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import xyz.haoshoku.nick.api.NickAPI;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class OnJoin implements Listener {

    private Main plugin;

    public OnJoin(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException {
        Player player = event.getPlayer();

        String sqlSelect = "SELECT id, firstname, secondname FROM users WHERE uuid=?;";
        PreparedStatement  stmt2 = plugin.database.connection.prepareStatement(sqlSelect);
        stmt2.setString(1, String.valueOf(player.getUniqueId()));
        ResultSet results = stmt2.executeQuery();

        Integer tmpID = 0;
        String tmpFirstname = null;
        String tmpSecondname = null;

        while (results.next()){
            tmpID = results.getInt("id");
            tmpFirstname = results.getString("firstname");
            tmpSecondname = results.getString("secondname");
        }

        //player.sendMessage(ChatColor.translateAlternateColorCodes('&', FilesManager.getMessage.getString("message.infoIfNotLoggedInWebsite")));
        String messageKickNotRegister = ChatColor.translateAlternateColorCodes('&', FilesManager.getMessage.getString("message.infoIfNotLoggedInWebsite"));
        if(tmpFirstname == null){
            player.kickPlayer(messageKickNotRegister);
        }



        String sql = "UPDATE users SET uuid=? WHERE nick=?;";
        PreparedStatement stmt = plugin.database.connection.prepareStatement(sql);
        stmt.setString(1, String.valueOf(player.getUniqueId()));
        stmt.setString(2, String.valueOf(player.getName()));
        stmt.executeUpdate();





        String tmpIDString = String.valueOf(tmpID);
        String names =  ChatColor.GRAY + tmpFirstname + " " ;

        String IDTEAMCHANGE = tmpIDString+ "TEAM";
        Scoreboard scoreboard;
        // Change it, if you are using main scoreboard
        if ( player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard() ) {
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard( scoreboard );
        } else
            scoreboard = player.getScoreboard();


        if ( scoreboard.getTeam( IDTEAMCHANGE) != null ) scoreboard.getTeam( IDTEAMCHANGE ).unregister();

        Team team = scoreboard.registerNewTeam( IDTEAMCHANGE );
        team.setPrefix( "[" + tmpIDString + "] " );
        team.setSuffix( ChatColor.GRAY + tmpSecondname );

        Collection<String> values = NickAPI.getNickedPlayers().values();
        for ( String name : values )
            scoreboard.getTeam( IDTEAMCHANGE ).addEntry( name );


        NickAPI.nick(player, names);
        NickAPI.refreshPlayer(player);



    }
}
