package net.pomedge.utils;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public class Utils {
	
	public static EmbedBuilder newEmbedSintaxe(User author, String description, String cmd, Erros sintaxe) {

		EmbedBuilder bd = new EmbedBuilder();
		if (sintaxe == Erros.ERRO) {
			bd.setTitle(author.getJDA().getSelfUser().getName() + " | Ocorreu um Erro!");
			bd.setColor(Color.red);
			bd.setDescription("" + description);
			bd.setFooter(author.getName(), author.getAvatarUrl());
		} else if (sintaxe == Erros.SINTAXE) {
			
			bd.setTitle(author.getJDA().getSelfUser().getName() + " | Sintaxe errada - " + cmd);
			bd.setColor(Color.red);
			bd.setDescription("" + description);
			bd.setFooter(author.getName(), author.getAvatarUrl());

		} else if (sintaxe == Erros.ACESSO_NEGADO) {
			bd.setTitle("**" + author.getJDA().getSelfUser().getName() + " | Acesso Negado!**");
			bd.setColor(Color.red);
			bd.setDescription("" + description);
			bd.setFooter(author.getName(), author.getAvatarUrl());
		}
		return bd;
	}

	public static EmbedBuilder sucessEmbed(User author, String description) {
		EmbedBuilder bd = new EmbedBuilder();
		bd.setTitle(author.getJDA().getSelfUser().getName() + " | Sucesso!");
		bd.setColor(Color.green);
		bd.setDescription(description);
		bd.setFooter(author.getName(), author.getAvatarUrl());
		return bd;
	}
}