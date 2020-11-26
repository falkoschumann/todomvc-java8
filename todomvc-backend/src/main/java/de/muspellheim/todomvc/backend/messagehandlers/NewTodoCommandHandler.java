/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.Failure;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import java.io.IOException;
import java.util.ArrayList;
import lombok.NonNull;
import lombok.var;

public class NewTodoCommandHandler {
  private final TodoRepository repository;

  public NewTodoCommandHandler(TodoRepository repository) {
    this.repository = repository;
  }

  public CommandStatus handle(@NonNull NewTodoCommand command) {
    try {
      var todos = new ArrayList<>(repository.load());
      todos.add(Todo.of(command.getTitle()));
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }
}
