package readers;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Cpu;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import entities.devices.Hardware;
import entities.devices.cpus.CpuCore;
import services.DeviceListener;
import utils.NSLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CpuInfoCollector implements SensorInfoCollector {
    private final List<? super Hardware> cpuList;

    public CpuInfoCollector() { // TODO: 25.06.2023 перенести в проект
        cpuList = new ArrayList<>();
    }

    private static NSLogger LOGGER = new NSLogger(CpuInfoCollector.class);

    @Override
    public List<? super Hardware> collectInfo() {
        return Collections.unmodifiableList(cpuList);
    }

    @Override
    public void updateSensorInfo() {
        cpuList.clear();
        LOGGER.info(String.format("Используемая OS - %s для логирования CPU", DeviceListener.isSupportedOs() ? "поддерживается" : "не поддерживается"));
        if (DeviceListener.isSupportedOs()) {
            LOGGER.info("Инициализация компоненты JSensors");
            Components component = JSensors.get.components();
            LOGGER.info("Инициализация компоненты JSensors успешна");
            List<Cpu> cpus = component.cpus;
            LOGGER.info(String.format("Обнаружен процессор, количество ядер - %s", cpus.size()));
            entities.devices.cpus.Cpu deviceCpu = new entities.devices.cpus.Cpu();
            for (final Cpu c : cpus) {
                deviceCpu.setDeviceName(c.name);
                LOGGER.info(String.format("Наименование процессора - %s", deviceCpu.getDeviceName()));
                if (c.sensors.temperatures.size() > 0) {
                    LOGGER.info("Чтение информации о ядрах CPU");
                    deviceCpu.setCores(new ArrayList<>());
                    for (Temperature cores : c.sensors.temperatures) {
                        CpuCore core = new CpuCore();
                        core.setCoreName(cores.name);
                        core.setTemperature(cores.value);
                        LOGGER.info(String.format("Наименование ядра - %s, температура ядра - %s", core.getCoreName(), core.getTemperature()));
                        deviceCpu.getCores().add(core);
                    }
                    LOGGER.info("Чтение завершено");
                }
                LOGGER.info("Сохраняю информацию о процессоре");
                cpuList.add(deviceCpu);
                LOGGER.info("Информация о процессоре сохранена");
            }
        }
        LOGGER.info(String.format("Дальнейшая запись логов в классе %s не требуется, логер отключен", this.getClass().getSimpleName()));
        LOGGER = null;
    }
}
