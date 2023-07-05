package enums;

import java.util.HashMap;

public class Help {
    private static final HashMap<String, String> connections;
    private static final HashMap<String, String> disconnected;
    private static final HashMap<String, String> nodes;
    private static final HashMap<String, String> root;

    static {
        //корневой help
        root = new HashMap<>();
        root.put("connections", "Управление подключенными клиентами");
        root.put("disconnected", "Управление отключенными клиентами");
        root.put("nodes", "Управление узлами");
        root.put("shutdown", "Завершение работы сервера");
        root.put("auth", "Авторизация на сервере. Маска [auth login pass]");

        //управление подключениями
        connections = new HashMap<>();
        connections.put("show", "Отображает список подключенных клиентов");
        connections.put("reboot [i]", "Перезагрузить клиент по индексу [i]. Для отображения списка используйте команду [show]");
        connections.put("this", "Показать параметры текущего подключения");

        //управление отключенными клиентами
        disconnected = new HashMap<>();
        disconnected.put("show", "Отображает список отключенных клиентов");
        disconnected.put("remove [i]", "Удаляет клиент из списка отключенных по индексу [i]. Для отображения списка используйте команду [show]");
        disconnected.put("add [i]", "Добавляет клиент в список отключенных по индексу [i]. Для отображения списка используйте команду [show]");

        //управление наблюдаемыми узлами
        nodes = new HashMap<>();
        nodes.put("detected", "Отображает список обнаруженных узлов. Для обнаружения узлов используйте [scan IP]");
        nodes.put("scan ip_address", "Выполнить сканирование сети [ip_address]. Пример: scan 192.168.0.0");
        nodes.put("add [i]", "Добавляет узел на отслеживание по индексу [i]. Для отображения списка используйте команду [detected]");
        nodes.put("tracked", "Отобразить наблюдаемые узлы");
        nodes.put("remove [i]", "Удаляет наблюдаемый узел из списка по индексу [i]. Для отображения списка используйте команду [tracked]");
        nodes.put("tracking [start/stop]", "Включает/отключает режим наблюдения за узлами");
    }

    private static String getHead() {
        return "* При указании аргументов квадратные скобочки не требуются\n* Для детальной информации по командам введите ? после наименования команды\n\n";
    }

    public static String getRootHelp() {
        return helpBuilder(root);
    }

    public static String getConnectionsHelp() {
        return helpBuilder(connections);
    }

    public static String getDisconnectedHelp() {
        return helpBuilder(disconnected);
    }

    public static String getCNodesHelp() {
        return helpBuilder(nodes);
    }

    /**
     * формирует список команд help
     */
    private static String helpBuilder(HashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(getHead());
        int longerKey = getLongerKey(map);
        map.forEach((key, value) -> sb
                .append(addSpace(longerKey - key.length()))
                .append(key)
                .append(" - ")
                .append(value)
                .append("\n"));
        return sb.toString();
    }

    /**
     * осуществляет поиск самого длинного ключа в мапе
     */
    private static int getLongerKey(HashMap<String, String> map) {
        final int[] longerKeyLength = {0};
        map.forEach((mapKey, value) -> {
            int keyLength = mapKey.length();
            if (keyLength > longerKeyLength[0]) {
                longerKeyLength[0] = keyLength;
            }
        });
        return longerKeyLength[0];
    }

    /**
     * собирает строку из пробелов [на вход принимает число пробелов]
     */
    private static String addSpace(int spaceLength) {
        return " ".repeat(Math.max(0, spaceLength));
    }

}
