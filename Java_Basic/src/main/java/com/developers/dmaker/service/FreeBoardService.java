package com.developers.dmaker.service;

import com.developers.dmaker.dto.FreeBoardDto;
import com.developers.dmaker.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FreeBoardService {
  private final FreeBoardRepository freeBoardRepository;

  @Transactional
  public void addFreeBoard(FreeBoardDto freeBoardDto){
    // Session Check
    freeBoardRepository.save(freeBoardDto);
    // History Insert
  }

}
