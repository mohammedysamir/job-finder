package com.jobfinder.finder.mapper;

import com.jobfinder.finder.constant.SubmissionStatus;
import com.jobfinder.finder.dto.post.PostDto;
import com.jobfinder.finder.dto.submission.SubmissionRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionResponseDto;
import com.jobfinder.finder.entity.PostEntity;
import com.jobfinder.finder.entity.SubmissionEntity;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
  SubmissionEntity toEntity(SubmissionRequestDto dto);

  SubmissionResponseDto toDto(SubmissionEntity submissionEntity);

  @Mapping(target = "submissionDate", source = "date")
  @Mapping(target = "status", source = "status")
  SubmissionResponseDto toResponse(SubmissionRequestDto submissionRequestDto, LocalDate date, SubmissionStatus status);
}
