package services;

import entities.Connection;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ClientListenersDataBus {
    private static volatile ClientListenersDataBus nettyDataBus;
    private String data = "data"; //удалить
    private static volatile List<Connection> connections;
    private static volatile List<SocketAddress> disabledClients;

    public String getData() {
        return data;
    }

    private ClientListenersDataBus() {
        connections = new ArrayList<>();
        disabledClients = new ArrayList<>();
    }

    public static synchronized ClientListenersDataBus getNettyDataBus() {
        ClientListenersDataBus nb = nettyDataBus;
        if (nb == null) {
            nb = nettyDataBus;
            if (nb == null) {
                nettyDataBus = nb = new ClientListenersDataBus();
            }
        }
        return nb;
    }

    public static synchronized void addConnection(Connection connection) {
        connections.add(connection);
    }

    public static List<Connection> getConnectionsList() {
        return Collections.unmodifiableList(connections);
    }

    public static synchronized Connection getConnection(String UUID) {
        Optional<Connection> connectionElement = connections
                .stream()
                .filter(connection -> connection.getDevice().getUUID().equals(UUID))
                .findFirst();
        return connectionElement.orElse(null);
    }

    public static synchronized Connection getConnection(Integer index) {
        return connections.get(index);
    }

    public static synchronized boolean removeConnection(Connection connection) {
        return connections.remove(connection);
    }

    public static synchronized boolean removeConnection(Integer index) {
        return connections.remove(index);
    }

    public static synchronized SocketAddress getDisabledClient(SocketAddress address) {
        Optional<SocketAddress> socketAddress = disabledClients
                .stream()
                .filter(disabledAddresses -> disabledAddresses.equals(address))
                .findFirst();
        return socketAddress.orElse(null);
    }

    public static synchronized boolean disableClient(SocketAddress address) {
        if (!disabledClients.contains(address)) {
            disabledClients.add(address);
            return true;
        } else {
            return false;
        }
    }

    public static List<SocketAddress> getDisabledClientsList() {
        return Collections.unmodifiableList(disabledClients);
    }

    public static synchronized void enableClient(int index) {
        SocketAddress socketAddress = disabledClients.get(index);
        disabledClients.remove(socketAddress);
    }

    public static boolean disconnectClient(int index) {
        try {
            Connection connection = connections.get(index);
            connection.closeConnection();
            connections.remove(index);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
