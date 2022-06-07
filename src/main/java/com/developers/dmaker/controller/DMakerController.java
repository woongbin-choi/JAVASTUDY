package com.developers.dmaker.controller;

import com.developers.dmaker.dto.*;
import com.developers.dmaker.exception.DMakerException;
import com.developers.dmaker.service.DMakerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DMakerController {

  private final DMakerService dMakerService;

  @GetMapping("/developers")
  public List<DeveloperDto> getAllDevelopers() {
    return dMakerService.getAllEmployedDevelopers();
  }

  @GetMapping("/developers/{memberId}")
  public DeveloperDetailDto getDeveloperDetail(@PathVariable String memberId) {
    return dMakerService.getDeveloperDetail(memberId);
  }

  @PostMapping("/create-developer")
  public CreateDeveloper.Response createDevelopers(
   @Valid @RequestBody CreateDeveloper.Request request
    ) {
    log.info("reqeust : {}", request);
    return dMakerService.createdDeveloper(request);
  }

  @PutMapping("/developers/{memberId}")
  public DeveloperDetailDto editDeveloper(@PathVariable String memberId,
                                          @Valid @RequestBody EditDeveloper.Request request){
    return dMakerService.editDeveloper(request,memberId);
  }

  @DeleteMapping("/developers/{memberId}")
  public DeveloperDetailDto deleteDeveloper(@PathVariable String memberId){
    return dMakerService.deleteDeveloper(memberId);
  }

  @ExceptionHandler(DMakerException.class)
  public DMakerErrorResponse handleException (DMakerException e, HttpServletRequest request) {
    log.error("errorCode: {}, url: {}, message: {}",
      e.getDMakerErrorCode(), request.getRequestURL(), e.getDetailMessage());

    return DMakerErrorResponse.builder()
      .errorCode(e.getDMakerErrorCode())
      .errorMessage(e.getDetailMessage())
      .build();
  }
}
