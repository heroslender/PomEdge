package net.pomedge.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;

public class Ping implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Ping:");
        bd.addField("Ping da internet: ",String.valueOf(event.getJDA().getGatewayPing()),true);
        bd.addField("Ping da API: ",String.valueOf(event.getJDA().getRestPing().complete()),true);
        channel2.sendMessage(bd.build()).queue();
    }

    @Override
    public String getInvoke() {
        return "ping";
    }
}
