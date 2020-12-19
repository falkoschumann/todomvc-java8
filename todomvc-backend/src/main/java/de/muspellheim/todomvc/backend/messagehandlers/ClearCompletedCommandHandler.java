/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.CommandStatus;
import de.muspellheim.todomvc.contract.messages.Failure;
import de.muspellheim.todomvc.contract.messages.Success;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.var;

public class ClearCompletedCommandHandler {
  private final TodoRepository repository;

  public ClearCompletedCommandHandler(TodoRepository repository) {
    this.repository = repository;
  }

  public CommandStatus handle(@NonNull ClearCompletedCommand command) {
    try {
      var todos = repository.load().stream().filter(Todo::isActive).collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }
}
