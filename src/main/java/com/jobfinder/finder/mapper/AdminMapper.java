package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.AdminCreationDto;
import com.jobfinder.finder.dto.AdminResponseDto;
import com.jobfinder.finder.entity.AdminEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AdminMapper {
  AdminEntity toEntity(AdminCreationDto adminCreationDto);

  AdminResponseDto toDto(AdminEntity adminEntity);
}
