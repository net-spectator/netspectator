package readers;

import entities.devices.Hardware;
import entities.devices.drives.Drive;
import entities.devices.drives.Partition;
import oshi.SystemInfo;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HWPartition;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DriveInfoCollector implements SensorInfoCollector {
    private final SystemInfo systemInfo;
    private List<HWDiskStore> drives;

    public DriveInfoCollector() {
        systemInfo = new SystemInfo();
        updateSensorInfo();
    }

    @Override
    public List<? super Hardware> collectInfo() {
        List<? super Hardware> hardware = new ArrayList<>();
        try {
            for (HWDiskStore ds : drives) {
                if (ds.getSize() > 0) {
                    Drive drive = new Drive();
                    drive.setPartitions(new ArrayList<>());
                    drive.setDeviceName(ds.getModel().replaceAll(" \\(Стандартные дисковые накопители\\)", ""));
                    List<HWPartition> partitions = ds.getPartitions();
                    for (HWPartition p : partitions) {
                        String path = p.getMountPoint();
                        FileStore fileStore = Files.getFileStore(Path.of(path));
                        if (fileStore.getTotalSpace() > 0) {
                            Partition partition = new Partition();
                            partition.setMountPoint(path);
                            partition.setTotalSpace(fileStore.getTotalSpace());
                            partition.setUsedSpace(fileStore.getUsableSpace());
                            drive.getPartitions().add(partition);
                        }
                    }
                    hardware.add(drive);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return hardware;
    }

    @Override
    public void updateSensorInfo() {
        drives = systemInfo.getHardware().getDiskStores();
    }
}
