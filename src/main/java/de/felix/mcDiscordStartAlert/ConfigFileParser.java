package de.felix.mcDiscordStartAlert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ConfigFileParser {
    private HashMap<String, String> values;
    ConfigFileParser(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        values = new HashMap<>();
        while ((line = reader.readLine()) != null){
            if (line.contains(": ")){
                String setting = line.split(": ")[0];
                String value = line.split(": ")[1];
                values.put(setting, value);
            }
        }
    }

    public HashMap<String, String> getValues() {
        return values;
    }
}
