package com.github.carthax08.hazardousplayermines;

import co.aikar.commands.PaperCommandManager;
import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.ConfigurationSection;
import com.clubobsidian.wrappy.transformer.NodeTransformer;
import com.github.carthax08.hazardousplayermines.commands.GUICommand;
import com.github.carthax08.hazardousplayermines.config.Database;
import com.github.carthax08.hazardousplayermines.config.Settings;
import com.github.carthax08.hazardousplayermines.database.DatabaseHandler;
import com.github.carthax08.hazardousplayermines.transformers.ChatColorTransformer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@SuppressWarnings("rawtypes, unused")
public final class PluginMain extends JavaPlugin {

    private String prefix;
    private Configuration config;
    private Logger logger;
    private Settings settings;
    private Database database;
    private DatabaseHandler databaseHandler;

    private static PluginMain instance;

    @Override
    public void onEnable() {
        // Config Init Stuff
        saveResource("config.conf", false);
        File configFile = new File(this.getDataFolder(), "config.conf");

        config = Configuration.load(configFile);

        ConfigurationSection settingsSection = config.getConfigurationSection("settings");
        ConfigurationSection databaseSection = config.getConfigurationSection("database");

        settings = new Settings();
        database = new Database();

        Collection<NodeTransformer> transformers = new ArrayList<>();
        transformers.add(new ChatColorTransformer());

        settingsSection.inject(settings, transformers);
        databaseSection.inject(database);

        prefix = settings.getPrefix();

        logger = this.getLogger();

        log("Config saved and loaded...");

        // Other init stuff
        initCommands();
        logger.info(prefix + "Commands registered...");
        initEvents();
        logger.info(prefix + "Events registered...");
        initHandlers();
        log("Handlers registered...");
        log("Plugin initialization successful");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initCommands() {

        // Init commands
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        commandManager.getCommandReplacements().addReplacement("custom", String.join("|", settings.getCustomAliases()));

        commandManager.registerCommand(new GUICommand());
    }

    private void initEvents() {
        // Init events
    }

    private void initHandlers() {
        // Init handlers

        // Database Handler
        databaseHandler = new DatabaseHandler(database);
        if (!databaseHandler.execute("CREATE TABLE IF NOT EXISTS playermines " +
                "(uuid varchar(36) NOT NULL, " + //UUID for the mine, generated at creation
                "owner varchar(36) NOT NULL, " + //uuid of player
                "blocks varchar(255) NOT NULL, " + //list of blocks
                "location varchar(255) NOT NULL, " + //X1:Y1:Z1:X2:Y2:Z2
                "type varchar(16) NOT NULL, " + //PRIVATE, PUBLIC, INVITE - See allowed players
                "allowed-players varchar(255), " + // null if not invite, otherwise list of players uuids
                "upkeep int unsigned NOT NULL," + //Upkeep cost of the mine
                "reset varchar(255) NOT NULL," + //PERCENT:99, TIME:1d
                "balance bigint unsigned NOT NULL DEFAULT 0," + //The mine's current balance, used first for upkeep
                "PRIMARY KEY (uuid))")) {

            getLogger().severe(prefix + "Failed to create table in database");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public static PluginMain get() {
        return instance;
    }

    public Database getDatabase() {
        return database;
    }

    public Settings getSettings() {
        return settings;
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public String getPrefix() {
        return prefix;
    }

    public void log(String message){
        logger.info(prefix + message);
    }
}