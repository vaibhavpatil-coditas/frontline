package com.coditas.frontline.controller;

import com.coditas.frontline.constants.ApiPaths;
import com.coditas.frontline.dto.request.AgentIdRequest;
import com.coditas.frontline.dto.response.ApplicationResponse;
import com.coditas.frontline.dto.response.TeamResponse;
import com.coditas.frontline.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(ApiPaths.BASE_PATH)
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping(ApiPaths.Team.TEAMS)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApplicationResponse<TeamResponse>> create(){
        TeamResponse response = teamService.create();
        URI location = URI.create(ApiPaths.BASE_PATH+ApiPaths.Team.TEAMS+"/"+response.getId());
        return ResponseEntity.created(location).body(ApplicationResponse.success(response, "Team created successfully"));
    }

    @PostMapping(ApiPaths.Team.TEAMS+ApiPaths.Team.TEAM_ID+ApiPaths.Team.ADD_MEMBER)
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ApplicationResponse<TeamResponse>> addAgents(@PathVariable(name = "team-id") Long teamId,
                                                                       @RequestBody AgentIdRequest request){
        TeamResponse response = teamService.addAgents(teamId, request);
        URI location = URI.create(ApiPaths.BASE_PATH+ApiPaths.Team.TEAMS+"/"+response.getId());
        return ResponseEntity.created(location).body(ApplicationResponse.success(response, "Team member added successfully"));
    }
}
