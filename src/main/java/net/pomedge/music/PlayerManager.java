package net.pomedge.music;


import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
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
import net.pomedge.utils.Erros;
import net.pomedge.utils.Utils;

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
    private String truncate(String value, int length) {
        // Ensure String length is longer than requested size.
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }
    public void loadAndPlay(TextChannel channel, String trackUrl, User author, MessageReceivedEvent event) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                Long trackDuration = track.getDuration();
            	EmbedBuilder bd = new EmbedBuilder();
            	bd.setTitle("<a:Music:680732802244673598> Discoteca:");
            	bd.setDescription("Foi adicionada essa musica na fila: \n" +
                        "   "+track.getInfo().title + "\n Duração: "+ truncate(trackDuration.toString(), trackDuration.toString().length() - 3) + "s");
            	channel.sendMessage(bd.build()).queue();

                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().remove(0);
                }

                
                EmbedBuilder bd = Utils.sucessEmbed(author, "A PlayList "+playlist.getName()+" foi carregada com sucesso");
            	bd.setImage("https://i.ytimg.com/vi/"+firstTrack.getInfo().uri.replace("https://www.youtube.com/watch?v=", "")+"/hqdefault.jpg");
            	channel.sendMessage(bd.build()).queue();
                play(musicManager, firstTrack);

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

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
        
    }

    public static synchronized PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}