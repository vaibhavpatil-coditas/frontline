package com.coditas.frontline.dto.response;

import com.coditas.frontline.enums.TicketCategory;
import com.coditas.frontline.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
public class TicketResponse {
    private Long id;
    private Long customerId;
    private Long agentId;
    private String description;
    private TicketCategory category;
    private TicketStatus status;
    private Instant raisedAt;
}
