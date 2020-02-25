package net.pomedge.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.music.GuildMusicManager;
import net.pomedge.music.PlayerManager;
import net.pomedge.music.TrackScheduler;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

public class Skip implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        TextChannel channel = event.getTextChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;
        if (player.getPlayingTrack() == null) {
            channel.sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                    "Não estou tocando nada...",
                    prefix + "skip", Erros.ERRO).build()).queue();

            return;
        }
        channel.sendMessage(Opt.sucessEmbed(event.getAuthor(),
                "A musica '" + player.getPlayingTrack().getInfo().title + "' foi pulada").build()).queue();
        scheduler.nextTrack();
    }

    @Override
    public String getInvoke() {
        return "skip";
    }
}
