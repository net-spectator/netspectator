package services;

import entities.Connection;
import entities.TrackedEquipment;
import entities.devices.ClientHardwareInfo;

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

    /**
     * Возвращает singleton текущего объекта
     */
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

    /**
     * Добавляет подключение в список
     */
    public static synchronized void addConnection(Connection connection) {
        connections.add(connection);
    }

    /**
     * Возвращает неизменяемый лист текущих подключений
     */
    public static synchronized List<Connection> getConnectionsList() {
        return Collections.unmodifiableList(connections);
    }

    /**
     * Возвращает подключение по UUID,
     * если подключение не найдено - возвращает NULL
     */
    public static synchronized Connection getConnection(String UUID) {
        Optional<Connection> connectionElement = connections
                .stream()
                .filter(connection -> connection.getDevice().getEquipmentUuid().equals(UUID))
                .findFirst();
        return connectionElement.orElse(null);
    }

    /**
     * Возвращает подключение по индексу
     */
    public static synchronized Connection getConnection(Integer index) {
        return connections.get(index);
    }

    /**
     * Осуществляет поиск адреса в списке отключенных клиентов, если не находит, возвращает null
     */
    public static synchronized SocketAddress getDisabledClient(SocketAddress address) {
        Optional<SocketAddress> socketAddress = disabledClients
                .stream()
                .filter(disabledAddresses -> disabledAddresses.equals(address))
                .findFirst();
        return socketAddress.orElse(null);
    }

    /**
     * Добавляет клиент в список отключенных
     */
    public static synchronized boolean disableClient(SocketAddress address) {
        if (!disabledClients.contains(address)) {
            disabledClients.add(address);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Возвращает неизменяемый лист текущих отключенных агентов
     */
    public static List<SocketAddress> getDisabledClientsList() {
        return Collections.unmodifiableList(disabledClients);
    }

    /**
     * Удаляет клиент из списка отключенных по индексу
     */
    public static synchronized void enableClient(int index) {
        SocketAddress socketAddress = disabledClients.get(index);
        disabledClients.remove(socketAddress);
    }

    /**
     * Отключение клиента по индексу
     */
    public static synchronized boolean disconnectClient(int index) {
        try {
            Connection connection = connections.get(index);
            connection.closeConnection();
            connections.remove(index);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * отключение клиента по device_id
     */
    public static synchronized boolean disconnectClient(long deviceId) {
        try {
            Connection removedConnection = connections
                    .stream()
                    .filter(connection -> connection.getDevice().getId().equals(deviceId))
                    .findAny()
                    .orElse(null);
            assert removedConnection != null;
            removedConnection.closeConnection();
            connections.remove(removedConnection);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Отключение клиента по объекту Connection
     */
    public static boolean disconnectClient(Connection connection) {
        try {
            Connection removedConnection = connections
                    .stream()
                    .filter(connection::equals)
                    .findAny()
                    .orElse(null);
            assert removedConnection != null;
            removedConnection.closeConnection();
            connections.remove(removedConnection);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Возвращает текущую информацию о клиенте и состояние всех его сенсоров
     */
    public static synchronized ClientHardwareInfo getClientHardwareInfo(long deviceId) {
        Connection result = connections
                .stream()
                .filter(connection -> connection.getDevice().getId().equals(deviceId))
                .findAny()
                .orElse(null);
        assert result != null;
        TrackedEquipment te = result.getDevice();
        return te.getDeviceInfo();
    }
}
