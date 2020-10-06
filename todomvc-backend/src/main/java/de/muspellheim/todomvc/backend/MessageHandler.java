/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend;

import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQueryResult;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.val;
import lombok.var;

public class MessageHandler {
  private final TodoRepository repository;

  public MessageHandler(TodoRepository repository) {
    this.repository = repository;
  }

  public CommandStatus handle(NewTodoCommand command) {
    val todos = new ArrayList<>(repository.load());
    todos.add(Todo.of(command.getTitle()));
    repository.store(todos);
    return new Success();
  }

  public CommandStatus handle(ToggleAllCommand command) {
    var todos = repository.load();
    todos =
        todos.stream()
            .map(it -> it.withCompleted(command.isCompleted()))
            .collect(Collectors.toList());
    repository.store(todos);
    return new Success();
  }

  public CommandStatus handle(ToggleCommand command) {
    var todos = repository.load();
    todos =
        todos.stream()
            .map(
                it ->
                    (it.getId().equals(command.getId())) ? it.withCompleted(!it.isCompleted()) : it)
            .collect(Collectors.toList());
    repository.store(todos);
    return new Success();
  }

  public CommandStatus handle(DestroyCommand command) {
    var todos = repository.load();
    todos = todos.stream().filter(it -> it.getId() != command.getId()).collect(Collectors.toList());
    repository.store(todos);
    return new Success();
  }

  public CommandStatus handle(EditCommand command) {
    var todos = repository.load();
    todos =
        todos.stream()
            .map(it -> it.getId().equals(command.getId()) ? it.withTitle(command.getTitle()) : it)
            .collect(Collectors.toList());
    repository.store(todos);
    return new Success();
  }

  public CommandStatus handle(ClearCompletedCommand command) {
    var todos = repository.load();
    todos = todos.stream().filter(it -> !it.isCompleted()).collect(Collectors.toList());
    repository.store(todos);
    return new Success();
  }

  public TodoListQueryResult handle(TodoListQuery query) {
    val todos = repository.load();
    return new TodoListQueryResult(todos);
  }
}
