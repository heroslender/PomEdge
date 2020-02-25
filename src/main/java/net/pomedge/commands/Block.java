package net.pomedge.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.main.Main;
import net.pomedge.utils.Opt;

public class Block implements ICommand {

    @Override
    public void handle(String[] args, MessageReceivedEvent event, String prefix, MessageChannel channel2,String msg) {

        String tag = msg.replace(prefix+"block", "");
        User user=null;
        try {
            user = Main.jda.getUserByTag(tag);
        }catch (Exception e) {
            channel2.sendMessage("O usuario "+tag+" nÃ£o existe").queue();
            return;
        }
        if (args.length < 2) {
            channel2.sendMessage("Uso: " + prefix + "block <TagDoMembro>").queue();
            return;
        } else if (!event.getAuthor().getId().equals("535862121255141378")) {
            channel2.sendMessage("Apenas o meu criador pode executar esse comando!").queue();

        } else if (Main.bannedUsers.contains(Main.jda.getUserByTag(tag).getId())) {
            channel2.sendMessage("O Usuario "+user.getAsMention()+" ja esta registrado como banido no meu banco de dados, pulando...").queue();
            return;
        } else {
            Main.bannedUsers.add(user.getId());
            Opt.saveJson();
            channel2.sendMessage("O Usuario "+user.getAsMention()+" foi banido com sucesso!").queue();
        }
    }

    @Override
    public String getInvoke() {
        return "block";
    }
}
