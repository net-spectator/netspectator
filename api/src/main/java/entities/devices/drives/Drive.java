package entities.devices.drives;

import com.fasterxml.jackson.annotation.JsonProperty;
import entities.devices.Hardware;
import lombok.Data;

import java.util.List;

@Data
public class Drive extends Hardware {
    @JsonProperty("deviceName")
    private String deviceName;
    @JsonProperty("temperature")
    private long temperature;
    @JsonProperty("partitions")
    private List<Partition> partitions;
}
