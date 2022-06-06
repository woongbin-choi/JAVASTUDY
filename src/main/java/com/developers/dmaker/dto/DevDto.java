package com.developers.dmaker.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Slf4j
public class DevDto {
  String name;
  Integer age;
  LocalDateTime startAt;
}
