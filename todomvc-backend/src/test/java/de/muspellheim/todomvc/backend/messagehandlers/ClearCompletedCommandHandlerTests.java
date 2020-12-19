/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todomvc.backend.adapters.TodoMemoryRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.Success;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import java.util.Arrays;
import java.util.Collections;
import lombok.var;
import org.junit.jupiter.api.Test;

public class ClearCompletedCommandHandlerTests {
  @Test
  void handleClearCompletedCommand() {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new ClearCompletedCommandHandler(repository);
    var command = new ClearCompletedCommand();

    var result = handler.handle(command);

    assertEquals(new Success(), result, "Command handled successfully");
    assertEquals(
        Collections.singletonList(
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
        repository.load(),
        "Todos updated");
  }
}
