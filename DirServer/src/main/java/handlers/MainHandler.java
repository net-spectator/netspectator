package handlers;

import entities.Connection;
import services.ClientListenersDataBus;
import stringHandlers.*;
import services.ChanelListener;
import services.DataBaseService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

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
        dbService.changeDeviceStatus(client.getDevice().getEquipmentUuid(), false);
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
        dbService = new DataBaseService();
        MessageSender messageSender = new MessageSender(ctx, client);
        DisabledClientsControl blackList = new DisabledClientsControl(messageSender, client);
        ServerControl server = new ServerControl(messageSender, client);
        ConnectionsList connectionsList = new ConnectionsList(messageSender, blackList, client);
        chanelListener = new ChanelListener(messageSender, connectionsList, blackList, client, server, dbService);
    }
}

