/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.muspellheim.todomvc.backend.adapters.TodoMemoryRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.Arrays;
import java.util.Collections;
import lombok.var;
import org.junit.jupiter.api.Test;

public class MessageHandlingTests {
  @Test
  void handleNewTodoCommand() {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new MessageHandling(repository);
    var command = new NewTodoCommand("Foobar");

    var result = handler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(3, repository.load().size(), "Todo added to list");
    assertNotNull(repository.load().get(2).getId(), "Todo id is set");
    assertEquals("Foobar", repository.load().get(2).getTitle(), "Todo title is set");
    assertFalse(repository.load().get(2).isCompleted(), "Todo is not completed");
  }

  @Test
  void handleToggleCommand() {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new MessageHandling(repository);
    var command = new ToggleCommand("d2f7760d-8f03-4cb3-9176-06311cb89993");

    var result = handler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
        repository.load(),
        "Todos updated");
  }

  @Test
  void handleToggleAllCommand() {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new MessageHandling(repository);
    var command = new ToggleAllCommand(true);

    var result = handler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
        repository.load(),
        "Todos updated");
  }

  @Test
  void handleEditCommand() {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new MessageHandling(repository);
    var command = new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar");

    var result = handler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar", false)),
        repository.load(),
        "Todos updated");
  }

  @Test
  void handleDestroyCommand() {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new MessageHandling(repository);
    var command = new DestroyCommand("119e6785-8ffc-42e0-8df6-dbc64881f2b7");

    var result = handler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Collections.singletonList(
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
        repository.load(),
        "Todos updated");
  }

  @Test
  void handleClearCommand() {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new MessageHandling(repository);
    var command = new ClearCompletedCommand();

    var result = handler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Collections.singletonList(
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
        repository.load(),
        "Todos updated");
  }

  @Test
  void handleTodosQuery() {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new MessageHandling(repository);
    var query = new TodosQuery();

    var result = handler.handle(query);

    assertEquals(
        new TodosQueryResult(
            Arrays.asList(
                new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false))),
        result);
  }
}
