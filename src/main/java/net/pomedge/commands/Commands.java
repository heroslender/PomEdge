package net.pomedge.commands;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.pomedge.commands.commandhandler.CommandHandlerManager;
import net.pomedge.main.*;
import net.pomedge.music.GuildMusicManager;
import net.pomedge.music.PlayerManager;
import net.pomedge.music.TrackScheduler;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Commands extends ListenerAdapter {


    private final CommandHandlerManager manager = new CommandHandlerManager();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        TextChannel channel2 = event.getMessage().getTextChannel();
        String msg = event.getMessage().getContentRaw();
        String[] args = event.getMessage().getContentRaw().split(" ");
        MessageChannel channel3 = event.getChannel();

        String prefix;

        prefix = (String) Main.jsonReader.get(event.getGuild().getId() + "Prefix");
        if (prefix == null) {
            prefix = "pe!";
            Main.jsonReader.put(event.getGuild().getId() + "Prefix", "pe!");
            Opt.saveJson();
        }

        if (Main.bannedUsers.contains(event.getAuthor().getId()) && msg.startsWith(prefix)) {
            channel2.sendMessage(
                    "Infelizmente, nao podes executar nenhum comando, porque tu quebrou os nossos termos, e o meu criador baniu você!")
                    .queue();
            return;
        }
        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() &&
                event.getMessage().getContentRaw().startsWith(prefix)) {
            manager.handleCommand(event);
        }

    }

}
