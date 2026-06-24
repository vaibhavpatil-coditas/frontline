package com.coditas.frontline.controller;

import com.coditas.frontline.constants.ApiPaths;
import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.ApplicationResponse;
import com.coditas.frontline.dto.response.TicketResponse;
import com.coditas.frontline.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(ApiPaths.BASE_PATH)
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping(ApiPaths.Ticket.TICKETS)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApplicationResponse<TicketResponse>> raiseTicket(@Valid @RequestBody TicketRequest request){
        TicketResponse response = ticketService.raiseTicket(request);
        URI location = URI.create(ApiPaths.BASE_PATH+ApiPaths.Ticket.TICKETS+"/"+response.getId());
        return ResponseEntity.created(location).body(ApplicationResponse.success(response, "Ticket raised successfully"));
    }

    @PostMapping(ApiPaths.Ticket.TICKETS+ApiPaths.Ticket.TICKET_ID+ApiPaths.Ticket.ASSIGN+ApiPaths.Agent.AGENT_ID)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApplicationResponse<TicketResponse>> assignAgent(@PathVariable(name = "ticket-id") Long ticketId,
                                                                           @PathVariable(name = "agent-id") Long agentId){
        TicketResponse response = ticketService.assignAgent(ticketId, agentId);
        URI location = URI.create(ApiPaths.BASE_PATH+ApiPaths.Ticket.TICKETS+"/"+response.getId());
        return ResponseEntity.created(location).body(ApplicationResponse.success(response, "Ticket assigned to agent successfully"));
    }
}
