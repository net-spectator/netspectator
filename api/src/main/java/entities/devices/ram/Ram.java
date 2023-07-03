package entities.devices.ram;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.devices.Hardware;
import lombok.Data;

@Data
public class Ram extends Hardware {
    @JsonProperty("totalSpace")
    private long totalSpace;
    @JsonProperty("usedSpace")
    private long usedSpace;
}
