package com.github.carthax08.hazardousplayermines;

import com.clubobsidian.wrappy.Configuration;
import com.clubobsidian.wrappy.ConfigurationSection;
import com.clubobsidian.wrappy.transformer.NodeTransformer;
import com.github.carthax08.hazardousplayermines.config.Database;
import com.github.carthax08.hazardousplayermines.config.Settings;
import com.github.carthax08.hazardousplayermines.database.DatabaseHandler;
import com.github.carthax08.hazardousplayermines.transformers.ChatColorTransformer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

@SuppressWarnings("rawtypes")
public final class PluginMain extends JavaPlugin {

    private static String prefix;
    private Configuration config;
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

        Logger logger = getLogger();

        logger.info(prefix + "Config saved and loaded...");

        // Other init stuff
        initCommands();
        logger.info(prefix + "Commands registered...");
        initEvents();
        logger.info(prefix + "Events registered...");
        initHandlers();
        logger.info(prefix + "Handlers registered...");
        logger.info(prefix + "Plugin initialization successful");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initCommands() {
        // Init commands
    }

    private void initEvents() {
        // Init events
    }

    private void initHandlers() {
        // Init handlers
        // Database Handler
        databaseHandler = new DatabaseHandler(database);
        databaseHandler.execute("CREATE TABLE IF NOT EXISTS playermines " +
                "(owner varchar(36) NOT NULL, " +
                "blocks varchar(255), " +
                "location varchar(255), " +
                "PRIMARY KEY (owner))");
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
}
