package readers;

import entities.devices.Hardware;

import java.util.Collections;
import java.util.List;

public class CpuInfoCollector implements SensorInfoCollector {
    @Override
    public List<? super Hardware> collectInfo() {
        return Collections.emptyList();
    }

    @Override
    public void updateSensorInfo() {

    }
}
