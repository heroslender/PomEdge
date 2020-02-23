package net.pomedge.main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.security.auth.login.LoginException;

import net.pomedge.commands.Commands;
import net.pomedge.events.modules.WelcomeMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

@SuppressWarnings("all")
public class Main {
    public static JSONArray bannedUsers;
    public static JDA jda;
    public static JSONParser jp = new JSONParser();
    public static JSONObject jsonReader;

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
        jdabuilder.addEventListeners(new WelcomeMessage());
        bannedUsers = (JSONArray) jsonReader.get("bannedUsers");

        jdabuilder.setToken((String) jsonReader.get("token"));
        try {
            jda = jdabuilder.build();
            jda.awaitReady();

        } catch (LoginException e) {
            System.out.println(
                    "ERRO AO LOGAR! \n SoluÃ§Ãµes comuns: \n Verifique sua conexÃ£o com a internet \n Verifique o token");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



}