package readers;

import entities.devices.Hardware;

import java.util.Collections;
import java.util.List;

public class RamInfoCollector implements SensorInfoCollector {
    private List<? super Hardware> ram;

    public RamInfoCollector() {
    }

    @Override
    public List<? super Hardware> collectInfo() {

        return Collections.emptyList();
    }

    @Override
    public void updateSensorInfo() {

    }
}
