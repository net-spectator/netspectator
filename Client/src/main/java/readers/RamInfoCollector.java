package readers;

import entities.devices.Hardware;
import entities.devices.ram.Ram;
import oshi.SystemInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RamInfoCollector implements SensorInfoCollector {
    private final List<? super Hardware> ramList; // TODO: 24.06.2023 добавить логирование событий 
    private final SystemInfo systemInfo;
    private final Ram ram;

    public RamInfoCollector() {
        systemInfo = new SystemInfo();
        ramList = new ArrayList<>();
        ram = new Ram();
    }

    @Override
    public List<? super Hardware> collectInfo() {
        return ramList;
    }

    @Override
    public void updateSensorInfo() {
        ramList.clear();
        ram.setTotalSpace(systemInfo.getHardware().getMemory().getTotal());
        ram.setUsedSpace(systemInfo.getHardware().getMemory().getTotal() - systemInfo.getHardware().getMemory().getAvailable());
        ramList.add(ram);
    }
}
