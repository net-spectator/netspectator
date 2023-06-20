package readers;

import entities.devices.Hardware;
import entities.devices.Ram;

import java.util.Collections;
import java.util.List;

public class RamInfoCollector implements SensorInfoCollector {
    @Override
    public List<? super Hardware> collectInfo() {
        return Collections.emptyList();
    }

    @Override
    public void updateSensorInfo() {

    }
}
