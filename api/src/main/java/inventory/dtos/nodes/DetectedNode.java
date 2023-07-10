package inventory.dtos.nodes;

import lombok.Data;

@Data
public class DetectedNode {
    private String nodeName;
    private String ipAddress;
    private String MACAddress;

    @Override
    public String toString() {
        return String.format("Имя узла: %s, IP адрес: %s, MAC адрес: %s",
                nodeName.length() > 0 ? "EMPTY" : nodeName,
                ipAddress,
                MACAddress);
    }
}
