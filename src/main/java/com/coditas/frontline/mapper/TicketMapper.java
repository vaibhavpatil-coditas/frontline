package com.coditas.frontline.mapper;

import com.coditas.frontline.dto.request.TicketRequest;
import com.coditas.frontline.dto.response.TicketResponse;
import com.coditas.frontline.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    Ticket toTicket(TicketRequest request);

    TicketResponse toTicketResponse(Ticket savedTicket);
}
