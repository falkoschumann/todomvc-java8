/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.adapters;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import java.util.Collections;
import java.util.List;

public class TodoMemoryRepository implements TodoRepository {
  private List<Todo> todos = Collections.emptyList();

  @Override
  public List<Todo> load() {
    return todos;
  }

  @Override
  public void store(List<Todo> todos) {
    this.todos = todos;
  }
}
