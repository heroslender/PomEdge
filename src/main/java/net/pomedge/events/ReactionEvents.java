package net.pomedge.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pomedge.main.Main;
import net.pomedge.utils.Opt;

import java.io.Console;
import java.util.ArrayList;

public class ReactionEvents extends ListenerAdapter {
	@Override
	public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
		if(!Opt.helpId.contains(event.getMessageId())){
			return;
		}
		Message msg;
		if(event.getUser() == event.getJDA().getSelfUser()) return;
		if(event.getReactionEmote().getName().equals("Music")) {
			TextChannel channel = (TextChannel) event.getChannel();
			msg = channel.retrieveMessageById(event.getMessageId()).complete();
			EmbedBuilder ebb = new EmbedBuilder();
			ebb.setTitle("Comando de musica");
			ebb.addField("```play```", "```este comando serve para quando tu queres colocar uma musica,\nUso: <prefix>play <musica>```", true);
			ebb.addField("```join```", "```este comando serve para quando tu queres colocar o bot na mesma sala de voz que tu esta,\nUso: <prefix>join```", true);
			ebb.addField("```mostrarPosicao```", "```este comando serve para quando tu queres saber qual posicao a musica esta,\nUso: <prefix>mostrarPosicao <nomeDaMusica>```", true);
			ebb.addField("```volume```", "```este comando serve para quando tu queres diminuir/aumentar o volume da musica,\nUso: <prefix>volume <aPorcentagem>```", true);
			ebb.addField("```leave```", "```este comando serve para quando tu queres tirar o bot da sala de voz que tu estas,\nUso: <prefix>leave```", true);
			ebb.addField("```skip```", "```este comando serve para quando tu queres pular a musica que esta a tocar,\nUso: <prefix>skip```", true);
			ebb.addField("```stop```", "```este comando serve para quando tu queres parar de tocar as musicas e tambem vai deletar toda a fila de musicas que tu criaste,\nUso: <prefix>stop```", true);
			ebb.addField("```pause```", "```este comando serve para quando tu queres pausar/despausar a musica,\nUso: <prefix>pause```", true);
			ebb.addField("```queue```", "```este comando serve para quando tu queres ver as musicas que estão na fila,\nUso: <prefix>volume <aPorcentagem>```", true);
			ebb.addField("```wpn?```", "```este comando serve para quando tu queres saber a musica que esta a tocar,\nUso: <prefix>wpn?```", true);
			msg.editMessage(ebb.build()).queue();
		}
	}

}
