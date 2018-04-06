package de.felix.mcDiscordStartAlert;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = mcDiscordStartAlertMod.MODID, name = mcDiscordStartAlertMod.NAME, version = mcDiscordStartAlertMod.VERSION)
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
        sendMessage(webhook, "Server stopping...");
    }

    private void sendMessage(String webhookAPI, String message) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(webhookAPI);

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("content", message));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
    }

}
