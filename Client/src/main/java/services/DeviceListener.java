package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.devices.ClientHardwareInfo;
import entities.devices.ram.Ram;
import entities.devices.cpus.Cpu;
import entities.devices.drives.Drive;
import oshi.SystemInfo;
import readers.SensorInfoCollector;
import utils.NSLogger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeviceListener implements Runnable {
    private final HashMap<String, SensorInfoCollector> sensors;
    private static final List<String> unsupportedOsList = Arrays.asList("Mac", "Android");
    private ClientHardwareInfo chi;
    private final DataOutputStream out;
    private static boolean supportedOs = false;
    private static final NSLogger LOGGER = new NSLogger(DeviceListener.class);

    public static boolean isSupportedOs() {
        return supportedOs;
    }

    public DeviceListener(DataOutputStream out, String[] sensorsListFromServer) {
        supportedOs = checkOs();
        sensors = new HashMap<>();
        chi = new ClientHardwareInfo();
        this.out = out;
        LOGGER.info(String.format("Инициализация сенсоров: %s", Arrays.toString(sensorsListFromServer)));
        Arrays.stream(sensorsListFromServer).forEach(this::createSensors);
    }

    private boolean checkOs() {
        String currentOs = System.getProperty("os.name");
        LOGGER.info(String.format("Используемая OS: %s", currentOs));
        return unsupportedOsList.stream().noneMatch(currentOs::startsWith);
    }

    private void updateSensorsStatement() {
        sensors.forEach((key, value) -> value.updateSensorInfo());
    }

    @Override
    public void run() {
        chi = new ClientHardwareInfo();
        updateSensorsStatement();
        clientHardwareInfoInit();
        sendClientHardwareInfoToServer(chi);
    }

    public <T> List<T> castList(Class<? extends T> clazz, Collection<?> rawCollection) {
        try {
            return rawCollection.stream().map((Function<Object, T>) clazz::cast).collect(Collectors.toList());
        } catch (ClassCastException e) {
            return Collections.emptyList();
        }
    }

    public static SensorInfoCollector castClass(String name) {
        Class<?> clazz = null;
        Object testClass;
        try {
            clazz = Class.forName(name);
            testClass = clazz.getConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return (SensorInfoCollector) testClass;
    }

    private void createSensors(String sensorName) {
        sensors.put(castClass("readers." + sensorName)
                        .getClass()
                        .getSimpleName(),
                castClass("readers." + sensorName));
    }

    private void sendClientHardwareInfoToServer(ClientHardwareInfo clientHardwareInfo) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(clientHardwareInfo);
            out.write(("\\" + clientHardwareInfo.getClass().getSimpleName() + " " + json).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clientHardwareInfoInit() {
        clientStaticInfoInit();
        sensors.forEach((key, value) -> {
            switch (key) {
                case "DriveInfoCollector":
                    chi.setDrives(castList(Drive.class, sensors.get("DriveInfoCollector").collectInfo()));
                    break;
                case "CpuInfoCollector":
                    chi.setCpus(castList(Cpu.class, sensors.get("CpuInfoCollector").collectInfo()));
                    break;
                case "RamInfoCollector":
                    chi.setRam(castList(Ram.class, sensors.get("RamInfoCollector").collectInfo()).get(0));
                    break;
            }
        });
    }

    private void clientStaticInfoInit() {
        SystemInfo systemInfo = new SystemInfo();
        chi.setOsFamily(systemInfo.getOperatingSystem().toString());
        chi.setOsManufacture(systemInfo.getOperatingSystem().getManufacturer());
        chi.setOsVersion(systemInfo.getOperatingSystem().getVersionInfo().getVersion());
    }
}
