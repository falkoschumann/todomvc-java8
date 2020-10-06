/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQueryResult;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MessageHandlerTests {
  private TestingTodoRepository repository;
  private MessageHandler messageHandler;

  @BeforeEach
  void setUp() {
    repository = new TestingTodoRepository();
    messageHandler = new MessageHandler(repository);
  }

  @Test
  void newTodo() {
    val command = new NewTodoCommand("Foobar");

    val result = messageHandler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(3, repository.todos.size(), "Todo added to list");
    assertNotNull(repository.todos.get(2).getId(), "Todo id is set");
    assertEquals("Foobar", repository.todos.get(2).getTitle(), "Todo title is set");
    assertFalse(repository.todos.get(2).isCompleted(), "Todo is not completed");
  }

  @Test
  void toggleAll() {
    val command = new ToggleAllCommand(true);

    val result = messageHandler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
        repository.todos,
        "Todos updated");
  }

  @Test
  void toggle() {
    val command = new ToggleCommand("d2f7760d-8f03-4cb3-9176-06311cb89993");

    val result = messageHandler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
        repository.todos,
        "Todos updated");
  }

  @Test
  void destroy() {
    val command = new DestroyCommand("119e6785-8ffc-42e0-8df6-dbc64881f2b7");

    val result = messageHandler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Collections.singletonList(
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
        repository.todos,
        "Todos updated");
  }

  @Test
  void edit() {
    val command = new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar");

    val result = messageHandler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar", false)),
        repository.todos,
        "Todos updated");
  }

  @Test
  void clearCompleted() {
    val command = new ClearCompletedCommand();

    val result = messageHandler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Collections.singletonList(
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
        repository.todos,
        "Todos updated");
  }

  @Test
  void queryTodoList() {
    val query = new TodoListQuery();

    val result = messageHandler.handle(query);

    assertEquals(
        new TodoListQueryResult(
            Arrays.asList(
                new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false))),
        result);
  }

  private static class TestingTodoRepository implements TodoRepository {
    List<Todo> todos =
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false));

    @Override
    public List<Todo> load() {
      return todos;
    }

    @Override
    public void store(List<Todo> todos) {
      this.todos = todos;
    }
  }
}
