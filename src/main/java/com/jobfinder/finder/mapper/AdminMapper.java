package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.admin.AdminCreationDto;
import com.jobfinder.finder.dto.admin.AdminPatchDto;
import com.jobfinder.finder.dto.admin.AdminResponseDto;
import com.jobfinder.finder.entity.AdminEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
  AdminEntity toEntity(AdminCreationDto adminCreationDto);
  AdminEntity toEntity(AdminPatchDto adminPatchDto);

  AdminResponseDto toDto(AdminEntity adminEntity);
  AdminResponseDto toResponse(AdminCreationDto adminCreationDto);
}
