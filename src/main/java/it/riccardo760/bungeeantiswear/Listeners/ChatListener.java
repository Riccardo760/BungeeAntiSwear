package it.riccardo760.bungeeantiswear.Listeners;

import it.riccardo760.bungeeantiswear.Main;
import it.riccardo760.bungeeantiswear.Utils.Manager;
import litebans.api.Database;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChatEvent(ChatEvent e){

        boolean foundSwear=false;

        if (e.getSender() instanceof ProxiedPlayer){
            ProxiedPlayer targetChatSender = (ProxiedPlayer) e.getSender();

            //Anti SPAM
            if (Manager.chatTimer.containsKey(targetChatSender.getUniqueId().toString())){
                //Check if time is passed
                if (!targetChatSender.hasPermission("bungeeantiswear.bypass.chatcooldown")){
                    if (Manager.chatTimer.get(targetChatSender.getUniqueId().toString())>System.currentTimeMillis()){
                        //Still to wait
                        e.setCancelled(true);

                        float timeLeft = (System.currentTimeMillis()-Manager.chatTimer.get(targetChatSender.getUniqueId().toString()))/-1000f;
                        String seconds = String.format("%.1f", timeLeft);
                        targetChatSender.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
                                " &8[&a&l!&8] &aAttendi ancora &c"+seconds+" &asecondi prima di inviare un altro messaggio.")).create());
                    } else {
                        Manager.chatTimer.remove(targetChatSender.getUniqueId().toString());
                    }
                }
            } else {
                Manager.chatTimer.put(targetChatSender.getUniqueId().toString(), System.currentTimeMillis()+3000);
            }


            //NO SWEAR
            if (!targetChatSender.hasPermission("bungeeantiswear.notify")){
                if (!Database.get().isPlayerMuted(targetChatSender.getUniqueId(),null)){
                    if (!e.getMessage().startsWith("/")){
                        StringBuilder newMsg = new StringBuilder();

                        for (String bannedWord : Main.bannedWords){
                            if (e.getMessage().toLowerCase().contains(bannedWord.toLowerCase())){
                                StringBuilder stars= new StringBuilder();
                                for (int i=0; i<bannedWord.length(); i++){
                                    stars.append("*");
                                }

                                newMsg = new StringBuilder(e.getMessage().replaceAll(bannedWord, stars.toString()));
                                foundSwear=true;
                            }
                        }



                        if (foundSwear){
                            for (ProxiedPlayer pp : ProxyServer.getInstance().getPlayers()){
                                if (pp.hasPermission("bungeeantiswear.notify")){
                                    if (e.getSender() instanceof ProxiedPlayer) {
                                        ProxiedPlayer whoTold = (ProxiedPlayer) e.getSender();

                                        //Staff notify
                                        pp.sendMessage(new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',
                                                "\n "+Main.prefix+" &8(&c"+whoTold.getServer().getInfo().getName()+"&8) &7" + whoTold.getName() + " &8» &f" + e.getMessage())+"\n").create());

                                        /*
                                        if (!Database.get().isPlayerMuted(targetChatSender.getUniqueId(),null)){
                                            TextComponent message = new TextComponent("&8[&a&nClicca Qui&8] &rper mutare &7"+targetChatSender.getName()+" &rper &a1h &rcon motivo \"&7Insulti/Bestemmie\"\n");
                                            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "tempmute "+targetChatSender.getName()+" 3h Insulti/Bestemmie -s"));
                                        }*/



                                    }

                                }
                            }
                            e.setMessage(newMsg.toString());
                        }
                    }

                }
            }
        }







    }

    @EventHandler
    public void onDisconnectEvent(PlayerDisconnectEvent e){
        Manager.chatTimer.remove(e.getPlayer().getUniqueId().toString());
    }



}
