package net.pomedge.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;

import java.awt.*;

public class Aviso implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("@everyone" + event.getMessage().getContentRaw().replace(prefix+"aviso","") + ".").queue();
            event.getMessage().delete().queue();
        }
        else {
            EmbedBuilder ebb = new EmbedBuilder();
            ebb.setTitle("Erro");
            ebb.setDescription("so Administradores tem acesso a este comando");
            ebb.setColor(Color.red);
            event.getChannel().sendMessage(ebb.build()).queue();
        }
    }

    @Override
    public String getInvoke() {
        return "aviso";
    }
}
