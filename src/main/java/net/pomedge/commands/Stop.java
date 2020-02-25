package net.pomedge.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.music.GuildMusicManager;
import net.pomedge.music.PlayerManager;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

public class Stop implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        if (musicManager.player.getPlayingTrack() == null) {
            event.getTextChannel().sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                    "Não tenho nenhuma musica em reprodução", "`" + prefix + "parar`", Erros.ERRO).build()).queue();
            return;
        } else
            musicManager.scheduler.getQueue().clear();
        musicManager.player.stopTrack();
        musicManager.scheduler.getQueue().clear();
        musicManager.player.setPaused(false);

        event.getTextChannel().sendMessage(Opt
                .sucessEmbed(event.getAuthor(), "A musica foi parada e a  minha playlist foi deletada!").build())
                .queue();
    }

    @Override
    public String getInvoke() {
        return "stop";
    }
}
