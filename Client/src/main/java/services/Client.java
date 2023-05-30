package services;


import config.IniFileOperator;
import config.Logo;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Client {
    private DataOutputStream out;
    private DataInputStream in;
    private ReadableByteChannel rbc;
    private ExecutorService threadManager;
    private final ByteBuffer byteBuffer = ByteBuffer.allocate(8 * 1024);
    private static HashMap<String, String> clientParams;
    private Socket socket;
    private String ADDRESS;
    private int PORT;
    private boolean isInteractive;
    private ScheduledExecutorService executor;
    private static final Logger LOGGER = Logger.getLogger(Client.class);

    public static String getClientParams(String param) {
        return clientParams.get(param);
    }

    public Client() {
        executor = Executors.newSingleThreadScheduledExecutor();
        paramsInit();
        while (true) {
            LOGGER.info(String.format("Попытка установить соединение с сервером [%s:%s]", ADDRESS, PORT));
            tryToConnect();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void paramsInit() {
        clientParams = IniFileOperator.initFileParams("client.ini");
        System.out.println(Logo.showLogo());
        assert clientParams != null;
        ADDRESS = clientParams.get("Address");
        LOGGER.info(String.format("Адрес сервера: [%s]", ADDRESS));
        PORT = Integer.parseInt(clientParams.get("Port"));
        LOGGER.info(String.format("Порт сервера: [%s]", PORT));
        isInteractive = clientParams.get("Interactive mode").equals("true");
        if (!isInteractive) {
            LOGGER.info("Активирован автоматический режим");
        } else {
            LOGGER.info("Активирован интерактивный режим");
        }
        clientParams.put("Client name", deviceName());
    }

    private int connect() {
        try {
            socket = new Socket(ADDRESS, PORT);
            if (socket.isConnected()) {
                out = new DataOutputStream(socket.getOutputStream());
                in = new DataInputStream(socket.getInputStream());
                rbc = Channels.newChannel(in);
                threadManager = Executors.newFixedThreadPool(5);
                LOGGER.info(String.format("Установлено соединение с сервером по адресу: [%s:%s]", ADDRESS, PORT));
                return 1;
            }
        } catch (IOException e) {
            LOGGER.info(String.format("Не удалось установить соединение с сервером по адресу [%s:%s]", ADDRESS, PORT));
            return -1;
        }
        return 0;
    }

    private void tryToConnect() {
        int connectionResult = connect();
        if (connectionResult > 0 && isInteractive) {
            interactiveMode();
        } else if (connectionResult > 0) {
            automaticModeInit();

        }
    }

    private void interactiveMode() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            StringBuilder command = new StringBuilder();
            out.write(("/hello").getBytes());
            System.out.print(queryStringListener());
            while (true) {
                command.append(reader.readLine());
                if (command.toString().equals("exit")) {
                    break;
                }
                out.write(("/" + command).getBytes());
                command.delete(0, command.length());
                command.append(queryStringListener());
                System.out.print(command);
                if (command.toString().contains("shutdown")) {
                    break;
                }
                command.delete(0, command.length());
            }
        } catch (IllegalArgumentException e) {
            LOGGER.error("Сервер разорвал соединение");
            tryToConnect();
        } catch (IOException e) {
            LOGGER.info("Соединение потеряно");
        }
    }

    private void automaticModeInit() {
        String[] query;
        boolean keepAlive = true;
        try {
            out.write(("\\auth " + clientParams.get("Public key")).getBytes());
            while (keepAlive) {
                query = queryStringListener().replace("\n", "").split(" ");
                switch (query[0]) {
                    case "getId":
                        out.write(("\\clientID " + clientParams.get("Client ID")).getBytes());
                        break;
                    case "newID":
                        clientParams.put("Client ID", query[1]);
                        IniFileOperator.writeFileParams(clientParams);
                        LOGGER.info(String.format("Клиенту присвоен новый ID: [%s]", query[1]));
                        break;
                    case "getName":
                        out.write(("\\clientName " + clientParams.get("Client name")).getBytes());
                        break;
                    case "startSensors":
                        executor.scheduleAtFixedRate(new DeviceListener(out), 0, 5, TimeUnit.SECONDS);
                        break;
                    case "close":
                        keepAlive = false;
                        break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Connection closed.");
        }
    }

    private String queryStringListener() {
        int readNumberBytes = 0;
        try {
            readNumberBytes = rbc.read(byteBuffer);
        } catch (SocketException e) {
            LOGGER.error("Connection closed");
            return "close";
        } catch (IOException e) {
            LOGGER.error("IO exception detected");
            return "close";
        }

        String queryAnswer = null;
        try {
            queryAnswer = new String(Arrays.copyOfRange(byteBuffer.array(), 0, readNumberBytes));
        } catch (Exception e) {
            queryAnswer = "close";
            LOGGER.error("Connection refused");
        }
        byteBuffer.clear();
        return queryAnswer;
    }

    private String deviceName() {
        String computerName = null;
        try {
            computerName = Inet4Address.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return computerName;
    }


}
