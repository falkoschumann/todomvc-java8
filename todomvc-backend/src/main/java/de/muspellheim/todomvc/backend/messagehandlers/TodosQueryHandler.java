/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.io.IOException;
import java.util.Collections;
import lombok.NonNull;
import lombok.var;

public class TodosQueryHandler {
  private final TodoRepository repository;

  public TodosQueryHandler(TodoRepository repository) {
    this.repository = repository;
  }

  public TodosQueryResult handle(@NonNull TodosQuery query) {
    try {
      var todos = repository.load();
      return new TodosQueryResult(todos);
    } catch (IOException e) {
      System.err.println(e);
      return new TodosQueryResult(Collections.emptyList());
    }
  }
}
