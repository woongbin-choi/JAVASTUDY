package com.developers.dmaker.service;

import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.repository.DeveloperRepository;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class DMakerService {
  private final DeveloperRepository developerRepository;

  @Transactional
  public void createdDeveloper(){
    Developer developer = Developer.builder()
      .developerLevel(DeveloperLevel.JUNGNIOR)
      .developerSkillType(DeveloperSkillType.FRONT_END)
      .experienceYears(2)
      .name("Olaf")
      .age(5)
      .build();

    developerRepository.save(developer);
  }
}
