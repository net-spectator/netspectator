package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Connection;
import entities.TrackedEquipment;
import entities.devices.ClientHardwareInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import stringHandlers.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static enums.Status.OFFLINE;
import static enums.Status.ONLINE;

@RequiredArgsConstructor
public class ChanelListener {
    private String uuid;
    private final MessageSender messageSender;
    private final ConnectionsList connections;
    private final DisabledClientsControl blackList;
    private final Connection connection;
    private final ServerControl server;
    private final DataBaseService dbService;
    private static final Logger LOGGER = Logger.getLogger(ChanelListener.class);

    public void listen(ChannelHandlerContext ctx, Object msg) throws IOException {
        ByteBuf byteBuf = (ByteBuf) msg;
        String request = byteBuf.toString(StandardCharsets.UTF_8);
        String[] header = request
                .replace("\n", "")
                .replace("\r", "")
                .split(" ", 0);

        if (header[0].toLowerCase().contains("ssh")) {
            ctx.disconnect();
        }

        if (!header[0].equals("\\ClientHardwareInfo")) {
            LOGGER.info(String.format("Received header with content: [%s]", request));
        }

        /*  \\ - автоматический режим работы агента, / - интерактивный режим работы агента */
        switch (header[0]) {
            //---------------------------------------------------------------------------приветствие
            case "/hello":
                messageSender.sendMessageWithHeader("Welcome to Net Spectator server. ");
                break;
            //---------------------------------------------------------------------------авторизация в режиме консоли
            case "/auth":
                if (header.length > 1 && header[1].equals(ClientListenersStarter.getProperties(("admin")))) {
                    connection.setAuth(true);
                    messageSender.sendMessageWithHeader("Authorization ok");
                    TrackedEquipment device = new TrackedEquipment();
                    device.setEquipmentTitle("Admin");
                    connection.setDevice(device);
                } else {
                    messageSender.sendMessageWithHeader("Wrong key");
                }
                break;
            //-----------------------------------------------------------------------авторизация в автоматическом режиме
            case "\\auth":
                if (header.length > 1 && header[1].equals(ClientListenersStarter.getProperties("publicKey"))) {
                    connection.setAuth(true);
                    messageSender.sendMessageWithoutHeader("getId");
                } else {
                    ClientListenersDataBus.disableClient(ctx.channel().localAddress());
                    ctx.disconnect();
                }
                break;
            //---------------------------------------------------------------------------получение ClientID
            case "\\clientID":
                if (header.length < 2) { //не забыть проверить уникальность ID по базе
                    LOGGER.info("Подключается новое устройство. Присваиваю новый ID");
                    uuid = UUID.randomUUID().toString();
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(("newID " + uuid).getBytes()));
                    LOGGER.info(String.format("Новому клиенту присвоен ID: [%s]", uuid));
                } else {
                    uuid = header[1];
                }
                ClientListenersDataBus.addConnection(connection);
                messageSender.sendMessageWithoutHeader("getName");
                break;
            //---------------------------------------------------------------------------получение имени клиента
            case "\\clientName":
                LOGGER.info(String.format("Имя клиента: [%s]", header[1]));
                deviceInit(ctx, header);
                messageSender.sendMessageWithoutHeader("getMac");
                break;
            //------------------------------------------------------------------------включение сенсоров клиента
            case "\\getSensors":
                StringBuilder sensors = new StringBuilder();
                connection.getDevice()
                        .getTrackedEquipmentSensorsList()
                        .forEach(trackedEquipmentSensors -> sensors.append(" ") // TODO: 23.06.2023 при первом подключении клиента выскакивает nullPointer и соединение обрывается
                                .append(trackedEquipmentSensors
                                        .getSensors()
                                        .getSensorTitle()));
                messageSender.sendMessageWithoutHeader("startSensors" + sensors);
                break;
            //-------------------------------------------------------получение MAC-адреса/отправка настроек логирования
            case "\\macAddress":
                LOGGER.info(String.format("MAC клиента: [%s]", header[1]));
                connection.getDevice().setEquipmentMacAddress(header[1]);
                dbService.updateTrackedEquipment(connection.getDevice());
                messageSender.sendMessageWithoutHeader("slog " + (connection.getDevice().getServerLog()));
                break;
            //---------------------------------------------------------------------------выключение сервера
            case "/shutdown":
                server.shutdown();
                break;
            //---------------------------------------------------------------------------управление подключениями
            case "/connections":
                if (!connections.connectionListOperator(ctx, header)) {
                    LOGGER.info("Bad command");
                }
                break;
            //---------------------------------------------------------------------------управление черным списком
            case "/blacklist":
                if (!blackList.disabledClientsListOperator(header)) {
                    LOGGER.info("Bad command");
                }
                break;
            //---------------------------------------------------------------------------получение состояния клиента
            case "\\ClientHardwareInfo":
                ObjectMapper mapper = new ObjectMapper();
                ClientHardwareInfo deviceInfo = mapper.readValue(request.substring(20), ClientHardwareInfo.class);
                connection.getDevice().setDeviceInfo(deviceInfo);
            default:
                messageSender.sendMessageWithHeader("Unknown command");
                break;
        }
    }

    private void deviceInit(ChannelHandlerContext ctx, String[] args) {
        TrackedEquipment device = dbService.getTrackedEquipmentByUUID(uuid);  // TODO: 19.06.2023 проверка уникальности клиента должна осуществляться по ip, mac и UUID
        if (device == null) {
            device = new TrackedEquipment();
            device.setEquipmentTitle(args[1]);
            device.setEquipmentUuid(uuid);
            LOGGER.info(dbService.addTrackedEquipment(device) > 0 ? "Клиент успешно добавлен в базу" : "Ошибка добавления клиента в базу");
        }
        device.setEquipmentOnlineStatus(ONLINE.getStatus());
        device.setEquipmentIpAddress(ctx.channel().localAddress()
                .toString()
                .replace("/", ""));
        dbService.updateTrackedEquipment(device);
        connection.setDevice(device);
    }
}
