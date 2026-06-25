package com.streaming.interfaces.billing;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateCardRequest(
        @NotBlank(message = "number must not be blank")
        String number,

        @NotBlank(message = "holder must not be blank")
        String holder,

        @NotBlank(message = "expiration must not be blank")
        @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{4}$", message = "expiration must follow MM/yyyy")
        String expiration,

        @NotBlank(message = "cvv must not be blank")
        @Pattern(regexp = "^\\d{3,4}$", message = "cvv must contain 3 or 4 digits")
        String cvv
) {
}
