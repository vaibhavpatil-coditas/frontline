package com.coditas.frontline.service.impl;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.TicketResponse;
import com.coditas.frontline.entity.Ticket;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.enums.Role;
import com.coditas.frontline.enums.TicketStatus;
import com.coditas.frontline.exception.NotFoundException;
import com.coditas.frontline.exception.ResourceMismatchedException;
import com.coditas.frontline.mapper.TicketMapper;
import com.coditas.frontline.repository.TicketRepository;
import com.coditas.frontline.repository.UserRepository;
import com.coditas.frontline.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.exc.MismatchedInputException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;

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

    @Override
    public TicketResponse assignAgent(Long ticketId, Long agentId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
        User agent = userRepository.findById(agentId).orElseThrow(()->
                new NotFoundException(ExceptionMessage.USER_NOT_FOUND));

        if(!agent.getRole().equals(Role.AGENT)){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AN_AGENT);
        }

        ticket.setAgent(agent);
        ticket.setStatus(TicketStatus.ASSIGNED);
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(savedTicket);
    }
}
