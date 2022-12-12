package com.github.carthax08.hazardousplayermines.objects;

import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

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

}
