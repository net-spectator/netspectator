package services;

import entities.EquipmentType;
import entities.nodes.DetectedNode;
import entities.TrackedEquipment;
import utils.NSLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.*;

import static enums.Status.OFFLINE;
import static enums.Status.ONLINE;

public class NodeListener {
    private static volatile List<TrackedEquipment> trackedList;
    private static volatile NodeListener nodeListener;
    private static long INIT_DELAY = 0; // TODO: 22.06.2023 перенести в clientParams
    private static long PERIOD = 5; // TODO: 22.06.2023 перенести в clientParams
    private static int PING_TIMEOUT = 200; // TODO: 22.06.2023 перенести в clientParams
    private static ScheduledExecutorService scheduledExecutorService;
    private static volatile List<DetectedNode> detectedNodes;
    private static EquipmentType equipmentType;
    private static NSLogger LOGGER = new NSLogger(NodeListener.class);
    private static String ARP_COMMAND;
    private static boolean tracking;
    private static ExecutorService pool = Executors.newFixedThreadPool(100);

    private NodeListener() {
        if (System.getProperty("os.name").contains("Windows")) {
            ARP_COMMAND = "arp -a";
        } else {
            ARP_COMMAND = "arp ";
        }
        trackedList = new ArrayList<>();
        detectedNodes = new ArrayList<>();
        equipmentType = DataBaseService.getEquipmentTypeElement("Nodes");
        startListener();
    }

    public static void startNodeListener() {
        if (nodeListener == null) {
            nodeListener = new NodeListener();
        }
    }

    //возвращает обнаруженные узлы после начала сканирования сети
    public static List<DetectedNode> getDetectedNodes() {
        return Collections.unmodifiableList(detectedNodes);
    }

    //получать список отслеживаемых устройств
    public static List<TrackedEquipment> getTrackedList() {
        return Collections.unmodifiableList(trackedList);
    }

    public static int detectedNodesListSize() {
        return detectedNodes.size();
    }

    public static int trackedNodesListSize() {
        return trackedList.size();
    }

    //добавляет обнаруженный узел на отслеживание
    public static synchronized void addNodeForTracking(int index) throws IndexOutOfBoundsException {
        DetectedNode dn = detectedNodes.get(index);
        TrackedEquipment detectedEquipment = new TrackedEquipment();
        detectedEquipment.setEquipmentTitle(dn.getNodeName());
        detectedEquipment.setEquipmentMacAddress(dn.getMACAddress());
        detectedEquipment.setEquipmentIpAddress(dn.getIpAddress());
        TrackedEquipment foundEquipment = DataBaseService.getTrackedNodeByIP(dn.getIpAddress());
        if (foundEquipment == null) {
            detectedEquipment.setEquipmentOnlineStatus(ONLINE.getStatus());
            detectedEquipment.setTypeId(equipmentType);
            DataBaseService.addTrackedEquipment(detectedEquipment);
            addTrackedNode(detectedEquipment);
        } else if (!trackedList.contains(foundEquipment)) {
            foundEquipment.setEquipmentOnlineStatus(ONLINE.getStatus());
            DataBaseService.updateTrackedEquipment(foundEquipment);
            addTrackedNode(foundEquipment);
        }
    }

    //добавляет обнаруженный узел с изменением его имени
    public static synchronized void addNodeForTracking(int index, String name) throws IndexOutOfBoundsException {
        DetectedNode dn = detectedNodes.get(index);
        dn.setNodeName(name);
        addNodeForTracking(index);
    }

    //удаление из отслеживания по индексу
    public static void removeNodeFromTracking(int index) {
        removeTrackEquipment(trackedList.get(index));
    }

    public static void removeNodeFromTrackingById(int id) {
        removeTrackEquipment(trackedList.stream().filter(trackedEquipment -> trackedEquipment.getId() == id)
                .findFirst()
                .orElse(null));
    }

    private static synchronized void removeTrackEquipment(TrackedEquipment trackedEquipment) {
        DataBaseService.removeTrackedEquipment(trackedEquipment);
        trackedList.remove(trackedEquipment);
    }

    //пингует узел и возвращает его состояние доступности
    public static boolean checkNode(String ipAddress) {
        int reached = 0;
        try {
            Process process = Runtime.getRuntime().exec("ping " + ipAddress);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int count = 3;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("ttl") && count != 0) {
                    reached++;
                } else if (count == 0) {
                    break;
                }
                count--;
            }
            reader.close();
        } catch (IOException e) {
            LOGGER.error(String.format("При проверке узла [%s] возникла ошибка", ipAddress), e);
        }
        return reached > 1;
    }

    private static synchronized void addSearchedNode(DetectedNode node) {
        detectedNodes.add(node);
    }

    private static synchronized void addTrackedNode(TrackedEquipment trackedEquipment) {
        trackedList.add(trackedEquipment);
    }

    private static String getRemoteMacAddress(String ipAddress) {
        StringBuilder MAC = null;
        try {
            Process process = Runtime.getRuntime().exec(ARP_COMMAND + ipAddress);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            MAC = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.contains(ipAddress)) {
                    String[] parts = line.split("\\s+");
                    if (parts.length >= 4) {
                        for (String s : parts) {
                            if (s.contains("-") || s.contains(":")) {
                                MAC.append(s);
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error(String.format("При получении MAC по ip адресу [%s] возникла ошибка", ipAddress), e);
            return "";
        }
        return MAC.toString();
    }

    private static String getRemoteNodeHostName(String ipAddress) {
        StringBuilder name = new StringBuilder();
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getByName(ipAddress);
            name.append(inetAddress.getCanonicalHostName());

        } catch (UnknownHostException e) {
            LOGGER.error(String.format("Не удалось определить hostname по адресу[%s]", ipAddress));
            return "";
        }
        return name.toString();
    }

    //сканирует сеть на доступные узлы 192.168.1.0
    public static void nodeBroadcastSearch(String ipRange) throws NumberFormatException { // TODO: 02.07.2023 в дальнейшем добавить калькулятор маски подсети
        detectedNodes.clear();
        String[] addressArray = ipRange.split("\\.");
        ipFormatCheck(addressArray);
        StringBuilder addressPrefix = new StringBuilder();
        addressPrefix.append(addressArray[0])
                .append(".")
                .append(addressArray[1])
                .append(".")
                .append(addressArray[2]);
        for (int i = 1; i < 255; i++) {
            pool.submit(new NodeSearcher(addressPrefix + "." + i));
        }
    }

    public static boolean startListener() {
        if (!tracking) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            trackedList = DataBaseService.getTrackedNodesListByType("Nodes");
            scheduledExecutorService.scheduleAtFixedRate(new NodeSpectator(), INIT_DELAY, PERIOD, TimeUnit.SECONDS);
            tracking = true;
            return true;
        } else {
            return false;
        }
    }

    public static boolean stopListener() {
        if (tracking) {
            scheduledExecutorService.shutdown();
            tracking = false;
            return true;
        } else {
            return false;
        }
    }

    private static void ipFormatCheck(String[] address) throws NumberFormatException {
        final int[] count = {0};
        Arrays.stream(address).forEach(s -> {
            if (ipOctetCheck(Integer.parseInt(s))) {
                count[0]++;
            }
        });
        if (count[0] < 3) {
            throw new NumberFormatException();
        }
    }

    private static boolean ipOctetCheck(int octet) {
        return octet < 255 && octet > 0;
    }

    private static class NodeSpectator implements Runnable {
        @Override
        public void run() {
            if (trackedList.size() > 0) {
                trackedList.forEach(trackedEquipment -> {
                    boolean isOnline = checkNode(trackedEquipment.getEquipmentIpAddress());
                    if (Objects.equals(trackedEquipment.getEquipmentOnlineStatus(), ONLINE.getStatus()) && !isOnline) {
                        trackedEquipment.setEquipmentOnlineStatus(OFFLINE.getStatus());
                        DataBaseService.updateTrackedEquipment(trackedEquipment);
                    }
                    if (Objects.equals(trackedEquipment.getEquipmentOnlineStatus(), OFFLINE.getStatus()) && isOnline) {
                        trackedEquipment.setEquipmentOnlineStatus(ONLINE.getStatus());
                        DataBaseService.updateTrackedEquipment(trackedEquipment);
                    }

                });
            }
        }
    }

    private static class NodeSearcher implements Runnable {
        private final String ipAddress;

        public NodeSearcher(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        @Override
        public void run() {
            boolean reached = checkNode(ipAddress);
            if (reached) {
                String MAC = getRemoteMacAddress(ipAddress);
                String name = getRemoteNodeHostName(ipAddress);
                DetectedNode node = new DetectedNode();
                node.setNodeName(name);
                node.setIpAddress(ipAddress);
                node.setMACAddress(MAC);
                addSearchedNode(node);
            }
        }
    }
}
