package entities.devices.cpus;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.devices.Hardware;

import java.util.List;

public class Cpu extends Hardware {
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("cores")
    private List<CpuCore> cores;
}
