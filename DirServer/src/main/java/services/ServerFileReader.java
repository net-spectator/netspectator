package services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ServerFileReader {
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

}
