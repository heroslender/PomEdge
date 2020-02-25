package net.pomedge.commands.commandhandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.pomedge.commands.*;
import net.pomedge.commands.commandhandler.interfaces.ICommand;
import net.pomedge.utils.Opt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CommandHandlerManager {

    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandHandlerManager() {
        addCommand(new Aviso());
        addCommand(new Ban());
        addCommand(new Block());
        addCommand(new ClearMsg());
        addCommand(new Config());
        addCommand(new Help());
        addCommand(new Join());
        addCommand(new Leave());
        addCommand(new mostrarPosicao());
        addCommand(new Pause());
        addCommand(new Ping());
        addCommand(new Play());
        addCommand(new Queue());
        addCommand(new SetPrefix());
        addCommand(new Skip());
        addCommand(new Stop());
        addCommand(new UnBlock());
        addCommand(new Volume());
        addCommand(new Wpn());
    }

    private void addCommand(ICommand command) {
        if(!commands.containsKey(command.getInvoke())) {
            commands.put(command.getInvoke(), command);
        }
    }

    public void handleCommand(MessageReceivedEvent event) {
        final String[] split = event.getMessage().getContentRaw().replaceFirst(
                "(?i)" + Pattern.quote(Opt.getServerPrefix(event.getGuild().getId())), "").split("\\s+");
        final String invoke = split[0].toLowerCase();

        if(commands.containsKey(invoke)) {
            final List<String> args = Arrays.asList(split).subList(1, split.length);

            commands.get(invoke).handle((String[]) args.toArray(), event, Opt.getServerPrefix(event.getGuild().getId()),event.getChannel(), event.getMessage().getContentRaw());
        }
    }
}
