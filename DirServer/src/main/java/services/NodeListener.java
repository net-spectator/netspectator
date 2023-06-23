package services;

import entities.TrackedEquipment;
import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static enums.Status.OFFLINE;
import static enums.Status.ONLINE;

public class NodeListener {
    private static volatile List<TrackedEquipment> nodesList;
    private static volatile NodeListener nodeListener;
    private static long INIT_DELAY = 0; // TODO: 22.06.2023 перенести в clientParams
    private static long PERIOD = 5; // TODO: 22.06.2023 перенести в clientParams
    private static int PING_TIMEOUT = 5000; // TODO: 22.06.2023 перенести в clientParams
    private static final Logger LOGGER = Logger.getLogger(NodeListener.class);
    private ScheduledExecutorService executor;

    private NodeListener() {
        nodesList = new ArrayList<>();
        startListener();
    }

    public static synchronized NodeListener getNodeListener() {
        NodeListener nl = nodeListener;
        if (nl == null) {
            nl = nodeListener;
            if (nl == null) {
                nodeListener = nl = new NodeListener();
            }
        }
        return nl;
    }

    public void addNodeForTracking(TrackedEquipment trackedEquipment) {
        nodesList.add(trackedEquipment);
    }

    public static synchronized boolean checkNode(String ipAddress) {
        InetAddress inetAddress = null;
        try {
            // Получаем объект InetAddress для IP-адреса или доменного имени
            inetAddress = InetAddress.getByName(ipAddress);

            LOGGER.info("Sending ping request to " + ipAddress);
            // Отправляем запрос на пинг
            if (inetAddress.isReachable(PING_TIMEOUT)) { // Таймаут 5 секунд
                LOGGER.info(ipAddress + " is reachable" + " ip address: " + inetAddress);
                return true;
            } else {
                LOGGER.info(ipAddress + " is not reachable");
            }
        } catch (UnknownHostException e) {
            LOGGER.info("Invalid IP address or host name: " + ipAddress);
        } catch (Exception e) {
            LOGGER.info("Error occurred while pinging " + ipAddress + ": " + e.getMessage());
        }
        return false;
    }

    public static List<String> searchNodes(String ipRange) { // TODO: 22.06.2023 поискать готовые библиотеки
        return Collections.emptyList();
    }

    private void startListener() {
        executor.scheduleAtFixedRate(new CheckNodes(), INIT_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    private static class CheckNodes implements Runnable {
        @Override
        public void run() {
            nodesList.forEach(trackedEquipment -> {
                boolean isOnline = checkNode(trackedEquipment.getEquipmentIpAddress());
                if (!isOnline) {
                    DataBaseService.changeTrackedEquipmentStatus(trackedEquipment.getId(), OFFLINE);
                } else {
                    DataBaseService.changeTrackedEquipmentStatus(trackedEquipment.getId(), ONLINE);
                }
            });
        }
    }

}
