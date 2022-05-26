package tdd.crud.service;

import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;
import tdd.crud.model.TodoEntity;
import tdd.crud.model.TodoRequest;
import tdd.crud.repository.TodoRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoServiceTest {

  @Mock
  private TodoRepository todoRepository;

  @InjectMocks
  private TodoService todoService;

  @Test
  public void add() {
    when(this.todoRepository.save(any(TodoEntity.class)))
      .then(AdditionalAnswers.returnsFirstArg());

    TodoRequest expected = new TodoRequest();
    expected.setTitle("Test title");

    TodoEntity actual = this.todoService.add(expected);

    assertEquals(expected.getTitle(), actual.getTitle());
  }

  @Test
  public void searchById() {
    TodoEntity entity = new TodoEntity();
    entity.setId(123L);
    entity.setTitle("title");
    entity.setOrder(0L);
    entity.setCompleted(false);
    Optional<TodoEntity> optional = Optional.of(entity);
    given(this.todoRepository.findById(anyLong()))
      .willReturn(optional);

    TodoEntity actual = this.todoService.searchById(123L);

    TodoEntity expected = optional.get();

    assertEquals(expected.getId(), actual.getId());
    assertEquals(expected.getTitle(), actual.getTitle());
    assertEquals(expected.getOrder(), actual.getOrder());
    assertEquals(expected.getCompleted(), actual.getCompleted());
  }
}
