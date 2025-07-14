package com.jobfinder.finder.dto;

import com.jobfinder.finder.constant.EmploymentType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
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
