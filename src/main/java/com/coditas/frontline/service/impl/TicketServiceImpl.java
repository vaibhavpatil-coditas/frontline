package com.coditas.frontline.service.impl;

import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.TicketResponse;
import com.coditas.frontline.entity.Ticket;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.enums.TicketStatus;
import com.coditas.frontline.mapper.TicketMapper;
import com.coditas.frontline.repository.TicketRepository;
import com.coditas.frontline.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    @Override
    public TicketResponse raiseTicket(TicketRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User customer = (User) authentication.getPrincipal();

        Ticket ticket = ticketMapper.toTicket(request);
        ticket.setCustomer(customer);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setRaisedAt(Instant.now());

        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(savedTicket);
    }
}
