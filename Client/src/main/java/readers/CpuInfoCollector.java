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
        // TODO: 24.06.2023 доработать сборщик информации о CPU
    }
}
