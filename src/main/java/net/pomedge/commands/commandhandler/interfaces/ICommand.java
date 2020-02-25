package net.pomedge.commands.commandhandler.interfaces;



import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public interface ICommand {

    void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg);

    String getInvoke();

}