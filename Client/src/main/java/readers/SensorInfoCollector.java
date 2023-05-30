package readers;

import entities.devices.Hardware;

import java.util.List;

public interface SensorInfoCollector {
    List<? super Hardware> collectInfo();
    void updateSensorInfo();
}
