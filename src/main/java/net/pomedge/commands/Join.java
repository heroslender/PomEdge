package net.pomedge.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

import java.util.ArrayList;

public class Join implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {

        ArrayList<Permission> permissions = new ArrayList<Permission>();
        permissions.add(Permission.VOICE_SPEAK);
        permissions.add(Permission.VOICE_CONNECT);
        permissions.add(Permission.VOICE_STREAM);
        GuildVoiceState memberVoiceState = event.getGuild().getMember(event.getAuthor()).getVoiceState();
        if (memberVoiceState.inVoiceChannel()) {
            if (!event.getGuild().getMember(event.getJDA().getSelfUser()).hasPermission(permissions)) {
                EmbedBuilder bd = Opt.newEmbedSintaxe(event.getAuthor(),
                        "Eu nao tenho permissao  para entrar no seu canal de voz >:(", "`" + prefix + "join`",
                        Erros.ERRO);
                event.getChannel().sendMessage(bd.build()).queue();
                return;
            } else if (event.getGuild().getMember(event.getJDA().getSelfUser()).getVoiceState().inVoiceChannel()) {
                EmbedBuilder bd = Opt.newEmbedSintaxe(event.getAuthor(), "Eu jÃ¡ estou em outro canal de audio",
                        "`" + prefix + "join`", Erros.ACESSO_NEGADO);
                event.getChannel().sendMessage(bd.build()).queue();
                return;
            }
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
            event.getChannel().sendTyping();

            EmbedBuilder bd = Opt.sucessEmbed(event.getAuthor(), "Eu Entrei no seu canal de voz com sucesso!");
            event.getChannel().sendMessage(bd.build()).queue();
        } else {
            event.getChannel().sendTyping();
            EmbedBuilder bd = Opt.newEmbedSintaxe(event.getAuthor(),
                    "Voce tem que estar em um canal de som para executar esse comando", "`-join`", Erros.ERRO);
            event.getChannel().sendMessage(bd.build()).queue();

            return;
        }
    }

    @Override
    public String getInvoke() {
        return "join";
    }
}
