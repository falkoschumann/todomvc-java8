/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.messagehandlers;

import de.muspellheim.messages.CommandHandling;
import de.muspellheim.messages.CommandStatus;
import de.muspellheim.messages.Failure;
import de.muspellheim.messages.Success;
import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.var;

public class EditCommandHandler implements CommandHandling<EditCommand> {
  private final TodoRepository repository;

  public EditCommandHandler(TodoRepository repository) {
    this.repository = repository;
  }

  @Override
  public CommandStatus handle(@NonNull EditCommand command) {
    try {
      var todos =
          repository.load().stream()
              .peek(
                  it -> {
                    if (it.getId().equals(command.getId())) {
                      it.setTitle(command.getTitle());
                    }
                  })
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }
}
