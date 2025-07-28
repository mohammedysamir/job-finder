package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.user.UserPatchDto;
import com.jobfinder.finder.dto.user.UserRegistrationDto;
import com.jobfinder.finder.dto.user.UserResponseDto;
import com.jobfinder.finder.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
  UserResponseDto toDto(UserEntity userEntity);

  @Mapping(target = "role", expression = "java(Roles.valueOf(userDto.getRole().toUpperCase()))")
  UserEntity toEntity(UserRegistrationDto userDto);

  UserEntity toEntity(UserPatchDto userDto);
}
