package com.coditas.frontline.dto.response;

import com.coditas.frontline.entity.FileData;
import com.coditas.frontline.enums.TicketCategory;
import com.coditas.frontline.enums.TicketStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;

@Builder
@Getter @Setter
public class TicketResponse {
    private Long id;
    private Long customerId;
    private Long agentId;
    private String description;
    private TicketCategory category;
    private TicketStatus status;
    private Instant raisedAt;
    private FileData file;
}
