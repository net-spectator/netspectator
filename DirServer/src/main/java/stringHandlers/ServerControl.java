package stringHandlers;

import entities.Connection;
import services.ClientListenersStarter;

public class ServerControl {
    private final MessageSender messageSender;
    private final Connection client;

    public ServerControl(MessageSender messageSender, Connection client) {
        this.messageSender = messageSender;
        this.client = client;
    }

    /**
     * Завершает работу сервера клиентов
     * */
    public void shutdown() {
        if (client.isAuth()) {
            messageSender.sendMessageWithoutHeader("Server shutdown");
            ClientListenersStarter.shutdownServer();
        } else {
            messageSender.sendMessageWithHeader("You are not authorized");
        }
    }
}
