package com.coditas.frontline.mapper;

import com.coditas.frontline.dto.response.TeamResponse;
import com.coditas.frontline.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    @Mapping(source = "manager.id", target = "managerId")
    TeamResponse toTeamResponse(Team savedTeam);
}
