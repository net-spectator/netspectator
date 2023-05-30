package config;

import services.Client;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class IniFileOperator {
    public static HashMap<String, String> initFileParams(String fileName) {
        HashMap<String, String> params = new HashMap<>();
        File iniFile = new File(fileName);
        StringBuilder string = new StringBuilder();
        if (iniFile.exists() && iniFile.length() > 0) {
            try (FileInputStream fis = new FileInputStream(iniFile);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8))) {
                while (reader.ready()) {
                    string.append(reader.readLine());
                    String[] iniParam = string.toString().split(":");
                    params.put(iniParam[0], iniParam.length == 2 ? iniParam[1].replace(" ", "") : "");
                    string.delete(0, string.length());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return params.size() > 0 ? params : null;
    }

    public synchronized static void writeFileParams(HashMap<String, String> params) {
        File file = new File("client.ini");
        try (FileOutputStream fis = new FileOutputStream(file);
             BufferedWriter writer = new BufferedWriter((new OutputStreamWriter(fis, StandardCharsets.UTF_8)));) {
            for (Map.Entry<String, String> data : params.entrySet()) {
                writer.write(data.getKey() + ": " + data.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
