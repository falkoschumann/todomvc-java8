/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQueryResult;
import java.io.IOException;
import java.util.Collections;
import lombok.NonNull;
import lombok.var;

public class TodoListQueryHandler {
  private final TodoRepository repository;

  public TodoListQueryHandler(TodoRepository repository) {
    this.repository = repository;
  }

  public TodoListQueryResult handle(@NonNull TodoListQuery query) {
    try {
      var todos = repository.load();
      return new TodoListQueryResult(todos);
    } catch (IOException e) {
      System.err.println(e);
      return new TodoListQueryResult(Collections.emptyList());
    }
  }
}
