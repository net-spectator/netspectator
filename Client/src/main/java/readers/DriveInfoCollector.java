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

    public DriveInfoCollector() {
        hardware = new ArrayList<>();
        systemInfo = new SystemInfo();
        updateSensorInfo();
    }

    /**
     * - Определяет состояние HDD/SSD (считывает доступное место, общий объем).
     * Если операционная система в списке поддерживаемых, добавляет температуру HDD/SSD.
     * */
    @Override
    public List<? super Hardware> collectInfo() {
        hardware.clear();
        StringBuilder sb = new StringBuilder();
        try {
            for (HWDiskStore ds : drives) {
                if (ds.getSize() > 0) {
                    Drive drive = new Drive();
                    drive.setPartitions(new ArrayList<>());
                    sb.append(ds.getModel().replaceAll(" \\(Стандартные дисковые накопители\\)", ""));
                    drive.setDeviceName(sb.toString());
                    long diskTotalUsableSpace = 0;
                    List<HWPartition> partitions = ds.getPartitions();
                    for (HWPartition p : partitions) {
                        String path = p.getMountPoint();
                        FileStore fileStore = Files.getFileStore(Path.of(path));
                        long totalSpace = fileStore.getTotalSpace();
                        if (totalSpace > 0) {
                            Partition partition = new Partition();
                            partition.setMountPoint(path);
                            long usableSpace = fileStore.getUsableSpace();
                            partition.setTotalSpace(totalSpace);
                            long usedSpace = totalSpace - usableSpace;
                            partition.setUsedSpace(usedSpace);
                            diskTotalUsableSpace += usedSpace;
                            drive.getPartitions().add(partition);
                        }
                    }
                    if (component != null && DeviceListener.isSupportedOs()) { //сопоставляем показатели температуры дискам
                        int occupiedSpaceInPercent = (int) (((double) diskTotalUsableSpace * 100) / ds.getSize());
                        disks.stream().filter(disk1 -> {
                            try {
                                int occupiedSpaceInPercentFromJSensor = (disk1.sensors.loads.get(0).value.intValue());
                                return occupiedSpaceInPercentFromJSensor == occupiedSpaceInPercent && drive.getDeviceName().toLowerCase().contains(disk1.name.toLowerCase());
                            } catch (IndexOutOfBoundsException e) {
                                return false;
                            }
                        }).findFirst().ifPresent(disk -> {
                            List<Temperature> temperature = disk.sensors.temperatures;
                            if (temperature.size() > 0) {
                                drive.setTemperature(disk.sensors.temperatures.get(0).value);
                            }
                        });
                    }
                    hardware.add(drive);
                    sb.delete(0, sb.length());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Collections.unmodifiableList(hardware);
    }

    @Override
    public void updateSensorInfo() {
        drives = systemInfo.getHardware().getDiskStores();
        if (DeviceListener.isSupportedOs()) {
            component = JSensors.get.components();
            disks = component.disks;
        }
    }
}
