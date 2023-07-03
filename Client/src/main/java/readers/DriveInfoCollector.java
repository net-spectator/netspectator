package readers;

import com.profesorfalken.jsensors.JSensors;
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
    private final SystemInfo systemInfo;
    private List<HWDiskStore> drives;
    private List<Disk> disks;
    private final List<? super Hardware> hardware;
    private static final NSLogger LOGGER = new NSLogger(DriveInfoCollector.class);

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
            disks = JSensors.get.components().disks;
            LOGGER.info("Инициализация компоненты JSensors успешна");
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
                    if (DeviceListener.isSupportedOs() && diskTotalUsableSpace > 0) { //сопоставляем показатели температуры дискам
                        LOGGER.info(String.format("Сопоставляю температуру накопителю - %s", drive.getDeviceName()));
                        int occupiedSpaceInPercent = (int) (((double) diskTotalUsableSpace * 100) / ds.getSize());
                        LOGGER.info(String.format("Занято места на диске (процент) - %s ", occupiedSpaceInPercent));
                        disks.stream().filter(JSensorsDisk -> {
                            try {
                                int occupiedSpaceInPercentFromJSensor = (JSensorsDisk.sensors.loads.get(0).value.intValue());
                                LOGGER.info(String.format("По результатам SystemInfo - %s, по результатам JSensor - %s ", occupiedSpaceInPercentFromJSensor, occupiedSpaceInPercent));
                                return occupiedSpaceInPercentFromJSensor == occupiedSpaceInPercent;
                            } catch (IndexOutOfBoundsException e) {
                                return false;
                            }
                        }).findFirst().ifPresent(JSensorsDisk -> {
                            LOGGER.info("Сопоставляю температуру накопителю");
                            List<Temperature> temperature = JSensorsDisk.sensors.temperatures;
                            if (temperature.size() > 0) {
                                drive.setTemperature(JSensorsDisk.sensors.temperatures.get(0).value);
                                LOGGER.info(String.format("Температура накопителя - %s C", drive.getTemperature()));
                                disks.remove(JSensorsDisk);
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
        LOGGER.setLocalLog(false);
    }
}
