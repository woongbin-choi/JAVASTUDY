package com.developers.dmaker.service;

import com.developers.dmaker.code.StatusCode;
import com.developers.dmaker.dto.CreateDeveloper;
import com.developers.dmaker.dto.DeveloperDetailDto;
import com.developers.dmaker.dto.DeveloperDto;
import com.developers.dmaker.dto.EditDeveloper;
import com.developers.dmaker.entity.Developer;
import com.developers.dmaker.entity.RetiredDeveloper;
import com.developers.dmaker.exception.DMakerErrorCode;
import com.developers.dmaker.exception.DMakerException;
import com.developers.dmaker.repository.DeveloperRepository;
import com.developers.dmaker.repository.RetiredDeveloperRepository;
import com.developers.dmaker.type.DeveloperLevel;
import com.developers.dmaker.type.DeveloperSkillType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.developers.dmaker.exception.DMakerErrorCode.*;

@Service
@RequiredArgsConstructor
public class DMakerService {
  private final DeveloperRepository developerRepository;
  private final RetiredDeveloperRepository retiredDeveloperRepository;

  public List<DeveloperDto> getAllEmployedDevelopers() {
    return developerRepository.findDevelopersByStatusCodeEquals(StatusCode.EMPLOYED)
      .stream().map(DeveloperDto::fromEntity)
      .collect(Collectors.toList());
  }

  public DeveloperDetailDto getDeveloperDetail(String memberId) {
    return developerRepository.findByMemberId(memberId)
      .map(DeveloperDetailDto::fromEntity)
      .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
  }

  // ACID
  // Atomic : 원자성 - A 계좌에서 만원 빠져나갔는데, 정전이 나서 B의 계좌에 안들어갈 경우 실패
  // Consistency : 일관성 - 트랜잭션이 끝나는 시점에는 일관성이 무조건 맞춰있어야 한다(중간 부분은 일관성을 무시해도 됨)
  // Isolation : 고립성
  // Durability : 지속성 - 모든 커밋 된 이력을 남긴다
  @Transactional
  public CreateDeveloper.Response createdDeveloper(CreateDeveloper.Request request){
    validateCreateDeveloperRequest(request);

    Developer developer = Developer.builder()
      .developerLevel(request.getDeveloperLevel())
      .developerSkillType(request.getDeveloperSkillType())
      .experienceYears(request.getExperienceYears())
      .memberId(request.getMemberId())
      .statusCode(StatusCode.EMPLOYED)
      .name(request.getName())
      .age(request.getAge())
      .build();

    developerRepository.save(developer);
    return CreateDeveloper.Response.fromEntity(developer);
  }

  private void validateCreateDeveloperRequest(CreateDeveloper.Request request) {
    // business validation
    validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());

    developerRepository.findByMemberId(request.getMemberId())
      .ifPresent((developer -> {
        throw new DMakerException(DUPLICATED_MEMBER_ID);
      }));
//    if(developer.isPresent()) throw new DMakerException(DUPLICATED_MEMBER_ID);

    throw new ArrayIndexOutOfBoundsException();
  }

  @Transactional
  public DeveloperDetailDto editDeveloper(EditDeveloper.Request request, String memberId) {
    validateEidtDeveloperRequest(request, memberId);

    Developer developer = developerRepository.findByMemberId(memberId).orElseThrow(() -> new DMakerException(NO_DEVELOPER));

    developer.setDeveloperLevel(request.getDeveloperLevel());
    developer.setDeveloperSkillType(request.getDeveloperSkillType());
    developer.setExperienceYears(request.getExperienceYears());

    return DeveloperDetailDto.fromEntity(developer);
  }

  private void validateDeveloperLevel(DeveloperLevel developerLevel, Integer experienceYears) {
    if (developerLevel == DeveloperLevel.SENIOR && experienceYears < 10) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }

    if (developerLevel == DeveloperLevel.JUNGNIOR && (
      experienceYears < 4 || experienceYears > 10)) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }

    if (developerLevel == DeveloperLevel.JUNIOR && experienceYears > 4) {
      throw new DMakerException(LEVEL_EXPERIENCE_YEARS_NOT_MATCHED);
    }
  }

  private void validateEidtDeveloperRequest(EditDeveloper.Request request, String memberId) {
    validateDeveloperLevel(request.getDeveloperLevel(), request.getExperienceYears());
  }

  @Transactional
  public DeveloperDetailDto deleteDeveloper(String memberId) {
    // 1. EMPLOYED -> RETIRED
    Developer developer = developerRepository.findByMemberId(memberId)
      .orElseThrow(() -> new DMakerException(NO_DEVELOPER));
    developer.setStatusCode(StatusCode.RETIRED);

    // 2. save into RetiredDeveloper
    RetiredDeveloper retiredDeveloper = RetiredDeveloper.builder()
      .memberId(memberId)
      .name(developer.getName())
      .build();

    retiredDeveloperRepository.save(retiredDeveloper);
    return DeveloperDetailDto.fromEntity(developer);
  }
}
