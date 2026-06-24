package com.coditas.frontline.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "teams", schema = "public")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(name = "team_size")
    private Long teamSize;

    @Column(name = "created_at")
    private Instant createdAt;
}
