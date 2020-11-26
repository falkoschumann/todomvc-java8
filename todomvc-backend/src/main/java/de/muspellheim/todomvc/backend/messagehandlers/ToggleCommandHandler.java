/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.Failure;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.var;

public class ToggleCommandHandler {
  private final TodoRepository repository;

  public ToggleCommandHandler(TodoRepository repository) {
    this.repository = repository;
  }

  public CommandStatus handle(@NonNull ToggleCommand command) {
    try {
      var todos =
          repository.load().stream()
              .map(
                  it ->
                      (it.getId().equals(command.getId()))
                          ? it.withCompleted(!it.isCompleted())
                          : it)
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }
}
