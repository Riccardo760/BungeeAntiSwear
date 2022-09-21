package it.riccardo760.bungeeantiswear;

import it.riccardo760.bungeeantiswear.Commands.ReloadCommand;
import it.riccardo760.bungeeantiswear.Listeners.ChatListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public final class Main extends Plugin {

    public static Configuration configuration;
    public static List<String> bannedWords = new ArrayList<>();
    public static String cmd, prefix="&8[&aMUTE&8]";

    @Override
    public void onEnable() {
        // Plugin startup logic

        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        File file = new File(this.getDataFolder(), "config.yml");
        try {
            if (!file.exists())
                Files.copy(this.getResourceAsStream("config.yml"), file.toPath());
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

            //loading from config
            bannedWords = configuration.getStringList("bannedWords");
            cmd = configuration.getString("bannedWordsCmd");
        } catch (IOException e) {
            getLogger().info("Error while reading config.yml");
        }

        getLogger().info("BungeeAntiSwear by Riccardo760 v1.0 enabled successfully.");

        ProxyServer.getInstance().getPluginManager().registerListener(this, new ChatListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand("bas", this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("BungeeAntiSwear by Riccardo760 v1.0 disabled successfully.");
    }


}
