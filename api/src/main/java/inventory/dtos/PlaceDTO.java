package inventory.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
public class PlaceDTO {
    private UUID id;
    private String name;
    private String description;
    private UUID parent_id;
    private Long registrationNumber;
}
