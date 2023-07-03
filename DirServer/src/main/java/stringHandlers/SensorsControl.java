package stringHandlers;

import entities.Connection;
import utils.MessageSender;

public class SensorsControl {
    private final MessageSender messageSender;

    private final Connection client;

    public SensorsControl(MessageSender messageSender, Connection client) {
        this.messageSender = messageSender;
        this.client = client;
    }

    public boolean sensorsOperator(String[] args) {

        if (!client.isAuth()) {
            messageSender.sendMessageWithHeader("Not authorized");
            return false;
        }

        if (args.length < 2) {
            messageSender.sendMessageWithHeader("wrong args");
            return false;
        }

        switch (args[1]) {
            case "show":
            
            case "remove":

            case "add":

            default:
                messageSender.sendMessageWithHeader("Bad command");
                break;
        }
        return false;
    }
}
