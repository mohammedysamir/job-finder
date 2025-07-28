package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.post.PostDto;
import com.jobfinder.finder.entity.PostEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
  PostEntity toEntity(PostDto postDto);
  PostDto toDto(PostEntity postEntity);
}
