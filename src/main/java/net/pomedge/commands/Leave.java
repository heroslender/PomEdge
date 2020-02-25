package net.pomedge.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

public class Leave implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        if (!event.getGuild().getMember(event.getJDA().getSelfUser()).getVoiceState().inVoiceChannel()) {
            event.getChannel().sendTyping();
            EmbedBuilder bd = Opt.newEmbedSintaxe(event.getAuthor(),
                    "Como assim sair? Eu nem estou em um canal de voz ",
                    "`" + prefix + "leave`", Erros.ERRO);
            event.getChannel().sendMessage(bd.build()).queue();
        } else {
            event.getGuild().getAudioManager().closeAudioConnection();
            event.getChannel().sendTyping();
            EmbedBuilder bd = Opt.sucessEmbed(event.getAuthor(), "Eu sai do canal de voz com sucesso!");

            event.getChannel().sendMessage(bd.build()).queue();
        }
    }

    @Override
    public String getInvoke() {
        return "leave";
    }
}
