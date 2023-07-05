package services;


import config.Logo;
import org.apache.log4j.Logger;
import utils.ModuleName;
import utils.NSLogger;
import utils.converter.PropertiesOperator;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import java.util.Properties;
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
    private Socket socket;
    private String ADDRESS;
    private int PORT;
    private String MACAddress;
    private boolean isInteractive;
    private ScheduledExecutorService executor;
    private long INIT_DELAY;
    private long EXECUTION_PERIOD;
    private static String PROPERTIES_PATH = "client.properties";
    public static Properties properties;
    private static final NSLogger LOGGER = new NSLogger(Client.class);

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
        properties = PropertiesOperator.returnProperties(PROPERTIES_PATH);
        if (properties != null) {
            PORT = Integer.parseInt(properties.get("port").toString());
            LOGGER.info(String.format("Порт сервера: [%s]", PORT));
            LOGGER.info(String.format("Сервер прослушивает порт: [%s]", PORT));
            ADDRESS = properties.getProperty("address");
            LOGGER.info(String.format("Адрес сервера: [%s]", ADDRESS));
            isInteractive = properties.getProperty("interactive_mode").equals("true");
            INIT_DELAY = Long.parseLong(properties.getProperty("schedule_init_delay"));
            LOGGER.info(String.format("Отложенный запуск задания: [%s] ms", INIT_DELAY));
            EXECUTION_PERIOD = Long.parseLong(properties.getProperty("schedule_execution_period"));
            LOGGER.info(String.format("Периодичность запуска: [%s] ms", EXECUTION_PERIOD));
            System.out.println(Logo.showLogo());
            if (!isInteractive) {
                LOGGER.info("Активирован автоматический режим");
            } else {
                LOGGER.info("Активирован интерактивный режим");
            }
            properties.put("name", deviceName());
        }
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
                if (command.toString().equals("shutdown")) {
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
        ModuleName.getModuleName().setName(properties.getProperty("name"));
        try {
            out.write(("\\auth " + properties.getProperty("pub_key")).getBytes());
            while (keepAlive) {
                query = queryStringListener().replace("\n", "").split(" ");
                switch (query[0]) {
                    case "getId":
                        LOGGER.info("Сервер запрашивает ID");
                        out.write(("\\clientID " + properties.getProperty("client_id")).getBytes());
                        break;
                    case "newID":
                        properties.setProperty("client_id", query[1]);
                        PropertiesOperator.propertiesWriter(properties, PROPERTIES_PATH);
                        LOGGER.info(String.format("Клиенту присвоен новый ID: [%s]", query[1]));
                        break;
                    case "getName":
                        LOGGER.info("Сервер запрашивает имя");
                        out.write(("\\clientName " + properties.getProperty("name")).getBytes());
                        break;
                    case "getMac":
                        LOGGER.info("Сервер запрашивает MAC адрес");
                        MACAddress = getMacAddress();
                        LOGGER.info(String.format("MAC адресс текущего устройства - %s", MACAddress));
                        out.write(("\\macAddress " + MACAddress).getBytes());
                        break;
                    case "slog":
                        LOGGER.info(String.format("Логирование на сервер - %s", (query[1].equals("1") ? "включено" : "отключено")));
                        out.write(("\\getSensors").getBytes());
                        break;
                    case "startSensors":
                        executor.scheduleAtFixedRate(new DeviceListener(out, Arrays.copyOfRange(query, 1, query.length)), INIT_DELAY, EXECUTION_PERIOD, TimeUnit.SECONDS);
                        break;
                    case "close":
                        LOGGER.info("Запрос о перезагрузке клиента");
                        keepAlive = false;
                        break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Connection closed");
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
            LOGGER.error("Не удалось определить имя устройства", e);
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
            while (true) {  // TODO: 30.06.2023 Пересмотреть логику цикла
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
            LOGGER.error("Не удалось определить MAC адрес устройства", e);
        }
        return null;
    }
}
