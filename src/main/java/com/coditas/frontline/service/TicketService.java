package com.coditas.frontline.service;

import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.TicketResponse;
import jakarta.validation.Valid;

public interface TicketService {
    TicketResponse raiseTicket(@Valid TicketRequest request);
}
