package com.jobfinder.finder.mapper;

import com.jobfinder.finder.constant.SubmissionStatus;
import com.jobfinder.finder.dto.submission.SubmissionRequestDto;
import com.jobfinder.finder.dto.submission.SubmissionResponseDto;
import com.jobfinder.finder.entity.SubmissionEntity;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubmissionMapper {
  @Mapping(target = "submissionDate", expression = "java(java.time.LocalDate.now())")
  @Mapping(target = "status", expression = "java(com.jobfinder.finder.constant.SubmissionStatus.SUBMITTED)")
  SubmissionEntity toEntity(SubmissionRequestDto dto);

  SubmissionResponseDto toDto(SubmissionEntity submissionEntity);

  @Mapping(target = "submissionDate", source = "date")
  @Mapping(target = "status", source = "status")
  SubmissionResponseDto toResponse(SubmissionRequestDto submissionRequestDto, LocalDate date, SubmissionStatus status);
}
