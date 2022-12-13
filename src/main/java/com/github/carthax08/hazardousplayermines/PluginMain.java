package com.github.carthax08.hazardousplayermines;

import co.aikar.commands.PaperCommandManager;
import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.ConfigurationSection;
import com.clubobsidian.wrappy.transformer.NodeTransformer;
import com.github.carthax08.hazardousplayermines.commands.GUICommand;
import com.github.carthax08.hazardousplayermines.config.Database;
import com.github.carthax08.hazardousplayermines.config.Messages;
import com.github.carthax08.hazardousplayermines.config.Settings;
import com.github.carthax08.hazardousplayermines.database.DatabaseHandler;
import com.github.carthax08.hazardousplayermines.transformers.ChatColorTransformer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
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
    private Messages messages;
    private DatabaseHandler databaseHandler;
    private Economy economy;

    private static PluginMain instance;

    @Override
    public void onEnable() {
        // Config Init Stuff
        saveResource("config.conf", false);
        File configFile = new File(this.getDataFolder(), "config.conf");

        config = Configuration.load(configFile);

        ConfigurationSection settingsSection = config.getConfigurationSection("settings");
        ConfigurationSection databaseSection = config.getConfigurationSection("database");
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");

        settings = new Settings();
        database = new Database();
        messages = new Messages();

        Collection<NodeTransformer> transformers = new ArrayList<>();
        transformers.add(new ChatColorTransformer());

        settingsSection.inject(settings, transformers);
        databaseSection.inject(database);
        messagesSection.inject(messages, transformers);

        prefix = settings.getPrefix();

        logger = this.getLogger();

        log("Config saved and loaded...");

        // Other init stuff
        initCommands();
        log("Commands registered...");
        initEvents();
        log("Events registered...");
        initHandlers();
        if(getServer().getPluginManager().getPlugin("Vault") == null){
            logger.severe(prefix + "Vault is missing or broken, Get it Fixed you idiot!");
            getServer().getPluginManager().disablePlugin(this);
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null){
            logger.severe(prefix + "You must have an economy plugin with vault you idiot");
            getServer().getPluginManager().disablePlugin(this);
        }
        economy = rsp.getProvider();
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
                "(uuid uuid NOT NULL, " + //UUID for the mine, generated at creation
                "owner uuid NOT NULL, " + //uuid of player
                "blocks varchar(255) NOT NULL, " + //list of blocks
                "location varchar(255) NOT NULL, " + //X1:Y1:Z1:X2:Y2:Z2
                "type varchar(16) NOT NULL, " + //PRIVATE, PUBLIC, INVITE - See allowed players
                "allowed-players varchar(255), " + // null if not invite, otherwise list of players uuids
                "upkeep int unsigned NOT NULL," + //Upkeep cost of the mine
                "reset varchar(255) NOT NULL," + //PERCENT:99, TIME:1d
                "balance bigint unsigned NOT NULL DEFAULT 0," + //The mine's current balance, used first for upkeep
                "pullFromOwnerBalance int(1) NOT NULL DEFAULT 0," +
                "mineActiveState int(1) NOT NULL," +
                "PRIMARY KEY (uuid))")) {

            getLogger().severe(prefix + "Failed to create mines table in database");
            getServer().getPluginManager().disablePlugin(this);
        }
        if(!databaseHandler.execute("CREATE TABLE IF NOT EXISTS notifications " +
                "(id NOT NULL AUTO_INCREMENT," +
                "player uuid NOT NULL," +
                "message varchar(255) NOT NULL," +
                "PRIMARY KEY (id))")) {

            getLogger().severe(prefix + "Failed to create notifications table in database");
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

    public Messages getMessages(){
        return messages;
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

    public Economy getEconomy(){
        return economy;
    }
}