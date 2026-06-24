package com.coditas.frontline.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "team_agent", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamAgent {
    @EmbeddedId
    private TeamAgentId id;

    @ManyToOne
    @MapsId("agentId")
    @JoinColumn(name = "agent_id")
    private User agent;

    @ManyToOne
    @MapsId("teamId")
    @JoinColumn(name = "team_id")
    private Team team;
}
