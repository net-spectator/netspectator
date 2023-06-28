import services.Client;
import utils.ModuleName;

import java.io.IOException;

public class ClientRun {
    public static void main(String[] args) throws IOException {
        ModuleName mn = ModuleName.getModuleName();
        mn.setName("Client");
        new Client();
    }
}
