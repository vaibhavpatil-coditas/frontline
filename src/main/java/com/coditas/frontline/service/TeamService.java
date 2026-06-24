package com.coditas.frontline.service;

import com.coditas.frontline.dto.request.AgentIdRequest;
import com.coditas.frontline.dto.response.TeamResponse;

public interface TeamService {
    TeamResponse create();

    TeamResponse addAgents(Long teamId, AgentIdRequest request);
}
