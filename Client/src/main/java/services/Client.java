package services;


import config.IniFileOperator;
import config.Logo;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


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
    private static long INIT_DELAY = 0; // TODO: 22.06.2023 перенести в clientParams
    private static long PERIOD = 5; // TODO: 22.06.2023 перенести в clientParams
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
                        executor.scheduleAtFixedRate(new DeviceListener(out, Arrays.copyOfRange(query, 1, query.length)), INIT_DELAY, PERIOD, TimeUnit.SECONDS);
                        break;
                    case "getMac":
                        out.write(("\\macAddress " + getMacAddress()).getBytes());
                        break;
                    case "close":
                        keepAlive = false;
                        break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Connection closed.");
            executor.shutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public String getMacAddress() throws Exception {
        try {
            String macAddress = null;
            String command = "ifconfig";

            String osName = System.getProperty("os.name");

            if (osName.startsWith("Windows")) {
                command = "ipconfig /all";
            } else if (osName.startsWith("Linux") || osName.startsWith("Mac") || osName.startsWith("HP-UX")
                    || osName.startsWith("NeXTStep") || osName.startsWith("Solaris") || osName.startsWith("SunOS")
                    || osName.startsWith("FreeBSD") || osName.startsWith("NetBSD")) {
                command = "ifconfig -a";
            } else if (osName.startsWith("OpenBSD")) {
                command = "netstat -in";
            } else if (osName.startsWith("IRIX") || osName.startsWith("AIX") || osName.startsWith("Tru64")) {
                command = "netstat -ia";
            } else if (osName.startsWith("Caldera") || osName.startsWith("UnixWare") || osName.startsWith("OpenUNIX")) {
                command = "ndstat";
            } else {// Note: Unsupported system.
                throw new Exception("The current operating system '" + osName + "' is not supported.");
            }

            Process pid = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pid.getInputStream()));
            Pattern p = Pattern.compile("([\\w]{1,2}(-|:)){5}[\\w]{1,2}");
            while (true) {
                String line = in.readLine();
                if (line == null)
                    break;

                Matcher m = p.matcher(line);
                if (m.find()) {
                    macAddress = m.group();
                    break;
                }
            }
            in.close();
            return macAddress;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
