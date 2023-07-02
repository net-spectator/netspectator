package readers;

import com.profesorfalken.jsensors.JSensors;
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

    public CpuInfoCollector() {
        cpuList = new ArrayList<>();
    }

    private static NSLogger LOGGER = new NSLogger(CpuInfoCollector.class);

    @Override
    public synchronized List<? super Hardware> collectInfo() {
        return Collections.unmodifiableList(cpuList);
    }

    @Override
    public void updateSensorInfo() {
        cpuList.clear();
        LOGGER.info(String.format("Используемая OS - %s для детального логирования CPU", DeviceListener.isSupportedOs() ? "поддерживается" : "не поддерживается"));
        if (DeviceListener.isSupportedOs()) {
            LOGGER.info("Инициализация компоненты JSensors");
            List<Cpu> cpus = JSensors.get.components().cpus;
            LOGGER.info("Инициализация компоненты JSensors успешна");
            LOGGER.info(String.format("Обнаружено процессоров - %s", cpus.size()));
            entities.devices.cpus.Cpu deviceCpu = new entities.devices.cpus.Cpu();
            for (final Cpu c : cpus) {
                LOGGER.info(String.format("Количество ядер - %s", c.sensors.temperatures.size()-1));
                deviceCpu.setDeviceName(c.name);
                LOGGER.info(String.format("Наименование процессора - %s", deviceCpu.getDeviceName()));
                if (c.sensors.temperatures.size() > 0) {
                    LOGGER.info("Чтение информации о ядрах CPU");
                    deviceCpu.setCores(new ArrayList<>());
                    for (Temperature cores : c.sensors.temperatures) {
                        if (!cores.name.contains("Package")) {
                            CpuCore core = new CpuCore();
                            core.setCoreName(cores.name);
                            core.setCoreTemperature(cores.value);
                            LOGGER.info(String.format("Наименование ядра - %s, температура ядра - %s", core.getCoreName(), core.getCoreTemperature()));
                            deviceCpu.getCores().add(core);
                        } else {
                            deviceCpu.setCpuTemperature(cores.value);
                        }
                    }
                    LOGGER.info("Чтение завершено");
                }
                LOGGER.info("Сохраняю информацию о процессоре");
                cpuList.add(deviceCpu);
                LOGGER.info("Информация о процессоре сохранена");
            }
        }
        LOGGER.info(String.format("Дальнейшая запись логов в классе %s не требуется, логер отключен", this.getClass().getSimpleName()));
        LOGGER.setLocalLog(false);
    }
}
