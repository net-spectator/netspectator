package services;

import entities.TrackedEquipment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NodeListener {
    private static volatile List<TrackedEquipment> routersList;

    private static volatile NodeListener nodeListener;

    private static long INIT_DELAY = 0; // TODO: 22.06.2023 перенести в clientParams
    private static long PERIOD = 5; // TODO: 22.06.2023 перенести в clientParams

    private ScheduledExecutorService executor;

    private NodeListener() {
        routersList = new ArrayList<>();
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

    public static boolean checkRouter(String address) { // TODO: 22.06.2023 добавить реализацию
        return false;
    }

    public static List<String> searchRouters(String ipRange) { // TODO: 22.06.2023 поискать готовые библиотеки
        return Collections.emptyList();
    }

    private void startListener() {
        executor.scheduleAtFixedRate(new CheckRouters(), INIT_DELAY, PERIOD, TimeUnit.SECONDS);
    }

    private static class CheckRouters implements Runnable {
        @Override
        public void run() {
            // TODO: 22.06.2023 осуществляет проверку доступности узлов из списка
        }
    }

}
