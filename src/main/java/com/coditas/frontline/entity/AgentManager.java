package com.coditas.frontline.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "agent_manager", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentManager {
    @EmbeddedId
    private AgentManagerId id;

    @MapsId("agentId")
    @ManyToOne
    @JoinColumn(name = "agent_id")
    private User agent;

    @MapsId("managerId")
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
}
