package net.pomedge.events.modules;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.pomedge.main.Main;
import net.pomedge.utils.Opt;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Random;

public class WelcomeMessage extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        if((Boolean) Opt.getJsonElement(event.getGuild().getId()+"msgEntrada")){
            BufferedImage image = null;
            BufferedImage imageBuffer = new BufferedImage( 800, 400, BufferedImage.TYPE_INT_RGB );

            Graphics g = imageBuffer.createGraphics();
            g.fillRect( 0, 0, 800, 400 );
            g.setColor(Color.black);
            g.fillRect( 0, 200, 800, 200 );
            try {
                //cria URL
                URL url1 = new URL(event.getUser().getAvatarUrl());

                //abre uma conexao na url criada àcima
                URLConnection con =  url1.openConnection();
                con.addRequestProperty("User-Agent", "Mozilla");
                con.setReadTimeout(5000);
                con.setConnectTimeout(5000);
                //tenta conectar.
                con.connect();
                //arquivo de saida
                new File("foto"+event.getUser().getId()+".png").createNewFile();
                FileOutputStream fileOut = new FileOutputStream("foto"+event.getUser().getId()+".png");
                int c=0;
                do{
                    //le o byte
                    c=con.getInputStream().read();
                    //escreve o byte no arquivo saida
                    fileOut.write(c);
                }while(c !=-1);
                //fecha o arquivo de saida
                fileOut.close();
                System.out.println("Arquivo baixado com sucesso");
            }catch(IOException e){
                e.printStackTrace();
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif",Font.BOLD, 32));
            g.drawString(event.getUser().getName(),260, 320);
            try {

                image = ImageIO.read(new File("foto"+event.getUser().getId()+".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawImage(image, 100, 250,100,100, null);
            new File("/tmp/Bot/foto"+event.getUser().getId()+".png").delete();
            String tempFilePath= "";
            File tempFile = null;
            try {


                tempFilePath = event.getUser().getId()+".png";
                tempFile = new File(tempFilePath);
                tempFile.createNewFile();
                ImageIO.write(imageBuffer, "png", tempFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            event.getGuild().getTextChannelById((String)Opt.getJsonElement(event.getGuild().getId()+"msgEntradaChannel")).sendFile(tempFile).queue();
            tempFile.delete();
        }
    }
}
