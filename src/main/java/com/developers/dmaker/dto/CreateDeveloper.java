package com.developers.dmaker.dto;

import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class CreateDeveloper {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  @ToString
  public static class Request{
    @NotNull
    private DeveloperLevel developerLevel;
    @NotNull
    private DeveloperSkillType developerSkillType;
    @NotNull
    @Min(0)
    @Max(20)

    private Integer experienceYears;

    @NotNull
    @Size(min = 3, max = 50, message="memberId size must 3~50")
    private String memberId;
    @NotNull
    @Size(min = 3, max = 20, message="name size must 3~20")
    private String name;
    @Min(18)
    private Integer age;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response{
    private DeveloperLevel developerLevel;
    private DeveloperSkillType developerSkillType;
    private Integer experienceYears;

    private String memberId;
    private String name;
    private Integer age;
  }
}
