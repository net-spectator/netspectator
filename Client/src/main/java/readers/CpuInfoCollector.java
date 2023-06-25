package readers;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import entities.devices.Hardware;
import entities.devices.cpus.CpuCore;
import services.DeviceListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CpuInfoCollector implements SensorInfoCollector {
    private final List<? super Hardware> cpuList;

    public CpuInfoCollector() { // TODO: 25.06.2023 перенести в проект
        cpuList = new ArrayList<>();
    }

    @Override
    public List<? super Hardware> collectInfo() {
        return Collections.unmodifiableList(cpuList);
    }

    @Override
    public void updateSensorInfo() {
        cpuList.clear();
        if (DeviceListener.isSupportedOs()) {
            Components component = JSensors.get.components();
            List<Cpu> cpus = component.cpus;
            entities.devices.cpus.Cpu deviceCpu = new entities.devices.cpus.Cpu();
            for (final Cpu c : cpus) {
                deviceCpu.setDeviceName(c.name);
                if (c.sensors.temperatures.size() > 0) {
                    deviceCpu.setCores(new ArrayList<>());
                    for (Temperature cores: c.sensors.temperatures){
                        CpuCore core = new CpuCore();
                        core.setCoreName(cores.name);
                        core.setTemperature(cores.value);
                        deviceCpu.getCores().add(core);
                    }
                }
                cpuList.add(deviceCpu);
            }
        }
    }
}
