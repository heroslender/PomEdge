package net.pomedge.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.main.Main;
import net.pomedge.utils.Opt;

import java.util.List;

public class SetPrefix implements ICommand {

    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg)
        {
            if(!event.getMember().hasPermission(Permission.MANAGE_SERVER)) return;
            if (args.length < 1) {
                channel2.sendMessage("Uso: " + prefix + "setprefix <Novo Prefixo>").queue();
                return;
            }
            prefix = args[1];
            Main.jsonReader.put(event.getGuild().getId() + "Prefix", args[1]);
            Opt.saveJson();
            channel2.sendMessage("Prefixo alterado com sucesso!, o seu prefixo agora esta como '" + prefix + "'").queue();

        }


    @Override
    public String getInvoke() {
        return "setprefix";
    }
}
