package net.pomedge.events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pomedge.main.Main;

public class ReactionEvents extends ListenerAdapter {
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {



		Message msg = event.getChannel().getHistory().retrievePast(1).complete().get(0);
		if(event.getUser() == event.getJDA().getSelfUser()) return;
		if(event.getReactionEmote().getId() == "680732802244673598") {
			
		}
	}
	
}
