package com.developers.dmaker.controller;

import com.developers.dmaker.aspect.SessionChk;
import com.developers.dmaker.dto.*;
import com.developers.dmaker.service.DMakerService;
import com.developers.dmaker.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FreeBoardController {

  private final FreeBoardService freeBoardService;

  @SessionChk
  @PostMapping("/add/freeBoard")
  public void addFreeBoard(@RequestBody FreeBoardDto freeBoardDto){
    freeBoardService.addFreeBoard(freeBoardDto);
  }

}
