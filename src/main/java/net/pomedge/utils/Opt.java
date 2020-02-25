package net.pomedge.utils;

import java.awt.Color;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.pomedge.main.Main;

import javax.annotation.Nullable;

public class Opt {
	public static ArrayList<String> helpId = new ArrayList<String>();
	public static YouTube youTube;
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
	public static String getServerPrefix(String GuildId){

		String prefix;

		prefix = (String) Main.jsonReader.get(GuildId + "Prefix");
		if (prefix == null) {
			prefix = "pe!";
			Main.jsonReader.put(GuildId + "Prefix", "pe!");
			Opt.saveJson();
		}
		return prefix;
	}
	public static EmbedBuilder sucessEmbed(User author, String description) {
		EmbedBuilder bd = new EmbedBuilder();
		bd.setTitle(author.getJDA().getSelfUser().getName() + " | Sucesso!");
		bd.setColor(Color.green);
		bd.setDescription(description);
		bd.setFooter(author.getName(), author.getAvatarUrl());
		return bd;
	}
	@Nullable
	public static Object getJsonElement(Object key){
		return Main.jsonReader.get(key);
	}
	public static void setJsonElement(Object key ,Object value){
		Main.jsonReader.put(key,value);
	}
	public static void saveJson() {

		FileWriter sw;
		Main.jsonReader.put("bannedUsers", Main.bannedUsers);
		try {
			sw = new FileWriter("config.json");
			sw.write(Main.jsonReader.toString());
			sw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	public static String truncate(String value, int length) {
		// Ensure String length is longer than requested size.
		if (value.length() > length) {
			return value.substring(0, length);
		} else {
			return value;
		}
	}
	public static boolean isUrl(String input) {
		URLConnection con;
		try {

			con = new URL(input).openConnection();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		con.addRequestProperty("User-Agent","Mozilla");
		try {
			con.connect();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Nullable
	public static String searchYoutube(String input) {
		try {
			ArrayList<SearchResult> results = new ArrayList<SearchResult>();
			for (SearchResult result : youTube.search().list("id,snippet").setQ(input).setMaxResults(1L)
					.setType("video")
					.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
					.setKey("AIzaSyCDwopzp8zgVdXtxWBz3FFc9TEdBUuqis4").execute().getItems()) {
				results.add(result);
			}
			if (!results.isEmpty()) {
				String videoId = results.get(0).getId().getVideoId();
				System.out.println("https://www.youtube.com/watch?v=" + videoId);
				return "https://www.youtube.com/watch?v=" + videoId;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}