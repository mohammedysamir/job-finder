package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.UserPatchDto;
import com.jobfinder.finder.dto.UserRegistrationDto;
import com.jobfinder.finder.dto.UserResponseDto;
import com.jobfinder.finder.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
  UserResponseDto toDto(UserEntity userEntity);

  UserEntity toEntity(UserRegistrationDto userDto);

  UserEntity toEntity(UserPatchDto userDto);
}
