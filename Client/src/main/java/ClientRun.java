import services.Client;
import utils.ModuleName;

import java.io.IOException;

public class ClientRun {
    public static void main(String[] args) throws IOException {
        ModuleName.getModuleName().setName("Client");
        new Client();
    }
}
