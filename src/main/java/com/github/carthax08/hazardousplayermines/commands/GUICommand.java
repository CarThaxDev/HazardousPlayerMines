package com.github.carthax08.hazardousplayermines.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;

@CommandAlias("mine|%custom")
public class GUICommand extends BaseCommand {

    @Default
    @Description("Opens the mine config gui")
    public static void gui(Player player, String[] args) {
        player.sendMessage("This command is not yet implemented");
    }

}
