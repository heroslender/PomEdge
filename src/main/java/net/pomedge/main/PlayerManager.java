package net.pomedge.main;


import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;
    private boolean isLoop = false;
    public PlayerManager() {
        this.musicManagers = new HashMap<>();

        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
    }

    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }

    public void loadAndPlay(TextChannel channel, String trackUrl, User author, MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
          
            	EmbedBuilder bd =Utils.sucessEmbed(author, "A Musica "+track.getInfo().title + " foi Adicionada á fila com sucesso");
            	bd.setImage("https://i.ytimg.com/vi/"+track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "")+"/hqdefault.jpg");
            	channel.sendMessage(bd.build()).queue();

                play(musicManager, track, channel, author,track.getInfo().title, "https://i.ytimg.com/vi/"+track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "")+"/hqdefault.jpg");
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                }

                
                EmbedBuilder bd =Utils.sucessEmbed(author, "A PlayList "+playlist.getName()+" foi carregada com sucesso");
            	bd.setImage("https://i.ytimg.com/vi/"+firstTrack.getInfo().uri.replace("https://www.youtube.com/watch?v=", "")+"/hqdefault.jpg");
            	channel.sendMessage(bd.build()).queue();
                play(musicManager, firstTrack, channel, author, firstTrack.getInfo().title,"https://i.ytimg.com/vi/"+firstTrack.getInfo().uri.replace("https://www.youtube.com/watch?v=", "")+"/hqdefault.jpg");

                playlist.getTracks().forEach(musicManager.scheduler::queue);
            }

            @Override
            public void noMatches() {
                channel.sendMessage(Utils.newEmbedSintaxe(author, "URL Errada ou nenhum item encontrado na pesquisa", "-play", Erros.SINTAXE).build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            	channel.sendMessage(Utils.newEmbedSintaxe(author, "Ocorreu um erro por parte nossa,\n por favor, entre em contacto connosco: <@535862121255141378>", "-play", Erros.ERRO).build()).queue();
            }
        });

    }

    private void play(GuildMusicManager musicManager, AudioTrack track, TextChannel channel, User author, String title, String tumbUrl) {
        musicManager.scheduler.queue(track);
        
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}