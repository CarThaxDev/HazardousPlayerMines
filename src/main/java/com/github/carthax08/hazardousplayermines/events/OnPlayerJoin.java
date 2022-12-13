package com.github.carthax08.hazardousplayermines.events;

import com.github.carthax08.hazardousplayermines.PluginMain;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OnPlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        try(ResultSet results = PluginMain.get().getDatabaseHandler().query("SELECT * FROM notifications WHERE uuid = '" + player.getUniqueId() + "';")){
            if(results.next()){
                Bukkit.getScheduler().scheduleSyncDelayedTask(PluginMain.get(), () -> {
                    try {
                        player.sendMessage(results.getString("message"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }, (long) (PluginMain.get().getSettings().getNotificationDelay() * 20L));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
