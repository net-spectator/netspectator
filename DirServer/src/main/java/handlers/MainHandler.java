package handlers;

import entities.Connection;
import services.ClientListenersDataBus;
import stringHandlers.*;
import services.ChanelListener;
import services.DataBaseService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import utils.MessageSender;

public class MainHandler extends ChannelInboundHandlerAdapter {
    private ChanelListener chanelListener;
    private Connection client;
    private DataBaseService dbService;
    private static final Logger LOGGER = Logger.getLogger(MainHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("New connection " + ctx.channel().localAddress());
        if (ClientListenersDataBus.getDisabledClient(ctx.channel().localAddress()) != null) {
            ctx.disconnect();
        }
        connectionInit(ctx);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        DataBaseService.changeDeviceStatus(client.getDevice().getEquipmentUuid(), false); // TODO: 02.07.2023 Тут кака то лажа с NULL pointer
        if (ClientListenersDataBus.disconnectClient(client)) {
            LOGGER.info(String.format("Client [%s] successfully removed", client.getDevice().getEquipmentTitle()));
            LOGGER.info(String.format("Client [%s] disconnected", client.getDevice().getEquipmentTitle()));
        }
        LOGGER.info(String.format("Error with [%s] disconnection", client.getDevice().getEquipmentTitle()));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        chanelListener.listen(ctx, msg);
    }

    private void connectionInit(ChannelHandlerContext ctx) {
        client = new Connection(ctx);
        MessageSender messageSender = new MessageSender(ctx, client);
        DisabledClientsControl blackList = new DisabledClientsControl(messageSender, client);
        NodesControl nodesControl = new NodesControl(messageSender, client);
        ServerControl server = new ServerControl(messageSender, client);
        ConnectionsList connectionsList = new ConnectionsList(messageSender, blackList, client);
        chanelListener = new ChanelListener(messageSender, // TODO: 02.07.2023 заменить логику добавления слушателей по примеру паттерна Chain
                connectionsList,
                blackList,
                client,
                server,
                nodesControl);
    }
}

