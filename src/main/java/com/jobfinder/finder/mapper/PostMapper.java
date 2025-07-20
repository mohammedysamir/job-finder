package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.PostDto;
import com.jobfinder.finder.entity.PostEntity;
import org.mapstruct.Mapper;

@Mapper
public interface PostMapper {
  PostEntity toEntity(PostDto postDto);
  PostDto toDto(PostEntity postEntity);
}
