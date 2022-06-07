package com.developers.dmaker.dto;

import com.developers.dmaker.code.StatusCode;
import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import jdk.net.SocketFlow;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperDetailDto {
  private DeveloperLevel developerLevel;
  private DeveloperSkillType developerSkillType;
  private Integer experienceYears;
  private String memberId;
  private StatusCode statusCode;
  private String name;
  private Integer age;

  public static DeveloperDetailDto fromEntity(Developer developer) {
    return DeveloperDetailDto.builder()
      .developerLevel(developer.getDeveloperLevel())
      .developerSkillType(developer.getDeveloperSkillType())
      .experienceYears(developer.getExperienceYears())
      .memberId(developer.getMemberId())
      .statusCode(StatusCode.EMPLOYED)
      .name(developer.getName())
      .age(developer.getAge())
      .build();
  }
}
