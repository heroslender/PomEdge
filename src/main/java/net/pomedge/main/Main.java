package net.pomedge.main;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class Main {
	public static JSONArray bannedUsers;
	public static JDA jda;
	static JSONParser jp = new JSONParser();
	static JSONObject jsonReader;

	public static void main(String[] args) {
		JDABuilder jdabuilder = new JDABuilder(AccountType.BOT);
		try {
			FileReader s = new FileReader("settings.json");
			jsonReader = (JSONObject) jp.parse(s);

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		jdabuilder.addEventListeners(new Commands());

		bannedUsers = (JSONArray) jsonReader.get("bannedUsers");

		jdabuilder.setToken((String) jsonReader.get("token"));
		try {
			jda = jdabuilder.build();
			jda.awaitReady();

		} catch (LoginException e) {
			System.out.println(
					"ERRO AO LOGAR! \n Soluções comuns: \n Verifique sua conexão com a internet \n Verifique o token");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void writeJson() {

		FileWriter sw;
		jsonReader.put("bannedUsers", bannedUsers);
		try {
			sw = new FileWriter("settings.json");
			sw.write(jsonReader.toString());
			sw.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

}

class Commands extends ListenerAdapter {
	public static YouTube youTube;

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		TextChannel channel2 = event.getMessage().getTextChannel();
		String msg = event.getMessage().getContentRaw();
		String[] args = event.getMessage().getContentRaw().split(" ");
		String prefix;

		prefix = (String) Main.jsonReader.get(event.getGuild().getId() + "Prefix");
		if (prefix == null) {
			prefix = "!";
			Main.jsonReader.put(event.getGuild().getId() + "Prefix", "!");
			Main.writeJson();
		}
		if (Main.bannedUsers.contains(event.getAuthor().getId()) && msg.startsWith(prefix)) {
			channel2.sendMessage(
					"Infelizmente, não podes executar nenhum comando, porque você quebrou os nossos termos, e o meu criador baniu você!")
					.queue();
			return;
		}
		if (args[0].equals(prefix + "setPrefix")) {
			if (args.length < 2) {
				channel2.sendMessage("Uso: " + prefix + "setprefix <Novo Prefixo>").queue();
				return;
			}
			prefix = args[1];
			Main.jsonReader.put(event.getGuild().getId() + "Prefix", args[1]);
			Main.writeJson();
			channel2.sendMessage("Prefixo alterado com sucesso!, o seu prefixo agora é '" + prefix + "'").queue();

		}

		if (args[0].equals(prefix + "banUser")) {
			String tag = msg.replace(prefix+"banUser ", ""); 
			try {
				User teste = Main.jda.getUserByTag(tag);
			}catch (Exception e) {
				channel2.sendMessage("O usuario "+tag+" não existe").queue();
				return;
			}
			if (args.length < 2) {
				channel2.sendMessage("Uso: " + prefix + "banUser <TagDoMembro>").queue();
				return;
			} else if (!event.getAuthor().getId().equals("535862121255141378")) {
				channel2.sendMessage("Apenas o meu criador pode executar esse comando!").queue();
				
			} else if (Main.bannedUsers.contains(Main.jda.getUserByTag(tag).getId())) {
				channel2.sendMessage("O Usuario @"+tag+" ja está registrado como banido no meu banco de dados, pulando...").queue();
				return;
			} else {
				Main.bannedUsers.add(Main.jda.getUserByTag(tag).getId());
				Main.writeJson();
				channel2.sendMessage("O Usuario @"+tag+" foi banido com sucesso!").queue();
			}
		}
		
		if (args[0].equals(prefix + "unbanUser")) {
			String tag = msg.replace(prefix+"unbanUser ", ""); 
			try {
				User teste = Main.jda.getUserByTag(tag);
			}catch (IllegalArgumentException e) {
				channel2.sendMessage("O usuario "+tag+" não existe").queue();
				return;
			}
			if (args.length < 2) {
				channel2.sendMessage("Uso: " + prefix + "unbanUser <TagDoMembro>").queue();
				return;
			} else if (!event.getAuthor().getId().equals("535862121255141378")) {
				channel2.sendMessage("Apenas o meu criador pode executar esse comando!").queue();
				
			} else if (!Main.bannedUsers.contains(Main.jda.getUserByTag(tag).getId())) {
				channel2.sendMessage("O Usuario @"+tag+" não está banido, pulando...").queue();
				return;
			} else {
				Main.bannedUsers.remove(Main.jda.getUserByTag(tag).getId());
				Main.writeJson();
				channel2.sendMessage("O Usuario @"+tag+" foi desbanido com sucesso!").queue();
			}
		}
		if (args[0].equals(prefix + "clearmsg")) {
			if (args.length < 2) {
				channel2.sendMessage("Uso: " + prefix + "clearmsg <msgs>").queue();
				return;
			}
			try {
				Integer teste = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				channel2.sendMessage("O numero especificado é invalido!").queue();
				return;
			}
			List<Message> messages = event.getChannel().getHistory().retrievePast(Integer.parseInt(args[1])).complete();
			channel2.purgeMessages(messages);
			channel2.sendMessage("YaY! Foram eliminadas " + args[1] + " mensagens!").queue();
		}
		if (args[0].equals (prefix + "join")) {
			ArrayList<Permission> permissions = new ArrayList<Permission>();
			permissions.add(Permission.VOICE_SPEAK);
			permissions.add(Permission.VOICE_CONNECT);
			permissions.add(Permission.VOICE_STREAM);
			GuildVoiceState memberVoiceState = event.getGuild().getMember(event.getAuthor()).getVoiceState();
			if (memberVoiceState.inVoiceChannel()) {
				if (!event.getGuild().getMember(event.getJDA().getSelfUser()).hasPermission(permissions)) {
					EmbedBuilder bd = Utils.newEmbedSintaxe(event.getAuthor(),
							"Eu nao tenho permissao  para entrar no seu canal de voz >:(", "`" + prefix + "join`",
							Erros.ERRO);
					event.getChannel().sendMessage(bd.build()).queue();
					return;
				} else if (event.getGuild().getMember(event.getJDA().getSelfUser()).getVoiceState().inVoiceChannel()) {
					EmbedBuilder bd = Utils.newEmbedSintaxe(event.getAuthor(), "Eu já estou em outro canal de audio",
							"`" + prefix + "join`", Erros.ACESSO_NEGADO);
					event.getChannel().sendMessage(bd.build()).queue();
					return;
				}
				event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
				event.getChannel().sendTyping();

				EmbedBuilder bd = Utils.sucessEmbed(event.getAuthor(), "Eu Entrei no seu canal de voz com sucesso!");
				event.getChannel().sendMessage(bd.build()).queue();
			} else {
				event.getChannel().sendTyping();
				EmbedBuilder bd = Utils.newEmbedSintaxe(event.getAuthor(),
						"Voce tem que estar em um canal de som para executar esse comando", "`-join`", Erros.ERRO);
				event.getChannel().sendMessage(bd.build()).queue();

				return;
			}

		} else if (args[0].equals(prefix + "play")) {

			String input = msg.replace(prefix + "play", "");
			if (!event.getGuild().getMember(event.getJDA().getSelfUser()).getVoiceState().inVoiceChannel()) {
				event.getChannel().sendTyping();
				EmbedBuilder bd = Utils.newEmbedSintaxe(event.getAuthor(),
						"Use `-join`, porque eu nao estou em um canal de voz!", "`" + prefix + "play`", Erros.ERRO);
				event.getChannel().sendMessage(bd.build()).queue();
				return;
			} else if (args.length == 1) {

				event.getChannel().sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
						"Você não passou pra mim a musica que vc quer", "`" + prefix + "play`", Erros.SINTAXE).build())
						.queue();
			}
			YouTube temp = null;

			try {
				temp = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(),
						JacksonFactory.getDefaultInstance(), null).setApplicationName("Menudocs JDA tutorial bot")
								.build();
			} catch (Exception e) {
				e.printStackTrace();
			}

			youTube = temp;

			if (input.isEmpty()) {
				event.getChannel()
						.sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
								"Você não colocou nenhum argumento: \n `" + prefix + "play <Titulo da musica>`",
								prefix + "play", Erros.SINTAXE).build());

				return;
			}

			if (!isUrl(input)) {
				MessageAction msg1 = event.getChannel()
						.sendMessage(Utils.sucessEmbed(event.getAuthor(), "Proucurando...").build());

				msg1.queue();
				String ytSearched = searchYoutube(input);

				if (ytSearched == null) {
					event.getChannel().sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
							"Nao foram encontrados resultados!", prefix + "play", Erros.ERRO).build());

					return;
				}

				input = ytSearched;
			}

			PlayerManager manager = PlayerManager.getInstance();
			manager.loadAndPlay(event.getTextChannel(), input, event.getAuthor(), event);

		} else if (args[0].equals(prefix + "leave") || args[0].equals(prefix + "sair")) {
			if (!event.getGuild().getMember(event.getJDA().getSelfUser()).getVoiceState().inVoiceChannel()) {
				event.getChannel().sendTyping();
				EmbedBuilder bd = Utils.newEmbedSintaxe(event.getAuthor(),
						"Como assim sair? Eu nem estou em um canal de voz <:baka:649725958156189727>",
						"`" + prefix + "leave`", Erros.ERRO);
				event.getChannel().sendMessage(bd.build()).queue();
			} else {
				event.getGuild().getAudioManager().closeAudioConnection();
				event.getChannel().sendTyping();
				EmbedBuilder bd = Utils.sucessEmbed(event.getAuthor(), "Eu sai do canal de voz com sucesso!");
				event.getChannel().sendMessage(bd.build()).queue();
			}
		} else if (args[0].equals(prefix + "skip") || args[0].equals(prefix + "pular")) {
			TextChannel channel = event.getTextChannel();
			PlayerManager playerManager = PlayerManager.getInstance();
			GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
			TrackScheduler scheduler = musicManager.scheduler;
			AudioPlayer player = musicManager.player;
			Role dj;
			try {
				dj = event.getGuild().getRolesByName("dj", true).get(0);
				dj.getName();
			} catch (IndexOutOfBoundsException e) {
				EmbedBuilder bd = new EmbedBuilder();
				bd.setColor(Color.MAGENTA);
				bd.setTitle("Não estou achando o cargo DJ.");
				bd.setDescription("O cargo DJ não existe, recorra a algum staff para criar esse cargo!");
				event.getTextChannel().sendMessage(bd.build()).queue();
				return;
			}
			if (!event.getMember().getRoles().contains(dj)) {
				EmbedBuilder bd = new EmbedBuilder();
				bd.setColor(Color.MAGENTA);
				bd.setTitle("Você não tem o cargo dj!");
				bd.setDescription("Hmm, você não tem permissão, porque, você nao tem o cargo DJ!");
				event.getTextChannel().sendMessage(bd.build()).queue();
				return;
			} else if (player.getPlayingTrack() == null) {
				channel.sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
						"Wtf, vc ta querendo pular uma musica, MAS EU NEM TO TOCANDO NADA<:baka:649725958156189727>",
						prefix + "skip", Erros.ERRO).build()).queue();

				return;
			}
			channel.sendMessage(Utils.sucessEmbed(event.getAuthor(),
					"A musica '" + player.getPlayingTrack().getInfo().title + "' foi pulada").build()).queue();
			scheduler.nextTrack();

		} else if (args[0].equals(prefix + "stop") || args[0].equals(prefix + "parar")) {
			PlayerManager playerManager = PlayerManager.getInstance();
			GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
			Role dj;
			try {
				dj = event.getGuild().getRolesByName("dj", true).get(0);
			} catch (IndexOutOfBoundsException e) {
				EmbedBuilder bd = new EmbedBuilder();
				bd.setColor(Color.MAGENTA);
				bd.setTitle("Não estou achando o cargo DJ.");
				bd.setDescription("O cargo DJ não existe, recorra a algum staff para criar esse cargo!");
				event.getTextChannel().sendMessage(bd.build()).queue();
				return;
			}
			if (!event.getMember().getRoles().contains(dj)) {
				EmbedBuilder bd = new EmbedBuilder();
				bd.setColor(Color.MAGENTA);
				bd.setTitle("Você não tem o cargo dj!");
				bd.setDescription("Hmm, você não tem permissão, porque, você nao tem o cargo DJ!");
				event.getTextChannel().sendMessage(bd.build()).queue();
				return;
			} else if (musicManager.player.getPlayingTrack() == null) {
				event.getTextChannel().sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
						"Não tenho nunhuma musica em reprodução", "`" + prefix + "parar`", Erros.ERRO).build()).queue();
				return;
			} else
				musicManager.scheduler.getQueue().clear();
			musicManager.player.stopTrack();
			musicManager.player.setPaused(false);

			event.getTextChannel().sendMessage(Utils
					.sucessEmbed(event.getAuthor(), "A musica foi parada e a  minha playlist foi deletada!").build())
					.queue();
		} else if (args[0].equals(prefix+"pause") || args[0].equals(prefix+"pausar")) {
			PlayerManager playerManager = PlayerManager.getInstance();
			GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
			Role dj;
			try {
				dj = event.getGuild().getRolesByName("dj", true).get(0);
				dj.getName();
			} catch (IndexOutOfBoundsException e) {
				EmbedBuilder bd = new EmbedBuilder();
				bd.setColor(Color.MAGENTA);
				bd.setTitle("Não estou achando o cargo DJ.");
				bd.setDescription("O cargo DJ não existe, recorra a algum staff para criar esse cargo!");
				event.getTextChannel().sendMessage(bd.build()).queue();
				return;
			}
			if (!event.getMember().getRoles().contains(dj)) {
				EmbedBuilder bd = new EmbedBuilder();
				bd.setColor(Color.MAGENTA);
				bd.setTitle("Você não tem o cargo dj!");
				bd.setDescription("Hmm, você não tem permissão, porque, você nao tem o cargo DJ!");
				event.getTextChannel().sendMessage(bd.build()).queue();
				return;
			} else if (musicManager.player.getPlayingTrack() == null) {
				event.getTextChannel()
						.sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
								"Não tenho nenhuma música para pausar/despausar", prefix+"`pause`", Erros.ERRO).build())
						.queue();
				;
				return;
			}
			if (!musicManager.player.isPaused()) {
				musicManager.player.setPaused(true);
				event.getTextChannel().sendMessage(Utils.sucessEmbed(event.getAuthor(),
						"Musica pausada com sucesso!\n Dê `"+prefix+"pause` denovo para despausar!").build()).queue();
			} else {
				musicManager.player.setPaused(false);
				event.getTextChannel().sendMessage(Utils.sucessEmbed(event.getAuthor(),
						"Musica despausada com sucesso!\n Dê `"+prefix+"pause` denovo para pausar!").build()).queue();
			}

		} else if (args[0].equals(prefix+"volume")) {
			Role dj;
			try {
				dj = event.getGuild().getRolesByName("dj", true).get(0);
				dj.getName();
			} catch (IndexOutOfBoundsException e) {
				EmbedBuilder bd = new EmbedBuilder();
				bd.setColor(Color.MAGENTA);
				bd.setTitle("Não estou achando o cargo DJ.");
				bd.setDescription("O cargo DJ não existe, recorra a algum staff para criar esse cargo!");
				event.getTextChannel().sendMessage(bd.build()).queue();
				return;
			}
			if (!event.getMember().getRoles().contains(dj)) {
				EmbedBuilder bd = new EmbedBuilder();
				bd.setColor(Color.MAGENTA);
				bd.setTitle("Você não tem o cargo dj!");
				bd.setDescription("Hmm, você não tem permissão, porque, você nao tem o cargo DJ!");
				event.getTextChannel().sendMessage(bd.build()).queue();
				return;
			} else if (args.length == 1) {
				event.getChannel()
						.sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
								"Coloque um numero de `0 - 100`:\n "+prefix+"volume <Número>`", "`"+prefix+"volume`", Erros.SINTAXE)
								.build())
						.queue();
			}
			int volume;
			try {
				volume = Integer.parseInt(args[1]);

			} catch (NumberFormatException e) {
				event.getChannel()
						.sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
								"Por favor, digite um numero valido! `0 - 100`", "`"+prefix+"volume`", Erros.SINTAXE).build())
						.queue();
				return;
			}
			if (volume < 0 || volume > 100) {
				event.getChannel()
						.sendMessage(Utils.newEmbedSintaxe(event.getAuthor(),
								"Por favor, digite um numero valido! `0 - 100`", "`"+prefix+"volume`", Erros.SINTAXE).build())
						.queue();
				return;
			}
			PlayerManager playerManager = PlayerManager.getInstance();
			GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
			musicManager.player.setVolume(volume);
			event.getTextChannel()
					.sendMessage(Utils.sucessEmbed(event.getAuthor(), "Volume alterado com sucesso!").build()).queue();
		} else if (args[0].equals(prefix+"wpn?")) {
			PlayerManager playerManager = PlayerManager.getInstance();
			GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
			if (musicManager.player.getPlayingTrack() == null) {
				event.getTextChannel()
						.sendMessage(Utils.sucessEmbed(event.getAuthor(), "Não estou cantando nada.").build()).queue();
			} else {
				event.getTextChannel()
						.sendMessage(Utils
								.sucessEmbed(event.getAuthor(),
										"Estou cantando: " + musicManager.player.getPlayingTrack().getInfo().title)
								.build())
						.queue();
			}
		} else if (args[0].equals(prefix+"queue") || args[0].equals(prefix+"fila")) {
			TextChannel channel = event.getTextChannel();
			PlayerManager playerManager = PlayerManager.getInstance();
			GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
			BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

			if (queue.isEmpty()) {

				EmbedBuilder builder = new EmbedBuilder().setTitle("Minha fila está vazia!").setColor(Color.green);
				channel.sendMessage(builder.build()).queue();
				return;
			}

			int trackCount = Math.min(queue.size(), 20);
			List<AudioTrack> tracks = new ArrayList<>(queue);
			EmbedBuilder builder = new EmbedBuilder().setTitle("Minha playlist: (Total: " + queue.size() + ")");

			for (int i = 0; i < trackCount; i++) {
				AudioTrack track = tracks.get(i);
				AudioTrackInfo info = track.getInfo();

				builder.appendDescription(String.format("%s.%s - %s\n", i + 1, info.title, info.author));
			}

			channel.sendMessage(builder.build()).queue();

		} else if(args[0].equals(prefix + "mostrarDuracao")) {
			if(PlayerManager.getInstance().getGuildMusicManager(event.getGuild()).player.getPlayingTrack() == null) {
				channel2.sendMessage(Utils.newEmbedSintaxe(event.getAuthor(), "Não estou tocando nada", "'"+prefix+"mostrarDuracao'", Erros.ERRO).build());
			}
		}
		else if (args[0].equals(prefix+"aviso")) {
			if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
				event.getChannel().sendMessage("@everyone" + event.getMessage().getContentRaw().replace(prefix+"aviso","") + ".").queue();
				event.getMessage().delete().queue();
			}
			else {
				EmbedBuilder ebb = new EmbedBuilder();
				ebb.setTitle("Erro");
				ebb.setDescription("S� Administradores tem acesso a este comando");
				ebb.setColor(Color.red);
				event.getChannel().sendMessage(ebb.build()).queue();
			}
		}
	}

	private boolean isUrl(String input) {
		return input.startsWith("http://www.youtube.com");
	}

	@Nullable
	private String searchYoutube(String input) {
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
