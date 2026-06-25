package com.coditas.frontline.repository;

import com.coditas.frontline.entity.Ticket;
import com.coditas.frontline.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<List<Ticket>> findByCustomer(User customer);

    Optional<List<Ticket>> findByAgent(User agent);
}
