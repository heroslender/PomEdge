package net.pomedge.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pomedge.commands.commandhandler.CommandHandlerManager;
import net.pomedge.main.Main;
import net.pomedge.utils.Opt;

public class Commands extends ListenerAdapter {
    private final CommandHandlerManager manager = new CommandHandlerManager();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        TextChannel channel2 = event.getMessage().getTextChannel();
        String msg = event.getMessage().getContentRaw();
        String[] args = event.getMessage().getContentRaw().split(" ");
        MessageChannel channel3 = event.getChannel();

        String prefix;

        prefix = (String) Main.jsonReader.get(event.getGuild().getId() + "Prefix");
        if (prefix == null) {
            prefix = "pe!";
            Main.jsonReader.put(event.getGuild().getId() + "Prefix", "pe!");
            Opt.saveJson();
        }

        if (Main.bannedUsers.contains(event.getAuthor().getId()) && msg.startsWith(prefix)) {
            channel2.sendMessage(
                    "Infelizmente, nao podes executar nenhum comando, porque tu quebrou os nossos termos, e o meu criador baniu você!")
                    .queue();
            return;
        }
        if (!event.getAuthor().isBot() && !event.getMessage().isWebhookMessage() &&
                event.getMessage().getContentRaw().startsWith(prefix)) {
            manager.handleCommand(event);
        }
    }
}
