package com.coditas.frontline.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AgentManagerId {
    private Long agentId;
    private Long managerId;
}
