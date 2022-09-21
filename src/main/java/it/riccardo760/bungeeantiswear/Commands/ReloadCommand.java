package it.riccardo760.bungeeantiswear.Commands;

import it.riccardo760.bungeeantiswear.Main;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ReloadCommand extends Command {

    public Main plugin;

    public ReloadCommand(String name, Main plugin) {
        super(name);
        this.plugin=plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("bungeeantiswear.reload")){
            if (args.length==1){
                if (args[0].equalsIgnoreCase("reload")){
                    if (!plugin.getDataFolder().exists()) {
                        plugin.getDataFolder().mkdir();
                    }
                    File file = new File(plugin.getDataFolder(), "config.yml");
                    try {
                        if (!file.exists())
                            Files.copy(plugin.getResourceAsStream("config.yml"), file.toPath());
                        Main.configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);

                        //loading from config
                        Main.bannedWords = Main.configuration.getStringList("bannedWords");
                        Main.cmd = Main.configuration.getString("bannedWordsCmd");
                        sender.sendMessage(new ComponentBuilder("Reload completed successfully").create());
                    } catch (IOException e) {
                        sender.sendMessage(new ComponentBuilder("Error while reloading config.yml").create());
                    }
                }
            }
        } else {
            sender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
                    " &8[&a!&8] &cNon hai il permesso di eseguire questo comando!")).create());
        }
    }
}
