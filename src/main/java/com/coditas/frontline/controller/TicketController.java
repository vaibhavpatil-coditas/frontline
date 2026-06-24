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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
