package com.coditas.frontline.service.impl;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.dto.request.EmailDetails;
import com.coditas.frontline.dto.request.PriorityRequest;
import com.coditas.frontline.dto.request.RatingsRequest;
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
import com.coditas.frontline.service.EmailService;
import com.coditas.frontline.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;

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

        assert customer != null;
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(customer.getEmail())
                .msgBody("Your ticket has been raised with ticket id:"+ticket.getId())
                .subject("Ticket Raised")
                .build();
        emailService.sendSimpleMail(emailDetails);

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
        if(ticket.getAgent().equals(TicketStatus.ASSIGNED)){
            ticket.setStatus(TicketStatus.REASSIGNED);
        }else{
            ticket.setStatus(TicketStatus.ASSIGNED);
        }
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(savedTicket);
    }

    @Override
    public List<TicketResponse> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        List<Ticket> tickets =  switch (user.getRole()){
            case Role.CUSTOMER -> getCustomerTickets(user);
            case Role.AGENT -> getAgentTickets(user);
            case Role.MANAGER -> getAllTickets();
        };
        return ticketMapper.toTicketResponseList(tickets);
    }

    @Override
    public TicketResponse getTicketById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User)authentication.getPrincipal();
        assert user != null;
        if(user.getRole().equals(Role.MANAGER) || !(ticket.getAgent().equals(user)||ticket.getCustomer().equals(user))){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AUTHORIZED);
        }
        return ticketMapper.toTicketResponse(ticket);
    }

    @Override
    public TicketResponse resolveTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        if(!ticket.getAgent().equals(user)){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AUTHORIZED);
        }
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(savedTicket);
    }

    @Override
    public TicketResponse escalateTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        if(!ticket.getAgent().equals(user)){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AUTHORIZED);
        }
        ticket.setStatus(TicketStatus.ESCALATED);
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(savedTicket);
    }


    @Override
    public TicketResponse rate(Long ticketId, RatingsRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        if(!ticket.getCustomer().equals(user)){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AUTHORIZED);
        }
        ticket.setRatings(request.getRatings());
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(savedTicket);
    }

    @Override
    public TicketResponse setPriority(Long ticketId, PriorityRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        if(!user.getRole().equals(Role.MANAGER)){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AUTHORIZED);
        }
        ticket.setPriority(request.getPriority());
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toTicketResponse(savedTicket);
    }

    private List<Ticket> getAgentTickets(User agent) {
        return ticketRepository.findByAgent(agent).orElseThrow(()->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
    }

    public List<Ticket> getCustomerTickets(User customer) {
        return ticketRepository.findByCustomer(customer).orElseThrow(()->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }
}
