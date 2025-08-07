package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.user.UserPatchDto;
import com.jobfinder.finder.dto.user.UserRegistrationDto;
import com.jobfinder.finder.dto.user.UserResponseDto;
import com.jobfinder.finder.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {SubmissionMapper.class, AddressMapper.class})
public interface UserMapper {
  UserResponseDto toDto(UserEntity userEntity);

  @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
  UserEntity toEntity(UserRegistrationDto userDto);

  UserEntity toEntity(UserPatchDto userDto);

  @Named("mapRole")
  default String mapRole(String role) {
    return role != null ? role.toUpperCase() : null;
  }
}
