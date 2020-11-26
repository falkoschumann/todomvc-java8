/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.Failure;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.var;

public class DestroyCommandHandler {
  private final TodoRepository repository;

  public DestroyCommandHandler(TodoRepository repository) {
    this.repository = repository;
  }

  public CommandStatus handle(@NonNull DestroyCommand command) {
    try {
      var todos =
          repository.load().stream()
              .filter(it -> it.getId() != command.getId())
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (IOException e) {
      return new Failure(e.getLocalizedMessage());
    }
  }
}