package org.net.webcoreservice.exeptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AppError {
    private Integer code;
    private String message;
}
