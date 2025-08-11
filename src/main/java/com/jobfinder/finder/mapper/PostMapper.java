package com.jobfinder.finder.mapper;

import com.jobfinder.finder.dto.post.PostCreationDto;
import com.jobfinder.finder.dto.post.PostResponseDto;
import com.jobfinder.finder.dto.post.PostUpdateDto;
import com.jobfinder.finder.entity.PostEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {
  PostEntity toEntity(PostResponseDto postResponseDto);

  PostEntity toEntity(PostCreationDto postDto);
  PostEntity toEntity(PostUpdateDto postDto);

  PostResponseDto toDto(PostEntity postEntity);

  PostResponseDto toPostDto(PostCreationDto creationDto);
}
