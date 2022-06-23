package com.developers.dmaker.controller;

import com.developers.dmaker.dto.DeveloperDto;
import com.developers.dmaker.service.DMakerService;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;

@WebMvcTest(DMakerController.class)
public class DMakerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DMakerService dMakerService;

  @Test
  void getAllDevelopers() throws Exception {
    DeveloperDto seniorDeveloper = DeveloperDto.builder()
      .developerSkillType(DeveloperSkillType.FRONT_END)
      .developerLevel(DeveloperLevel.SENIOR)
      .memberId("test1")
      .build();

    DeveloperDto juniorDeveloper = DeveloperDto.builder()
      .developerSkillType(DeveloperSkillType.FRONT_END)
      .developerLevel(DeveloperLevel.SENIOR)
      .memberId("test2")
      .build();

    given(dMakerService.getAllEmployedDevelopers())
      .willReturn(Arrays.asList(juniorDeveloper, seniorDeveloper));

  }

}
