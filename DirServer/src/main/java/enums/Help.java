package enums;

import java.util.HashMap;
import java.util.LinkedHashMap;

//Не Enum, но очень похож по содержимому
public class Help {
    private static final LinkedHashMap<String, String> connections;
    private static final LinkedHashMap<String, String> disconnected;
    private static final LinkedHashMap<String, String> nodes;
    private static final LinkedHashMap<String, String> root;
    private static StringBuilder sb = null;

    static {
        //корневой help
        root = new LinkedHashMap<>();
        root.put("auth [login] [pass]", "Авторизация на сервере.");
        root.put("connections [args]", "Управление подключенными клиентами");
        root.put("disconnected [args]", "Управление отключенными клиентами");
        root.put("nodes [args]", "Управление узлами");
        root.put("shutdown [args]", "Завершение работы сервера");

        //управление подключениями
        connections = new LinkedHashMap<>();
        connections.put("show", "Отображает список подключенных клиентов");
        connections.put("reboot [index]", "Перезагрузить клиент по индексу [index]. Для отображения списка используйте команду [show]");
        connections.put("this", "Показать параметры текущего подключения");

        //управление отключенными клиентами
        disconnected = new LinkedHashMap<>();
        disconnected.put("show", "Отображает список отключенных клиентов");
        disconnected.put("add [index]", "Добавляет клиент в список отключенных по индексу [index]. Для отображения списка используйте команду [show]");
        disconnected.put("remove [index]", "Удаляет клиент из списка отключенных по индексу [index]. Для отображения списка используйте команду [show]");

        //управление наблюдаемыми узлами
        nodes = new LinkedHashMap<>();
        nodes.put("scan ip_address", "Выполнить сканирование сети [ip_address]. Пример: scan 192.168.0.0");
        nodes.put("detected", "Отображает список обнаруженных узлов. Для обнаружения узлов используйте [scan IP]");
        nodes.put("add [index]", "Добавляет узел на отслеживание по индексу [index]. Для отображения списка используйте команду [detected]");
        nodes.put("add [index] [-n] [name]", "Добавляет узел на отслеживание по индексу [index] с заданием имени [name]. Для отображения списка используйте команду [detected]");
        nodes.put("tracked", "Отобразить наблюдаемые узлы");
        nodes.put("remove [index]", "Удаляет наблюдаемый узел из списка по индексу [index]. Для отображения списка используйте команду [tracked]");
        nodes.put("tracking [start/stop]", "Включает/отключает режим наблюдения за узлами");
    }

    private static StringBuilder getHead(int longerKey) {
        StringBuilder sb = new StringBuilder();
        sb.append("* При указании аргументов квадратные скобочки не требуются\n* Для детальной информации по командам введите ? после наименования команды\n\n")
                .append(addSpace(longerKey - "Наименование:".length()))
                .append("Наименование: - Описание:\n");
        return sb;
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
     * формирует список команд help с header
     */
    private static String helpBuilder(HashMap<String, String> map) {
        sb = new StringBuilder();
        int longerKey = getLongerKey(map);
        sb.append(getHead(longerKey));
        map.forEach((key, value) -> sb
                .append(addSpace(longerKey - key.length()))
                .append(key)
                .append(" - ")
                .append(value)
                .append("\n"));
        return sb.toString();
    }

    /**
     * осуществляет поиск самого длинного ключа в map
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
