package net.pomedge.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.music.GuildMusicManager;
import net.pomedge.music.PlayerManager;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

public class Volume implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        if (args.length == 1) {
            event.getChannel()
                    .sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                            "Coloque um numero de `0 - 100`:\n "+prefix+"volume <NÃºmero>`", "`"+prefix+"volume`", Erros.SINTAXE)
                            .build())
                    .queue();
        }
        int volume;
        try {
            volume = Integer.parseInt(args[1]);

        } catch (NumberFormatException e) {
            event.getChannel()
                    .sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                            "Por favor, digite um numero valido! `0 - 100`", "`"+prefix+"volume`", Erros.SINTAXE).build())
                    .queue();
            return;
        }
        if (volume < 0 || volume > 100) {
            event.getChannel()
                    .sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                            "Por favor, digite um numero valido! `0 - 100`", "`"+prefix+"volume`", Erros.SINTAXE).build())
                    .queue();
            return;
        }
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        musicManager.player.setVolume(volume);
        event.getTextChannel()
                .sendMessage(Opt.sucessEmbed(event.getAuthor(), "Volume alterado com sucesso!").build()).queue();
    }

    @Override
    public String getInvoke() {
        return "volume";
    }
}
