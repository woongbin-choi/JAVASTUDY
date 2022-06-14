package com.developers.dmaker.service;

import com.developers.dmaker.dto.CreateDeveloper;
import com.developers.dmaker.dto.DeveloperDetailDto;
import com.developers.dmaker.dto.DeveloperDto;
import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.repository.DeveloperRepository;
import com.developers.dmaker.repository.RetiredDeveloperRepository;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc
public class DMakerServiceTest {
  @Mock
  private DeveloperRepository developerRepository;

  @Mock
  private RetiredDeveloperRepository retiredDeveloperRepository;

  @InjectMocks
  private DMakerService dMakerService;

  @Test
  public void testSomething() {
    dMakerService.createdDeveloper(CreateDeveloper.Request.builder()
      .developerLevel(DeveloperLevel.SENIOR)
      .developerSkillType(DeveloperSkillType.FRONT_END)
      .experienceYears(12)
      .memberId("memberId")
      .name("test")
      .age(32)
      .build());
    List<DeveloperDto> allEmployedDevelopers = dMakerService.getAllEmployedDevelopers();
    System.out.println(allEmployedDevelopers);
  }

  @Test
  public void test() {
    given(developerRepository.findByMemberId(anyString()))
      .willReturn(Optional.of(Developer.builder()
      .developerLevel(DeveloperLevel.SENIOR)
      .developerSkillType(DeveloperSkillType.FRONT_END)
      .experienceYears(12)
      .name("test")
      .age(20)
      .build()));

    DeveloperDetailDto developerDetail = dMakerService.getDeveloperDetail("memberId");

    assertEquals(DeveloperLevel.SENIOR, developerDetail.getDeveloperLevel());
    assertEquals(DeveloperSkillType.FRONT_END, developerDetail.getDeveloperLevel());
  }

}
