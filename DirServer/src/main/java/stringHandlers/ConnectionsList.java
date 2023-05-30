package stringHandlers;

import entities.Connection;
import io.netty.channel.ChannelHandlerContext;
import services.NettyBootstrap;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionsList {
    private final MessageSender messageSender;
    private final BlackList blackList;
    private final Connection client;

    public ConnectionsList(MessageSender messageSender, BlackList blackList, Connection client) {
        this.messageSender = messageSender;
        this.blackList = blackList;
        this.client = client;
    }

    public boolean connectionListOperator(ChannelHandlerContext ctx, String[] args) {
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
            case "show":
                AtomicInteger finalIndex = new AtomicInteger(1);
                NettyBootstrap.connections.forEach(socketAddress -> response
                        .append(finalIndex.getAndIncrement())
                        .append(". ")
                        .append(socketAddress)
                        .append("\n"));
                messageSender.sendMessageWithHeader(response.toString());
                return true;

            case "close":
                int index = 0;
                try {
                    index = Integer.parseInt(args[2]);
                    NettyBootstrap.connections.get(index - 1)
                            .getChannelHandlerContext()
                            .disconnect();
                    NettyBootstrap.connections.remove(index - 1);
                } catch (NumberFormatException e) {
                    messageSender.sendMessageWithHeader("Wrong index format");
                    return false;
                } catch (IndexOutOfBoundsException e) {
                    messageSender.sendMessageWithHeader("wrong index");
                    return false;
                }
                messageSender.sendMessageWithHeader("Operation complete");
                return true;
            case "blacklist":
                return blackList.blackListOperator(Arrays.copyOfRange(args, 1, args.length));
            case "this":
                messageSender.sendMessageWithHeader(ctx.channel().remoteAddress().toString());
                return true;
            default:
                messageSender.sendMessageWithHeader("Bad command");
                break;
        }
        return false;
    }
}
