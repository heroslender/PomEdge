package net.pomedge.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.music.GuildMusicManager;
import net.pomedge.music.PlayerManager;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

public class Pause implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        if (musicManager.player.getPlayingTrack() == null) {
            event.getTextChannel()
                    .sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                            "NÃ£o tenho nenhuma mÃºsica para pausar/despausar", prefix+"`pause`", Erros.ERRO).build())
                    .queue();
            ;
            return;
        }
        if (!musicManager.player.isPaused()) {
            musicManager.player.setPaused(true);
            event.getTextChannel().sendMessage(Opt.sucessEmbed(event.getAuthor(),
                    "Musica pausada com sucesso!\n DÃª `"+prefix+"pause` denovo para despausar!").build()).queue();
        } else {
            musicManager.player.setPaused(false);
            event.getTextChannel().sendMessage(Opt.sucessEmbed(event.getAuthor(),
                    "Musica despausada com sucesso!\n DÃª `"+prefix+"pause` denovo para pausar!").build()).queue();
        }
    }

    @Override
    public String getInvoke() {
        return "pause";
    }
}
