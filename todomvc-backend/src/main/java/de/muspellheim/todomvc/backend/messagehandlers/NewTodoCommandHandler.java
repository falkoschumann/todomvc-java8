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
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import java.util.ArrayList;
import lombok.NonNull;
import lombok.var;

public class NewTodoCommandHandler implements CommandHandling<NewTodoCommand> {
  private final TodoRepository repository;

  public NewTodoCommandHandler(TodoRepository repository) {
    this.repository = repository;
  }

  @Override
  public CommandStatus handle(@NonNull NewTodoCommand command) {
    try {
      var todos = new ArrayList<>(repository.load());
      todos.add(new Todo(command.getTitle()));
      repository.store(todos);
      return new Success();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }
}
