package net.pomedge.commands;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.music.PlayerManager;
import net.pomedge.utils.Erros;
import net.pomedge.utils.Opt;

public class Play implements ICommand {

    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {

        String input = msg.replace(prefix + "play", "");
        if (!event.getGuild().getMember(event.getJDA().getSelfUser()).getVoiceState().inVoiceChannel()) {
            event.getChannel().sendTyping();
            EmbedBuilder bd = Opt.newEmbedSintaxe(event.getAuthor(),
                    "Use `-join`, porque eu nao estou em um canal de voz!", "`" + prefix + "play`", Erros.ERRO);
            event.getChannel().sendMessage(bd.build()).queue();
            return;
        } else if (args.length == 1) {

            event.getChannel().sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                    "VocÃª nÃ£o passou pra mim a musica que vc quer", "`" + prefix + "play`", Erros.SINTAXE).build())
                    .queue();
        }


        try {
            Opt.youTube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(), null).setApplicationName("PomEdge")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }



        if (input.isEmpty()) {
            event.getChannel()
                    .sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                            "VocÃª nÃ£o colocou nenhum argumento: \n `" + prefix + "play <Titulo da musica>`",
                            prefix + "play", Erros.SINTAXE).build());

            return;
        }

        if (!Opt.isUrl(input)) {
            MessageAction msg1 = event.getChannel()
                    .sendMessage(Opt.sucessEmbed(event.getAuthor(), "Proucurando...").build());

            msg1.queue();
            String ytSearched = Opt.searchYoutube(input);

            if (ytSearched == null) {
                event.getChannel().sendMessage(Opt.newEmbedSintaxe(event.getAuthor(),
                        "Nao foram encontrados resultados!", prefix + "play", Erros.ERRO).build());

                return;
            }

            input = ytSearched;
        }

        PlayerManager manager = PlayerManager.getInstance();
        manager.loadAndPlay(event.getTextChannel(), input, event.getAuthor(), event);

    }

    @Override
    public String getInvoke() {
        return "play";
    }
}
