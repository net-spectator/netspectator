package services;

import amq.entities.NotificationMessage;
import amq.services.RabbitMQProducerService;
import amq.services.RabbitMQProducerServiceImpl;
import entities.TrackedEquipment;
import entities.devices.ClientHardwareInfo;
import entities.devices.cpus.Cpu;
import entities.devices.cpus.CpuCore;
import entities.devices.drives.Drive;
import entities.devices.drives.Partition;
import entities.devices.ram.Ram;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import utils.NSLogger;
import utils.SpringContext;

import java.util.HashMap;
import java.util.List;


public class ClientHardwareAnalyzer {
    private final NSLogger LOGGER;
    private long usableDriveSpaceWarning;
    private long usableRamSpaceWarning;
    private long cpuTemperatureWarning;
    private final TrackedEquipment device;
    private HashMap<String, Long> warnings;

    public ClientHardwareAnalyzer(TrackedEquipment device) {
        LOGGER = new NSLogger(ClientHardwareAnalyzer.class);
        this.device = device;
    }

    public void check(ClientHardwareInfo hardwareInfo) {
        warnings = new HashMap<>();
        getWarningParameters();
        checkHDD(hardwareInfo.getDrives());
        checkRAM(hardwareInfo.getRam());
        checkNotifications();
    }

    private void getWarningParameters() {
        try {
            usableDriveSpaceWarning = Long.parseLong(ClientListenersStarter.getProperties("usableDriveSpaceWarning"));
            usableRamSpaceWarning = Long.parseLong(ClientListenersStarter.getProperties("usableRamSpaceWarning"));
            cpuTemperatureWarning = Long.parseLong(ClientListenersStarter.getProperties("cpuTemperatureWarning"));
        } catch (NumberFormatException e) {
            LOGGER.error("Неверный формат граничных данных в файле server.properties");
            usableDriveSpaceWarning = 100;
            usableRamSpaceWarning = 100;
            cpuTemperatureWarning = 999;
        }
    }

    /**
     * Вычисляет оставшееся место по разделам в % и сопоставляет с граничными параметрами в server.properties.
     * При обнаружении несоответствия, формирует сообщение с текущим процентом оставшегося места.
     */
    private void checkHDD(List<Drive> drives) {
        if (drives != null) {
            for (Drive d : drives) {
                for (Partition p : d.getPartitions()) {
                    long usableSpacePercent = ((p.getTotalSpace() - p.getUsedSpace()) * 100) / p.getTotalSpace();
                    if (usableSpacePercent <= usableDriveSpaceWarning) {
                        warnings.put(String.format("Клиент: %s, диск %s, точка монтирования - %s, превышены критические показатели заполнения раздела",
                                device.getEquipmentTitle(),
                                d.getDeviceName(),
                                p.getMountPoint()
                        ), usableSpacePercent);
                    }
                }
            }
        }
    }

    /**
     * Получает температуру ядер CPU и сопоставляет с граничными параметрами в server.properties.
     * При обнаружении несоответствия, формирует сообщение с текущим процентом оставшегося места.
     */
    private void checkCPU(List<Cpu> cpus) {
        if (cpus != null) {
            for (Cpu cpu : cpus) {
                for (CpuCore core : cpu.getCores()) {
                    if (core.getCoreTemperature() >= cpuTemperatureWarning) {
                        warnings.put(String.format("Клиент: %s, cpu %s, превышены критические показатели температуры ядра",
                                device.getEquipmentTitle(),
                                core.getCoreTemperature()
                        ), (long) cpu.getCpuTemperature());
                    }
                }
            }
        }
    }

    /**
     * Вычисляет оставшееся место оперативной памяти (RAM) в % и сопоставляет с граничными параметрами в server.properties.
     * При обнаружении несоответствия, формирует сообщение с текущим процентом оставшегося места.
     */
    private void checkRAM(Ram ram) {
        if (ram != null) {
            long usableRamSpacePercent = ((ram.getTotalSpace() - ram.getUsedSpace()) * 100) / ram.getTotalSpace();
            if (usableRamSpacePercent <= usableRamSpaceWarning) {
                warnings.put(String.format("Клиент: %s, превышены критические показатели заполнения оперативной памяти",
                        device.getEquipmentTitle()
                ), usableRamSpacePercent);
            }
        }
    }

    private void checkNotifications() {

    }
}
