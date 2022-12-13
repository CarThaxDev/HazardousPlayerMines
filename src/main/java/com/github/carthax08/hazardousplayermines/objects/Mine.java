package com.github.carthax08.hazardousplayermines.objects;

import com.github.carthax08.hazardousplayermines.PluginMain;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockState;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Mine {
    public UUID mineIdentifier;
    public CuboidRegion region;
    public Player owner;
    public Block[] blocks;
    public String type;
    public int upkeep;
    public long balance;
    public String reset;
    public UUID[] allowedPlayers;
    public boolean pullFromOwnerBalance;
    public boolean inactive;
    public Location safteyTeleportLocation;

    public Mine(UUID mineIdentifier) {
        this.mineIdentifier = mineIdentifier;
    }

    public Mine(UUID mineIdentifier, UUID owner, Block[] blocks, CuboidRegion region,
                String type, UUID[] allowedPlayers, int upKeep, long balance, String reset,
                boolean pullFromOwnerBalance, boolean inactive, Location safteyTeleportLocation) {
        this.mineIdentifier = mineIdentifier;
        this.owner = owner;
        this.blocks = blocks;
        this.region = region;
        this.type = type;
        this.allowedPlayers = allowedPlayers;
        this.upkeep = upKeep;
        this.balance = balance;
        this.reset = reset;
        this.pullFromOwnerBalance = pullFromOwnerBalance;
        this.inactive = inactive;
        this.safteyTeleportLocation = safteyTeleportLocation;
    }


}
