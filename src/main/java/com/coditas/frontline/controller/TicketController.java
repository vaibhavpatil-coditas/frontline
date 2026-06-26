package com.coditas.frontline.controller;

import com.coditas.frontline.constants.ApiPaths;
import com.coditas.frontline.dto.request.PriorityRequest;
import com.coditas.frontline.dto.request.RatingsRequest;
import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.ApplicationResponse;
import com.coditas.frontline.dto.response.TicketResponse;
import com.coditas.frontline.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(ApiPaths.BASE_PATH)
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping(value = ApiPaths.Ticket.TICKETS)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApplicationResponse<TicketResponse>> raiseTicket(@Valid @RequestPart("data") TicketRequest request,
                                                                           @RequestPart("file") MultipartFile file) throws IOException {
        TicketResponse response = ticketService.raiseTicket(file, request);
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

    @GetMapping(ApiPaths.Ticket.TICKETS)
    public ResponseEntity<ApplicationResponse<List<TicketResponse>>> getAllTickets(){
        List<TicketResponse> response = ticketService.getAll();
        return ResponseEntity.ok(ApplicationResponse.success(response, "Fetched all tickets"));
    }

    @GetMapping(ApiPaths.Ticket.TICKETS+ApiPaths.Ticket.TICKET_ID)
    public ResponseEntity<ByteArrayResource> getTicketById(@PathVariable(name = "ticket-id") Long ticketId){
        TicketResponse response = ticketService.getTicketById(ticketId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(response.getFile().getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;      filename = " + response.getFile().getName())
                .body(new ByteArrayResource((response.getFile().getData())));
    }

    @PatchMapping(ApiPaths.Ticket.TICKETS+ApiPaths.Ticket.TICKET_ID+ApiPaths.Ticket.RESOLVE)
    @PreAuthorize("hasRole('AGENT')")
    public ResponseEntity<ApplicationResponse<TicketResponse>> resolveTicket(@PathVariable(name = "ticket-id") Long ticketId){
        TicketResponse response = ticketService.resolveTicket(ticketId);
        return ResponseEntity.ok(ApplicationResponse.success(response, "Resolved the ticket"));
    }

    @PatchMapping(ApiPaths.Ticket.TICKETS+ApiPaths.Ticket.TICKET_ID+ApiPaths.Ticket.ESCALATE)
    @PreAuthorize("hasAnyRole('AGENT','MANAGER')")
    public ResponseEntity<ApplicationResponse<TicketResponse>> escalateTicket(@PathVariable(name = "ticket-id") Long ticketId){
        TicketResponse response = ticketService.escalateTicket(ticketId);
        return ResponseEntity.ok(ApplicationResponse.success(response, "Ticket escalated"));
    }

    @PatchMapping(ApiPaths.Ticket.TICKETS+ApiPaths.Ticket.TICKET_ID+ApiPaths.Ticket.RATE)
    @PreAuthorize("hasAnyRole('CUSTOMER')")
    public ResponseEntity<ApplicationResponse<TicketResponse>> rateTicket(@PathVariable(name = "ticket-id") Long ticketId,
                                                                          @RequestBody RatingsRequest request){
        TicketResponse response = ticketService.rate(ticketId, request);
        return ResponseEntity.ok(ApplicationResponse.success(response, "Ticket rated"));
    }

    @PatchMapping(ApiPaths.Ticket.TICKETS+ApiPaths.Ticket.TICKET_ID+ApiPaths.Ticket.PRIORITY)
    @PreAuthorize("hasAnyRole('MANAGER')")
    public ResponseEntity<ApplicationResponse<TicketResponse>> setPriority(@PathVariable(name = "ticket-id") Long ticketId,
                                                                           @RequestBody PriorityRequest request){
        TicketResponse response = ticketService.setPriority(ticketId, request);
        return ResponseEntity.ok(ApplicationResponse.success(response, "Ticket priority set"));
    }
}