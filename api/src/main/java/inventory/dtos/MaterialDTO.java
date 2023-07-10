package inventory.dtos;

import inventory.enums.StateEquipment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class MaterialDTO {
    private UUID id;
    private String name;
    private String description;
    private String serialNumber;
    private StateEquipment state;
    // TODO: replace type of the field "responsible" on userDTO
    private UUID responsible;
    private TypeMaterialDTO typeMaterial;
    private VendorDTO vendor;
    private Long registrationNumber;

}
