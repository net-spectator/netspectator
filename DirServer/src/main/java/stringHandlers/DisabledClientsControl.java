package stringHandlers;

import entities.Connection;
import enums.Help;
import services.ClientListenersDataBus;
import utils.MessageSender;

import java.util.concurrent.atomic.AtomicInteger;

public class DisabledClientsControl {
    private final MessageSender messageSender;
    private final Connection client;
    public DisabledClientsControl(MessageSender messageSender, Connection client) {
        this.messageSender = messageSender;
        this.client = client;
    }

    public boolean disabledClientsListOperator(String[] args) {
        StringBuilder response = new StringBuilder();
        int index = 0;

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
                AtomicInteger finalIndex = new AtomicInteger(1);
                ClientListenersDataBus.getDisabledClientsList().forEach(socketAddress -> response
                        .append(finalIndex.getAndIncrement())
                        .append(". ")
                        .append(socketAddress)
                        .append("\n"));
                messageSender.sendMessageWithHeader((response.length() < 1 ? "empty" : response.toString()));
                return true;
            case "remove":
                try {
                    index = Integer.parseInt(args[2]);
                    ClientListenersDataBus.enableClient(index - 1);
                } catch (NumberFormatException e) {
                    messageSender.sendMessageWithHeader("Wrong index format");
                    return false;
                } catch (IndexOutOfBoundsException e) {
                    messageSender.sendMessageWithHeader(ClientListenersDataBus.getDisabledClientsList().size() == 0 ? "Empty blacklist" : "wrong index");
                    return false;
                }
                messageSender.sendMessageWithHeader("Operation complete");
                return true;
            case "add":
                try {
                    index = Integer.parseInt(args[2]);
                    ClientListenersDataBus.disableClient((ClientListenersDataBus.getConnection(index - 1))
                            .getChannelHandlerContext()
                            .channel()
                            .localAddress());
                    ClientListenersDataBus.disconnectClient(index-1);
                } catch (NumberFormatException e) {
                    messageSender.sendMessageWithHeader("Wrong index format");
                    return false;
                } catch (IndexOutOfBoundsException e) {
                    messageSender.sendMessageWithHeader(ClientListenersDataBus.getDisabledClientsList().size() == 0 ? "Empty blacklist" : "wrong index");
                    return false;
                }
                messageSender.sendMessageWithHeader("Operation complete");
                return true;
            case "?":
                messageSender.sendMessageWithHeader(Help.getDisconnectedHelp());
                break;
            default:
                messageSender.sendMessageWithHeader("Bad command");
                break;
        }
        return false;
    }
}
