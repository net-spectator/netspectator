package entities.devices;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Ram extends Hardware{
    @JsonProperty("totalSpace")
    private long totalSpace;
    @JsonProperty("usedSpace")
    private long usedSpace;
}
