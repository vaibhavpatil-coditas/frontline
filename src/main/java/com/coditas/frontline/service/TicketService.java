package com.coditas.frontline.service;

import com.coditas.frontline.dto.request.PriorityRequest;
import com.coditas.frontline.dto.request.RatingsRequest;
import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.TicketResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TicketService {
    TicketResponse raiseTicket(MultipartFile file, @Valid TicketRequest request) throws IOException;

    TicketResponse assignAgent(Long ticketId, Long agentId);

    List<TicketResponse> getAll();

    TicketResponse getTicketById(Long ticketId);

    TicketResponse resolveTicket(Long ticketId);

    TicketResponse escalateTicket(Long ticketId);

    TicketResponse rate(Long ticketId, RatingsRequest request);

    TicketResponse setPriority(Long ticketId, PriorityRequest request);
}
