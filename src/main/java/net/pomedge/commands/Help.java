package net.pomedge.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.main.Main;
import net.pomedge.utils.Opt;

import java.util.ArrayList;

public class Help implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Ajuda:");
        bd.addField("Clique em <a:Music:680732802244673598>:","  para ver os comandos de musica", true);
        Message msg1 = channel2.sendMessage(bd.build()).complete();
        ArrayList<String> emojis = new ArrayList<String>();
        emojis.add("680732802244673598");
        emojis.add("681866042623918091");
        for(String emoji: emojis) {
            System.out.println(emoji);
            msg1.addReaction(Main.jda.getGuildById("680488335453585409").getEmoteById(emoji)).queue();

        }


        Opt.helpId.add(msg1.getId());
    }

    @Override
    public String getInvoke() {
        return null;
    }
}
