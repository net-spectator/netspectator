package readers;

import com.profesorfalken.jsensors.JSensors;
import com.profesorfalken.jsensors.model.components.Components;
import com.profesorfalken.jsensors.model.components.Disk;
import com.profesorfalken.jsensors.model.sensors.Temperature;
import entities.devices.Hardware;
import entities.devices.drives.Drive;
import entities.devices.drives.Partition;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;
import services.DeviceListener;
import utils.NSLogger;
import utils.converter.ByteToString;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DriveInfoCollector implements SensorInfoCollector {
    private Components component = null;
    private final SystemInfo systemInfo;
    private List<HWDiskStore> drives;
    private List<Disk> disks;
    private final List<? super Hardware> hardware;
    private static NSLogger LOGGER = new NSLogger(DriveInfoCollector.class);

    public DriveInfoCollector() {
        hardware = new ArrayList<>();
        systemInfo = new SystemInfo();
        updateSensorInfo();
    }

    @Override
    public List<? super Hardware> collectInfo() {
        return Collections.unmodifiableList(hardware);
    }

    /**
     * - Определяет состояние HDD/SSD (считывает доступное место, общий объем).
     * Если операционная система в списке поддерживаемых, добавляет температуру HDD/SSD.
     */
    @Override
    public void updateSensorInfo() {
        hardware.clear(); //очищаем лист от предыдущих записей
        drives = systemInfo.getHardware().getDiskStores();
        LOGGER.info(String.format("Используемая OS - %s для логирования HDD/SSD", DeviceListener.isSupportedOs() ? "поддерживается" : "не поддерживается"));

        if (DeviceListener.isSupportedOs()) { //инициализируем компоненты
            LOGGER.info("Инициализация компоненты JSensors");
            component = JSensors.get.components();
            LOGGER.info("Инициализация компоненты JSensors успешна");
            disks = component.disks;
        }

        StringBuilder sb = new StringBuilder();
        try {
            LOGGER.info(String.format("Обнаружено %s накопителей", drives.size()));
            for (HWDiskStore ds : drives) {
                if (ds.getSize() > 0) {
                    Drive drive = new Drive();
                    drive.setPartitions(new ArrayList<>());
                    sb.append(ds.getModel().replaceAll(" \\(Стандартные дисковые накопители\\)", ""));
                    drive.setDeviceName(sb.toString());
                    LOGGER.info(String.format("Проверяю накопитель %s", drive.getDeviceName()));
                    LOGGER.info(String.format("Накопитель %s содержит %s разделов", drive.getDeviceName(), ds.getPartitions().size()));
                    long diskTotalUsableSpace = 0;
                    List<HWPartition> partitions = ds.getPartitions();
                    for (HWPartition p : partitions) {
                        String path = p.getMountPoint();
                        LOGGER.info(String.format("Проверяю раздел %s", path));
                        FileStore fileStore = Files.getFileStore(Path.of(path));
                        long totalSpace = fileStore.getTotalSpace();
                        LOGGER.info(String.format("Общий размер раздела - %s", ByteToString.byteToString(totalSpace)));
                        if (totalSpace > 0) {
                            Partition partition = new Partition();
                            partition.setMountPoint(path);
                            LOGGER.info("Проверка пространства раздела");
                            long usableSpace = fileStore.getUsableSpace();
                            LOGGER.info(String.format("Доступное место в текущем разделе - %s", ByteToString.byteToString(usableSpace)));
                            partition.setTotalSpace(totalSpace);
                            long usedSpace = totalSpace - usableSpace;
                            LOGGER.info(String.format("Занято места - %s", ByteToString.byteToString(usedSpace)));
                            partition.setUsedSpace(usedSpace);
                            diskTotalUsableSpace += usedSpace;
                            drive.getPartitions().add(partition);
                        }
                    }
                    LOGGER.info(String.format("Всего места на накопителе - %s", ByteToString.byteToString(diskTotalUsableSpace)));
                    if (component != null && DeviceListener.isSupportedOs()) { //сопоставляем показатели температуры дискам
                        LOGGER.info(String.format("Сопоставляю температуру накопителю - %s", drive.getDeviceName()));
                        int occupiedSpaceInPercent = (int) (((double) diskTotalUsableSpace * 100) / ds.getSize());
                        LOGGER.info(String.format("Занято места на диске (процент) - %s ", occupiedSpaceInPercent));
                        disks.stream().filter(disk1 -> {
                            try {
                                int occupiedSpaceInPercentFromJSensor = (disk1.sensors.loads.get(0).value.intValue());
                                LOGGER.info(String.format("По результатам JSensor - %s, по результатам SystemInfo - %s ", occupiedSpaceInPercentFromJSensor, occupiedSpaceInPercent));
                                LOGGER.info(String.format("Наименование по результатам JSensor - %s, по результатам SystemInfo - %s ", drive.getDeviceName().toLowerCase(), disk1.name.toLowerCase()));
                                return occupiedSpaceInPercentFromJSensor == occupiedSpaceInPercent && drive.getDeviceName().toLowerCase().contains(disk1.name.toLowerCase());
                            } catch (IndexOutOfBoundsException e) {
                                return false;
                            }
                        }).findFirst().ifPresent(disk -> {
                            LOGGER.info("Сопоставляю температуру накопителю");
                            List<Temperature> temperature = disk.sensors.temperatures;
                            if (temperature.size() > 0) {
                                drive.setTemperature(disk.sensors.temperatures.get(0).value);
                                LOGGER.info(String.format("Температура накопителя - %s C", drive.getTemperature()));
                            }
                        });
                    }
                    LOGGER.info("Сохраняю информацию о накопителе");
                    hardware.add(drive);
                    LOGGER.info("Информация о накопителе сохранена");
                    sb.delete(0, sb.length());
                }
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Обнаружена ошибка при проверке накопителей - %s", e));
        }
        LOGGER.info(String.format("Дальнейшая запись логов в классе %s не требуется, логер отключен", this.getClass().getSimpleName()));
        LOGGER.setDisabled(true);
    }
}
