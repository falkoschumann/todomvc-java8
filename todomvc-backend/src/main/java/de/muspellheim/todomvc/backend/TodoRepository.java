/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend;

import de.muspellheim.todomvc.contract.data.Todo;
import java.util.List;

public interface TodoRepository {
  List<Todo> load() throws Exception;

  void store(List<Todo> todos) throws Exception;
}
