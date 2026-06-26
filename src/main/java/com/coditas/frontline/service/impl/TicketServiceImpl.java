package com.coditas.frontline.service.impl;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.dto.request.EmailDetails;
import com.coditas.frontline.dto.request.PriorityRequest;
import com.coditas.frontline.dto.request.RatingsRequest;
import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.TicketResponse;
import com.coditas.frontline.entity.FileData;
import com.coditas.frontline.entity.Ticket;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.enums.Role;
import com.coditas.frontline.enums.TicketStatus;
import com.coditas.frontline.exception.NotFoundException;
import com.coditas.frontline.exception.ResourceAlreadyExistsException;
import com.coditas.frontline.exception.ResourceMismatchedException;
import com.coditas.frontline.mapper.TicketMapper;
import com.coditas.frontline.repository.FileDataRepository;
import com.coditas.frontline.repository.TicketRepository;
import com.coditas.frontline.repository.UserRepository;
import com.coditas.frontline.service.EmailService;
import com.coditas.frontline.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final FileDataRepository fileDataRepository;

    @Override
    @Transactional
    public TicketResponse raiseTicket(MultipartFile file, TicketRequest request) throws IOException {
        if (fileDataRepository.findByName(file.getOriginalFilename()).isPresent()){
            if (file.getOriginalFilename().equals(fileDataRepository.findByName(file.getOriginalFilename()).get().getName())) {
                throw new ResourceAlreadyExistsException(ExceptionMessage.FILE_ALREADY_EXISTS);
            }
        }

        FileData savedFile = fileDataRepository.save(FileData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .data(file.getBytes())
                .build());

        log.info("File content in bytes: {}", savedFile.getData());
        log.info("File content size: {}", savedFile.getData().length);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User customer = (User) authentication.getPrincipal();

        Ticket ticket = ticketMapper.toTicket(request);
        ticket.setCustomer(customer);
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setRaisedAt(Instant.now());

        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket:{} has been persisted", ticket.getId());

        assert customer != null;
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(customer.getEmail())
                .msgBody("Your ticket has been raised with ticket id:"+ticket.getId())
                .subject("Ticket Raised")
                .build();
        emailService.sendSimpleMail(emailDetails);
        log.info("Email sent to recipient:{}", customer.getEmail());
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
        if(ticket.getStatus().equals(TicketStatus.ASSIGNED)){
            ticket.setStatus(TicketStatus.REASSIGNED);
        }else{
            ticket.setStatus(TicketStatus.ASSIGNED);
        }
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Agent:{} has been assigned to ticked:{}", agentId, ticketId);
        String email = ticket.getCustomer().getEmail();
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(email)
                .msgBody("Your ticket with ticket id " + ticketId + " has been assigned to agent " + agent.getName())
                .subject("Ticket Resolving")
                .build();
        emailService.sendSimpleMail(emailDetails);
        log.info("Email for agent assignment has been sent to recipient:{}", email);
        return ticketMapper.toTicketResponse(savedTicket);
    }

    @Override
    public List<TicketResponse> getAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        assert user != null;
        List<Ticket> tickets =  switch (user.getRole()){
            case Role.CUSTOMER -> getCustomerTickets(user);
            case Role.AGENT -> getAgentTickets(user);
            case Role.MANAGER -> getAllTickets();
        };
        log.info("All tickets fetched");
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
        if(user.getRole().equals(Role.MANAGER) || !ticket.getCustomer().getId().equals(user.getId()) || (ticket.getAgent()!=null && !ticket.getAgent().getId().equals(user.getId()))){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AUTHORIZED);
        }
        log.info("Ticket:{} is fetched", ticketId);
        TicketResponse ticketResponse = ticketMapper.toTicketResponse(ticket);
        log.info("Ticket content {}", ticketResponse);
        return ticketResponse;
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
        log.info("Ticket:{} has been resolved", ticketId);
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
        log.info("Ticket:{} has been escalated", ticketId);
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
        log.info("Ticket:{} has been rated", ticketId);
        return ticketMapper.toTicketResponse(savedTicket);
    }

    @Override
    public TicketResponse setPriority(Long ticketId, PriorityRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new NotFoundException(ExceptionMessage.TICKET_NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User user = (User) authentication.getPrincipal();
        assert user != null;
        if(!user.getRole().equals(Role.MANAGER)){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AUTHORIZED);
        }
        ticket.setPriority(request.getPriority());
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Priority for ticket:{} has been set by manager: {}", ticketId, user.getId());
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
