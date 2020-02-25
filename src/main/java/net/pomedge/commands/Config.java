package net.pomedge.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.utils.Opt;

public class Config implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        if(args.length < 2) {
            EmbedBuilder ebb = new EmbedBuilder();
            ebb.setTitle("<a:error:680891547973320741> Erro");
            ebb.setDescription("Uso: " + prefix + "config algumTipoDeConfigura��o o que tu queres mudar");
        }
        if(!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {return;}
        if(args[1].equals("msgEntrada")){
            Opt.setJsonElement(event.getGuild().getId()+"msgEntrada", Boolean.parseBoolean(args[2]));
            Opt.saveJson();
            event.getChannel().sendMessage("msgEntrada setado para:"+args[2]).queue();
            if(Boolean.parseBoolean(args[2])){
                event.getChannel().sendMessage( "Agora configure o canal de texto para isso funcionar corretamente: \n "+prefix+"config msgEntradaChannel <Canal>").queue();
            }
        } else if(args[1].equals("msgEntradaChannel")){
            Opt.setJsonElement(event.getGuild().getId()+"msgEntradaChannel", event.getMessage().getMentionedChannels().get(0).getId());
            Opt.saveJson();
            event.getChannel().sendMessage("msgEntradaChannel setado para:"+args[2]).queue();
        }
    }

    @Override
    public String getInvoke() {
        return "config";
    }
}
