package de.felix.mcDiscordStartAlert;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Mod(modid = mcDiscordStartAlertMod.MODID, name = mcDiscordStartAlertMod.NAME, version = mcDiscordStartAlertMod.VERSION, serverSideOnly = true)
public class mcDiscordStartAlertMod {
    public static final String MODID = "discordmcalert";
    public static final String NAME = "MC Discord Start Alert";
    public static final String VERSION = "1.0";

    private static Logger logger;
    private ConfigFileParser configFileParser;
    private String webhook;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @EventHandler
    public void started(FMLServerStartedEvent event) {
        File file = new File("config/mcDis.config");
        try {
            if (!file.exists()){
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write("webhook: https://discordapp.com/api/webhooks/id/token");
                writer.close();
            }
            configFileParser = new ConfigFileParser(new File("config/mcDis.config"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        webhook = configFileParser.getValues().get("webhook");
        try {
            sendMessage(webhook, "Server started!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @EventHandler
    public void stoped(FMLServerStoppingEvent event) throws IOException {
        sendMessage(webhook, "Stopping server...");
    }

    private void sendMessage(String webhookAPI, String message) throws IOException {
        URL url = new URL(webhookAPI);
        URLConnection con = url.openConnection();
        HttpsURLConnection http = (HttpsURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        String request = "content=" + message;
        byte[] out = request.getBytes(StandardCharsets.UTF_8);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        http.setRequestProperty("User-Agent", "Mozilla/4.76");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null){
            System.out.println(line);
        }
    }

}
