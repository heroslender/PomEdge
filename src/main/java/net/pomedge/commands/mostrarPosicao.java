package net.pomedge.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.music.PlayerManager;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

public class mostrarPosicao implements ICommand {

    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        if(PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.getPlayingTrack() == null) {
            channel2.sendMessage(Opt.newEmbedSintaxe(event.getAuthor(), "Não estou tocando nada", "'"+prefix+"mostrarDuracao'", Erros.ERRO).build()).queue();
            return;
        }
        AudioPlayer player = PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player;
        Long durationInMs = player.getPlayingTrack().getPosition();
        String durationInSec = Opt.truncate(durationInMs.toString(), durationInMs.toString().length() - 3);
        channel2.sendMessage("O video esta em "+durationInSec+"s").queue();
    }

    @Override
    public String getInvoke() {
        return "mostrarposicao";
    }
}
