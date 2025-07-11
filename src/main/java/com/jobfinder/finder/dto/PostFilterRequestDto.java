package com.jobfinder.finder.dto;

import com.jobfinder.finder.constant.EmploymentType;
import java.util.List;
import lombok.Getter;

@Getter
public class PostFilterRequestDto {
  String companyName;
  String title;
  String location;
  EmploymentType employmentType;
  int minExperience;
  int maxExperience;
  String recruiterUsername;
  List<String> skillsRequired;
}
