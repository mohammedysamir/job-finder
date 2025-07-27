package com.jobfinder.finder.dto.post;

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

  @Override
  public String toString() {
    //concat all fields with '&' as separator
    return
            "companyName=" + companyName + '&' +
            "title=" + title + '&' +
            "location=" + location + '&' +
            "employmentType=" + employmentType + '&' +
            "minExperience=" + minExperience + '&' +
            "maxExperience=" + maxExperience+ '&' +
            "recruiterUsername=" + recruiterUsername + '&' +
            "skillsRequired=" + skillsRequired;
  }
}
