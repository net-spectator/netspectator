package entities.devices;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import entities.devices.cpus.Cpu;
import entities.devices.drives.Drive;
import entities.devices.ram.Ram;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientHardwareInfo {
    @JsonProperty("osFamily")
    private String osFamily;
    @JsonProperty("osVersion")
    private String osVersion;
    @JsonProperty("osManufacture")
    private String osManufacture;
    @JsonProperty("cpus")
    private List<Cpu> cpus;
    @JsonProperty("ram")
    private Ram ram;
    @JsonProperty("drives")
    private List<Drive> drives;
}
