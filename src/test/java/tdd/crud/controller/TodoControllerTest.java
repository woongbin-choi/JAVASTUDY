package tdd.crud.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tdd.crud.model.TodoEntity;
import tdd.crud.model.TodoRequest;
import tdd.crud.service.TodoService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

  @Autowired
  MockMvc mvc;

  @MockBean
  TodoService todoService;

  private TodoEntity expected;

  @Before
  void setUp() {
    this.expected = new TodoEntity();
    this.expected.setId(123L);
    this.expected.setTitle("test");
    this.expected.setOrder(0L);
    this.expected.setCompleted(false);
  }
}
