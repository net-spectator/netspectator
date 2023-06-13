package stringHandlers;

import entities.Connection;
import services.ClientListenersStarter;

public class Server {
    private final MessageSender messageSender;
    private final Connection client;

    public Server(MessageSender messageSender, Connection client) {
        this.messageSender = messageSender;
        this.client = client;
    }

    public void shutdown() {
        if (client.isAuth()) {
            messageSender.sendMessageWithoutHeader("Server shutdown");
            ClientListenersStarter.shutdownServer();
        } else {
            messageSender.sendMessageWithHeader("You are not authorized");
        }
    }
}
