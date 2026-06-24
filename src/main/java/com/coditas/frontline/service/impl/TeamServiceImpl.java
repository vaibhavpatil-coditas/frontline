package com.coditas.frontline.service.impl;

import com.coditas.frontline.constants.ExceptionMessage;
import com.coditas.frontline.dto.request.AgentIdRequest;
import com.coditas.frontline.dto.response.TeamResponse;
import com.coditas.frontline.entity.Team;
import com.coditas.frontline.entity.TeamAgent;
import com.coditas.frontline.entity.TeamAgentId;
import com.coditas.frontline.entity.User;
import com.coditas.frontline.enums.Role;
import com.coditas.frontline.exception.NotFoundException;
import com.coditas.frontline.exception.ResourceMismatchedException;
import com.coditas.frontline.mapper.TeamAgentMapper;
import com.coditas.frontline.mapper.TeamMapper;
import com.coditas.frontline.repository.TeamAgentRepository;
import com.coditas.frontline.repository.TeamRepository;
import com.coditas.frontline.repository.UserRepository;
import com.coditas.frontline.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;
    private final TeamAgentMapper teamAgentMapper;
    private final TeamAgentRepository teamAgentRepository;

    @Override
    public TeamResponse create() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        User manager = (User)authentication.getPrincipal();
        Team team = Team.builder()
                .manager(manager)
                .teamSize(0L)
                .createdAt(Instant.now())
                .build();
        Team savedTeam = teamRepository.save(team);
        return teamMapper.toTeamResponse(savedTeam);
    }

    @Override
    @Transactional
    public TeamResponse addAgents(Long teamId, AgentIdRequest request) {
        Team team = teamRepository.findById(teamId).orElseThrow(()->
                new NotFoundException(ExceptionMessage.TEAM_NOT_FOUND));
        User agent = userRepository.findById(request.getMemberId()).orElseThrow(()->
                new NotFoundException(ExceptionMessage.USER_NOT_FOUND));
        if(!agent.getRole().equals(Role.AGENT)){
            throw new ResourceMismatchedException(ExceptionMessage.NOT_AN_AGENT);
        }
        TeamAgent teamAgent = TeamAgent.builder()
                .id(new TeamAgentId(teamId, request.getMemberId()))
                .agent(agent)
                .team(team)
                .build();
        teamAgentRepository.save(teamAgent);
        team.setTeamSize(team.getTeamSize()+1);
        Team savedTeam = teamRepository.save(team);
        return teamMapper.toTeamResponse(savedTeam);
    }
}
