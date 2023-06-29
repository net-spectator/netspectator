package readers;

import entities.devices.Hardware;
import entities.devices.ram.Ram;
import oshi.SystemInfo;
import utils.NSLogger;
import utils.converter.ByteToString;

import java.util.ArrayList;
import java.util.List;

public class RamInfoCollector implements SensorInfoCollector {
    private final List<? super Hardware> ramList; // TODO: 24.06.2023 добавить логирование событий 
    private final SystemInfo systemInfo;
    private final Ram ram;
    private static NSLogger LOGGER = new NSLogger(RamInfoCollector.class);

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
        LOGGER.info("Определение состояние оперативной памяти");
        ramList.clear();
        ram.setTotalSpace(systemInfo.getHardware().getMemory().getTotal());
        ram.setUsedSpace(systemInfo.getHardware().getMemory().getTotal() - systemInfo.getHardware().getMemory().getAvailable());
        LOGGER.info(String.format("Всего оперативной памяти - %s, использовано памяти - %s", ByteToString.byteToString(ram.getTotalSpace()), ByteToString.byteToString(ram.getUsedSpace())));
        LOGGER.info("Сохраняю информацию о состоянии RAM");
        ramList.add(ram);
        LOGGER.info("Информация о состоянии RAM сохранена");
        LOGGER.info(String.format("Дальнейшая запись логов в классе %s не требуется, логер отключен", this.getClass().getSimpleName()));
        LOGGER.setLocalLog(false);
    }
}
