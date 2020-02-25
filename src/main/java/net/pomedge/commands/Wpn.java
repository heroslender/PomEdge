package net.pomedge.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.music.GuildMusicManager;
import net.pomedge.music.PlayerManager;
import net.pomedge.utils.Opt;

public class Wpn implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        if (musicManager.player.getPlayingTrack() == null) {
            event.getTextChannel()
                    .sendMessage(Opt.sucessEmbed(event.getAuthor(), "NÃ£o estou cantando nada.").build()).queue();
        } else {
            event.getTextChannel()
                    .sendMessage(Opt
                            .sucessEmbed(event.getAuthor(),
                                    "Estou cantando: " + musicManager.player.getPlayingTrack().getInfo().title)
                            .build())
                    .queue();
        }
    }

    @Override
    public String getInvoke() {
        return "wpn?";
    }
}
