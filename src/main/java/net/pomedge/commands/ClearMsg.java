package net.pomedge.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;

import java.util.List;

public class ClearMsg implements ICommand {

    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {


        if (args.length < 2) {
            channel2.sendMessage("Uso: " + prefix + "clearmsg <msgs>").queue();
            return;
        }
        try {
            Integer teste = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            channel2.sendMessage("O numero especificado esta invalido!").queue();
            return;
        }
        List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
        channel2.purgeMessages(messages);
        channel2.sendMessage("YaY! Foram eliminadas " + args[1] + " mensagens!").queue();
    }

    @Override
    public String getInvoke() {
        return "clearmsg";
    }
}
