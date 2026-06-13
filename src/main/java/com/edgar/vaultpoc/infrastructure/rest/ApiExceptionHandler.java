package com.edgar.vaultpoc.infrastructure.rest;
import org.springframework.http.*;import org.springframework.web.bind.annotation.*;import java.util.*;
@RestControllerAdvice public class ApiExceptionHandler{
 @ExceptionHandler(Exception.class) ResponseEntity<Map<String,Object>> handle(Exception e){return ResponseEntity.status(500).body(Map.of("error",e.getClass().getSimpleName(),"message",e.getMessage()));}
}
