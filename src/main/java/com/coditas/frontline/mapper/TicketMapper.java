package com.coditas.frontline.mapper;

import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.TicketResponse;
import com.coditas.frontline.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring",uses = UserMapper.class)
public interface TicketMapper {
    Ticket toTicket(TicketRequest request);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "agent.id", target = "agentId")
    TicketResponse toTicketResponse(Ticket savedTicket);

    List<TicketResponse> toTicketResponseList(List<Ticket> tickets);
}
