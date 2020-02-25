package net.pomedge.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;

public class Ban implements ICommand {
    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2, String msg) {
        if(!event.getMember().hasPermission(Permission.BAN_MEMBERS)){
            EmbedBuilder bd = new EmbedBuilder();
            bd.setTitle("<a:error:680891547973320741> Você não tem permissão!");

            channel2.sendMessage(bd.build()).queue();
            return;
        }else if(event.getMessage().getMentionedMembers().isEmpty()){
            EmbedBuilder bd = new EmbedBuilder();
            bd.setTitle("<a:error:680891547973320741> Mencione o cara que vc quer banir");

            channel2.sendMessage(bd.build()).queue();
            return;
        }

        event.getMessage().getMentionedMembers().get(0).ban(0).queue();
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("<a:BanKey:680884407594385419> Usuario Banido!");
        bd.addField(new MessageEmbed.Field("Usuario: ",event.getMessage().getMentionedMembers().get(0).getAsMention(),true));
        channel2.sendMessage(bd.build()).queue();
    }

    @Override
    public String getInvoke() {
        return "ban";
    }
}
