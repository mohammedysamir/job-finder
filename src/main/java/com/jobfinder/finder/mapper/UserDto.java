package com.jobfinder.finder.mapper;

import com.jobfinder.finder.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper
public interface UserDto {
  UserDto toDto(UserEntity userEntity);

  UserEntity toEntity(UserDto userDto);
}
