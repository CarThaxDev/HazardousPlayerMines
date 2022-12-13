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
    // set upon object creation from db or generated new
    public final UUID mineIdentifier;
    public CuboidRegion region;
    public UUID owner;
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


    public void doUpKeep() {
        if (balance > upkeep) {
            balance -= upkeep;
        } else if(pullFromOwnerBalance){
            Economy economy = PluginMain.get().getEconomy();
            OfflinePlayer player = Bukkit.getOfflinePlayer(owner);
            if(economy.getBalance(player) >= upkeep) {
                economy.withdrawPlayer(player, upkeep);
            }else{
                setInactiveState(true);
                notifyOwnerOfInactiveState();
            }
        } else {
            setInactiveState(true);
            notifyOwnerOfInactiveState();
        }
    }

    private void setInactiveState(boolean newState){
        inactive = newState;
    }

    private void notifyOwnerOfInactiveState() {
        Player player = Bukkit.getPlayer(owner);
        if (player == null) {
            PluginMain.get().getDatabaseHandler().execute("INSERT INTO notifications (player, message) values (" +
                    owner + "," + PluginMain.get().getPrefix() + PluginMain.get().getMessages().getMineInactiveMessage().replace("{LOCATION}", String.format("(%s,%s)", region.getCenter().getX(), region.getCenter().getZ())) + ");");
        } else {
            player.sendMessage(PluginMain.get().getPrefix() + PluginMain.get().getMessages().getMineInactiveMessage().replace("{PLAYER}", player.getName()).replace("{LOCATION}", String.format("(%s,%s)", region.getCenter().getX(), region.getCenter().getZ())));
        }
    }

    public void reset(){
        new BukkitRunnable(){
            @Override
            public void run(){
                RandomPattern pattern = new RandomPattern();
                for(Block block : blocks){
                    BlockState state = BukkitAdapter.adapt((BlockData) Bukkit.getScheduler().callSyncMethod(PluginMain.get(), () -> block.getType().createBlockData()));
                    pattern.add(state, 1.0/blocks.length);
                }
                try (EditSession session = WorldEdit.getInstance().newEditSession(region.getWorld())){
                    session.setBlocks(region, pattern);
                } catch (MaxChangedBlocksException e) {
                    throw new RuntimeException(e);
                }
                Bukkit.getScheduler().runTask(PluginMain.get(), () -> {
                    for (Player player : Bukkit.getOnlinePlayers()){
                        Location location = player.getLocation();
                        if(region.contains(BlockVector3.at(location.getX(), location.getY(), location.getZ()))){
                            player.teleport(safteyTeleportLocation);
                            player.sendMessage(PluginMain.get().getPrefix() + PluginMain.get().getMessages().getMineResetMessage());
                        }
                    }
                });
            }
        }.runTaskAsynchronously(PluginMain.get());
    }
}
