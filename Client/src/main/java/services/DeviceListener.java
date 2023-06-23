package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.devices.ClientHardwareInfo;
import entities.devices.Hardware;
import entities.devices.ram.Ram;
import entities.devices.cpus.Cpu;
import entities.devices.drives.Drive;
import readers.SensorInfoCollector;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeviceListener implements Runnable {

    private final HashMap<String, SensorInfoCollector> sensors;

    private final DataOutputStream out;

    public DeviceListener(DataOutputStream out, String[] sensorsListFromServer) {
        sensors = new HashMap<>();
        this.out = out;
        Arrays.stream(sensorsListFromServer).forEach(this::createSensors);
    }

    private void updateSensorsStatement() {
        sensors.forEach((key, value) -> value.updateSensorInfo());
    }

    @Override
    public void run() {
        updateSensorsStatement();
        ClientHardwareInfo chi = clientHardwareInfoInit();
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

    private ClientHardwareInfo clientHardwareInfoInit() {
        ClientHardwareInfo chi = new ClientHardwareInfo();
        sensors.forEach((key, value) -> {
            switch (key) {
                case "DriveInfoCollector":
                    chi.setDrives(castList(Drive.class, sensors.get("DriveInfoCollector").collectInfo()));
                    break;
                case "CpuInfoCollector":
                    chi.setCpus(castList(Cpu.class, sensors.get("CpuInfoCollector").collectInfo()));
                    break;
                case "RamInfoCollector":
                    chi.setRam(castList(Ram.class, sensors.get("RamInfoCollector").collectInfo()));
                    break;
            }
        });
        return chi;
    }
}
