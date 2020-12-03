/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend;

import de.muspellheim.todomvc.contract.MessageHandling;
import de.muspellheim.todomvc.contract.TodoRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.Failure;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.var;

public class MessageHandlingImpl implements MessageHandling {
  private final TodoRepository repository;

  public MessageHandlingImpl(TodoRepository repository) {
    this.repository = repository;
  }

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

  public CommandStatus handle(@NonNull ToggleCommand command) {
    try {
      var todos =
          repository.load().stream()
              .peek(
                  it -> {
                    if (it.getId().equals(command.getId())) {
                      it.setCompleted(!it.isCompleted());
                    }
                  })
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public CommandStatus handle(@NonNull ToggleAllCommand command) {
    try {
      var todos =
          repository.load().stream()
              .peek(it -> it.setCompleted(command.getCompleted()))
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

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

  public CommandStatus handle(@NonNull DestroyCommand command) {
    try {
      var todos =
          repository.load().stream()
              .filter(it -> !it.getId().equals(command.getId()))
              .collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public CommandStatus handle(@NonNull ClearCompletedCommand command) {
    try {
      var todos =
          repository.load().stream().filter(it -> !it.isCompleted()).collect(Collectors.toList());
      repository.store(todos);
      return new Success();
    } catch (Exception e) {
      return new Failure(e.getLocalizedMessage());
    }
  }

  public TodosQueryResult handle(@NonNull TodosQuery query) {
    try {
      var todos = repository.load();
      return new TodosQueryResult(todos);
    } catch (Exception e) {
      e.printStackTrace();
      return new TodosQueryResult(Collections.emptyList());
    }
  }
}
