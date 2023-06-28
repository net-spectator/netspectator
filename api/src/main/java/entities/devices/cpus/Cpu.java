package entities.devices.cpus;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.devices.Hardware;
import lombok.Data;

import java.util.List;
@Data
public class Cpu extends Hardware {
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("cpuTemperature")
    private double cpuTemperature;
    @JsonProperty("cores")
    private List<CpuCore> cores;
}
