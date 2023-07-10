package stringHandlers;

import entities.Connection;
import enums.Help;
import services.NodeListener;
import utils.MessageSender;

import java.text.ParseException;
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
        AtomicInteger finalIndex = new AtomicInteger(1);

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
                NodeListener.getDetectedNodes().forEach(detectedNode -> response
                        .append(finalIndex.getAndIncrement())
                        .append(". ")
                        .append(detectedNode)
                        .append("\n"));
                messageSender.sendMessageWithHeader(response.length() < 1 ? "empty" : response.toString());
                break;
            case "scan":
                try {
                    if (args.length > 2) {
                        NodeListener.nodeBroadcastSearch(args[2]);
                        messageSender.sendMessageWithHeader("Сканирование запущено");
                    } else {
                        messageSender.sendMessageWithHeader("Ошибка в аргументах");
                    }
                } catch (NumberFormatException e) {
                    messageSender.sendMessageWithHeader(String.format("Неверный формат диапазона сети: [%s]", args[2]));
                }
                break;
            case "add":
                try {
                    if (args.length == 3) {
                        NodeListener.addNodeForTracking(Integer.parseInt(args[2]) - 1);
                        messageSender.sendMessageWithHeader("Операция выполнена");
                    } else if (args.length == 5 && args[3].contains("-n")) {
                        NodeListener.addNodeForTracking(Integer.parseInt(args[2]) - 1, args[4]);
                        messageSender.sendMessageWithHeader("Операция выполнена");
                    } else {
                        messageSender.sendMessageWithHeader("Неверный набор аргументов");
                    }
                } catch (IndexOutOfBoundsException | NumberFormatException e) {
                    messageSender.sendMessageWithHeader(String.format("Неверный индекс, индекс должен быть в пределах от 1 до %s", NodeListener.detectedNodesListSize()));
                }
                break;
            case "tracked":
                NodeListener.getTrackedList().forEach(trackedEquipment -> response
                        .append(finalIndex.getAndIncrement())
                        .append(". ")
                        .append("Name = ")
                        .append(trackedEquipment.getEquipmentTitle())
                        .append(" ; ")
                        .append("IP = ")
                        .append(trackedEquipment.getEquipmentIpAddress())
                        .append(" ; ")
                        .append("MAC = ")
                        .append(trackedEquipment.getEquipmentMacAddress())
                        .append("\n"));
                messageSender.sendMessageWithHeader(response.length() < 1 ? "empty" : response.toString());
                break;
            case "remove":
                try {
                    if (args.length == 3) {
                        NodeListener.removeNodeFromTracking(Integer.parseInt(args[2]) - 1);
                        messageSender.sendMessageWithHeader("Операция выполнена");
                    }
                } catch (NumberFormatException e) {
                    messageSender.sendMessageWithHeader(String.format("Неверный индекс, индекс должен быть в пределах от 1 до %s", NodeListener.trackedNodesListSize()));
                }
                messageSender.sendMessageWithHeader("Команда не выполнена, проверьте аргументы");
                break;
            case "tracking":
                if (args.length == 3 && args[2].equals("stop")) {
                    if (NodeListener.stopListener()) {
                        messageSender.sendMessageWithHeader("Операция выполнена");
                    } else {
                        messageSender.sendMessageWithHeader("Сервис уже остановлен");
                    }
                }
                if (args.length == 3 && args[2].equals("start")) {
                    if (NodeListener.startListener()) {
                        messageSender.sendMessageWithHeader("Операция выполнена");
                    } else {
                        messageSender.sendMessageWithHeader("Сервис уже запущен");
                    }
                }
                messageSender.sendMessageWithHeader("Команда не выполнена, проверьте аргументы");
                break;
            case "?":
                messageSender.sendMessageWithHeader(Help.getCNodesHelp());
                break;
            default:
                messageSender.sendMessageWithHeader("Bad command");
                break;
        }
        return false;
    }
}
