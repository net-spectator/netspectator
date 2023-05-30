package entities.devices.drives;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Partition {
    @JsonProperty("mountPoint")
    private String mountPoint;
    @JsonProperty("totalSpace")
    private long totalSpace;
    @JsonProperty("usedSpace")
    private long usedSpace;
    @JsonProperty("dive")
    private Drive drive;
}
