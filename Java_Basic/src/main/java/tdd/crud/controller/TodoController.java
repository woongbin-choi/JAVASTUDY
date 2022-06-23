package tdd.crud.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import tdd.crud.model.TodoEntity;
import tdd.crud.model.TodoRequest;
import tdd.crud.model.TodoResponse;
import tdd.crud.service.TodoService;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class TodoController {

  private final TodoService service;

  @PostMapping
  public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest request) {
    System.out.println("Create");

    if(ObjectUtils.isEmpty(request.getTitle())){
      return ResponseEntity.badRequest().build();
    }

    if(ObjectUtils.isEmpty(request.getOrder())){
      request.setOrder(0L);
    }

    if(ObjectUtils.isEmpty(request.getCompleted())){
      request.setCompleted(false);
    }

    TodoEntity result = this.service.add(request);
    return ResponseEntity.ok(new TodoResponse(result));
  }

  @GetMapping
  public ResponseEntity<TodoResponse> readOne(@PathVariable Long id) {
    System.out.println("Read One");

    TodoEntity result = this.service.searchById(id);
    return ResponseEntity.ok(new TodoResponse(result));
  }

  @GetMapping
  public ResponseEntity<List<TodoResponse>> readAll() {
    System.out.println("Read All");

    List<TodoEntity> list = this.service.searchAll();
    List<TodoResponse> responses = list.stream().map(TodoResponse::new)
                                                .collect(Collectors.toList());
    return ResponseEntity.ok(responses);
  }

  @PatchMapping("{id}")
  public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request) {
    System.out.println("Update");

    TodoEntity result = this.service.updateById(id, request);
    return ResponseEntity.ok(new TodoResponse(result));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<?> deleteOne(@PathVariable Long id) {
    System.out.println("Delete One");

    this.service.deleteById(id);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<?> deleteAll() {
    System.out.println("Delete All");

    this.service.deleteAll();
    return ResponseEntity.ok().build();
  }
}
