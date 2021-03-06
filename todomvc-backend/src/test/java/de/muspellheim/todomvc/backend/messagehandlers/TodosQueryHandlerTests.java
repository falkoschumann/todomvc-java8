/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todomvc.backend.adapters.MemoryTodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.Arrays;
import lombok.var;
import org.junit.jupiter.api.Test;

public class TodosQueryHandlerTests {
  @Test
  void handleTodosQuery() {
    var repository = new MemoryTodoRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var handler = new TodosQueryHandler(repository);

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
