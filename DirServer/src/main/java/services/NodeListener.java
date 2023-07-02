package services;

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
    private ScheduledExecutorService executor;
    private static volatile List<DetectedNode> detectedNodes;
    private static NSLogger LOGGER = new NSLogger(NodeListener.class);
    private static String ARP_COMMAND;

    private static ExecutorService pool = Executors.newFixedThreadPool(100);

    private NodeListener() {
        if (System.getProperty("os.name").contains("Windows")) {
            ARP_COMMAND = "arp -a";
        } else {
            ARP_COMMAND = "arp ";
        }
        trackedList = new ArrayList<>();
        detectedNodes = new ArrayList<>();
        executor = Executors.newSingleThreadScheduledExecutor();
        startListener();
    }

    public static NodeListener getNodeListener() {
        NodeListener nl = nodeListener;
        if (nl == null) {
            synchronized (NodeListener.class) {
                nl = nodeListener;
                if (nl == null) {
                    nodeListener = nl = new NodeListener();
                }
            }
        }
        return nl;
    }

    public static List<DetectedNode> getDetectedNodes() {
        return Collections.unmodifiableList(detectedNodes);
    }

    public static int detectedNodesListSize() {
        return detectedNodes.size();
    }

    public static synchronized void addNodeForTracking(int index) throws IndexOutOfBoundsException {
        DetectedNode dn = detectedNodes.get(index);
        TrackedEquipment detectedEquipment = new TrackedEquipment();
        detectedEquipment.setEquipmentTitle(dn.getNodeName());
        detectedEquipment.setEquipmentMacAddress(dn.getMACAddress());
        detectedEquipment.setEquipmentIpAddress(dn.getIpAddress());
        TrackedEquipment foundEquipment = DataBaseService.getTrackedNodeByIP(dn.getIpAddress());
        if (foundEquipment == null) {
            detectedEquipment.setEquipmentOnlineStatus(ONLINE.getStatus());
            DataBaseService.addTrackedEquipment(detectedEquipment);
            addTrackedNode(detectedEquipment);
        } else if (!trackedList.contains(foundEquipment)) {
            foundEquipment.setEquipmentOnlineStatus(ONLINE.getStatus());
            DataBaseService.updateTrackedEquipment(foundEquipment);
            addTrackedNode(foundEquipment);
        }
    }

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

    private static synchronized void addTrackedNode(TrackedEquipment trackedEquipment){
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
                            }
                        }
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

    public static void nodeBroadcastSearch(String ipRange) { // TODO: 02.07.2023 в дальнейшем добавить калькулятор маски подсети
        detectedNodes.clear();
        String[] addressArray = ipRange.split("\\.");
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

    private void startListener() { // TODO: 02.07.2023 добавить логику чтения отслеживаемых узлов из бд
        executor.scheduleAtFixedRate(new CheckNodes(), INIT_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    private static class CheckNodes implements Runnable {
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
