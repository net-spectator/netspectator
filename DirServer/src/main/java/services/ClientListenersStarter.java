package services;

import handlers.ByteBufEncoder;
import handlers.MainHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;

import java.util.HashMap;

public final class ClientListenersStarter {
    private static EventLoopGroup auth;
    private static EventLoopGroup worker;
    private static int PORT;
    public static HashMap<String, String> serverParams;
    private static final Logger LOGGER = Logger.getLogger(ClientListenersStarter.class);

    public ClientListenersStarter() {
        ClientListenersDataBus.getNettyDataBus();
        initParams();
        serverStart();
    }

    private static void serverStart(){
        auth = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap.group(auth, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(
                                    new ByteBufEncoder(),
                                    new MainHandler());
                            channel.config().setRecvByteBufAllocator(new FixedRecvByteBufAllocator(8096));
                        }
                    });
            ChannelFuture future = bootstrap.bind(PORT).sync();
            LOGGER.info("Server start");
            future.channel().closeFuture().sync();
            System.out.println("Server finished");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            auth.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    private static void initParams(){
        serverParams = ServerFileReader.initFileParams("server.ini");
        assert serverParams != null;
        PORT = Integer.parseInt(serverParams.get("Port"));
        LOGGER.info(String.format("Сервер прослушивает порт: [%s]", PORT));
    }

    public static void shutdownServer() {
        auth.shutdownGracefully();
        worker.shutdownGracefully();
    }
}

// TODO: 22.06.2023 подкорректировать базу данных
/**
 * router
 * printer
 * server
 * workstation
 * */