package com.coditas.frontline.dto.request;

import com.coditas.frontline.enums.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TicketRequest {
    @NotBlank(message = "Description cannot be blank or null, please provide the description")
    private String description;
    @NotNull(message = "Ticket category cannot be null, please provide the ticket category")
    @NotBlank(message = "Ticket category cannot be blank, please provider the ticket category")
    private TicketCategory category;
}
