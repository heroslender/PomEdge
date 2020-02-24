package net.pomedge.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pomedge.main.Main;
import net.pomedge.utils.Opt;

import java.util.ArrayList;

public class ReactionEvents extends ListenerAdapter {
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		if(!Opt.helpId.contains(event.getMessageId())){
			return;
		}
		Message msg = event.getChannel().getHistory().getMessageById(event.getMessageId());
		if(event.getUser() == event.getJDA().getSelfUser()) return;
		if(event.getReactionEmote().getId() == "680732802244673598") {
			EmbedBuilder ebb = new EmbedBuilder();
			ebb.setTitle("Comando de musica");
			ebb.addField("play","este comando serve para quando tu queres colocar uma musica, Uso: play <musica>",true);
			ebb.addField("join","este comando serve para quando tu queres colocar o bot na mesma sala de voz que tu esta, Uso: play <musica>",true);
			ebb.addField("play","este comando serve para quando tu queres colocar uma musica, Uso: play <musica>",true);
			msg.editMessage(ebb.build()).queue();
		}
	}
	
}
