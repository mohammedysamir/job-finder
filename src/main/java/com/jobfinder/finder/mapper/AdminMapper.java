package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.admin.AdminCreationDto;
import com.jobfinder.finder.dto.admin.AdminResponseDto;
import com.jobfinder.finder.entity.AdminEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
  AdminEntity toEntity(AdminCreationDto adminCreationDto);

  AdminResponseDto toDto(AdminEntity adminEntity);
}
