package com.developers.dmaker.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Aspect
public class SessionAspect {

  @Pointcut("execution(* com.developers.dmaker.service.FreeBoardService.addFreeBoard(..))")
  public void addFreeBoard(){}

  @Pointcut("execution(* com.developers.dmaker.service.DMakerService.createdDeveloper(..))")
  public void createdDeveloper(){}

//  @Around("addFreeBoard() || createdDeveloper()"){
//    public Object sessionChk()  {
//      try {
//
//      } catch (Exception e){
//
//      }
//      return null;
//    }
//  }

  @Before("@annotation(SessionChk)")
  public Object accessSession(final ProceedingJoinPoint pjp) throws Throwable {
    try {

    } catch (Exception e){

    }
    return null;
  }
}
