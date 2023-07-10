package inventory.dtos;

import inventory.enums.RegistrationEventsType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class RegistrationDataDTO {

    private UUID id;
    private Long registrationNumber;
    private Long placeNumber;
    private RegistrationEventsType eventsType;
    private Date date;

}
