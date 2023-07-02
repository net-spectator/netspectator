package stringHandlers;

import entities.Connection;
import services.NodeListener;
import utils.MessageSender;

import java.util.concurrent.atomic.AtomicInteger;

public class NodesControl {
    private final MessageSender messageSender;
    private final Connection client;

    public NodesControl(MessageSender messageSender, Connection client) {
        this.messageSender = messageSender;
        this.client = client;
    }

    public boolean nodesOperator(String[] args) {
        StringBuilder response = new StringBuilder();

        if (!client.isAuth()) {
            messageSender.sendMessageWithHeader("Not authorized");
            return false;
        }

        if (args.length < 2) {
            messageSender.sendMessageWithHeader("wrong args");
            return false;
        }

        switch (args[1]) {
            case "detected":
                AtomicInteger finalIndex = new AtomicInteger(1);
                NodeListener.getDetectedNodes().forEach(detectedNode -> response
                        .append(finalIndex.getAndIncrement())
                        .append(". ")
                        .append(detectedNode)
                        .append("\n"));
                messageSender.sendMessageWithHeader(response.length() < 1 ? "empty" : response.toString());
                break;
            case "scan":
                if (args.length > 2) {
                    NodeListener.nodeBroadcastSearch(args[2]);
                    messageSender.sendMessageWithHeader("Сканирование запущено");
                } else {
                    messageSender.sendMessageWithHeader("Ошибка в аргументах");
                }
                break;
            case "add":
                if (args.length>2){
//                    NodeListener.
                }
                break;
            default:
                messageSender.sendMessageWithHeader("Bad command");
                break;
        }
        return false;
    }
}
