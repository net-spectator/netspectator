package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.devices.ClientHardwareInfo;
import entities.devices.Hardware;
import entities.devices.drives.Drive;
import readers.DriveInfoCollector;
import readers.SensorInfoCollector;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DeviceListener implements Runnable {

    private final HashMap<String, SensorInfoCollector> sensors;

    private final DataOutputStream out;

    public DeviceListener(DataOutputStream out) {
        sensors = new HashMap<>();
        this.out = out;
        sensors.put(castClass("readers.DriveInfoCollector").getClass().getSimpleName(),castClass("readers.DriveInfoCollector")); //временный вариант
    }

    private void updateSensorsStatement() {
        sensors.forEach((key, value) -> value.updateSensorInfo());
    }

    @Override
    public void run() { //ToDo доработать автоматическое создание сенсоров
        updateSensorsStatement();
        ClientHardwareInfo chi = new ClientHardwareInfo();
        chi.setDrives(castList(Drive.class, sensors.get("DriveInfoCollector").collectInfo()));
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(chi);
            out.write(("\\" + chi.getClass().getSimpleName() + " " + json).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}
