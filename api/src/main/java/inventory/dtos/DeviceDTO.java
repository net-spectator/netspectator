package inventory.dtos;

import inventory.enums.StateEquipment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class DeviceDTO {
    private UUID id;
    private String name;
    private String description;
    private StateEquipment state;
    private UUID responsible;
    private UUID user;
    private TypeMaterialDTO typeMaterial;
    private VendorDTO vendor;
    private Long registrationNumber;
}
