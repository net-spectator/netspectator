package entities.devices.cpus;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CpuCore {
    @JsonProperty("coreName")
    private String coreName;
    @JsonProperty("temperature")
    private long temperature;
}
