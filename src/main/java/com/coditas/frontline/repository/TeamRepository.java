package com.coditas.frontline.repository;

import com.coditas.frontline.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
