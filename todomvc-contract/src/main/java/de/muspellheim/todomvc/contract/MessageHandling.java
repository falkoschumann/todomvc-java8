/*
 * TodoMVC - Contract
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract;

import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.CommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;

public interface MessageHandling {
  CommandStatus handle(NewTodoCommand command);

  CommandStatus handle(ToggleCommand command);

  public CommandStatus handle(ToggleAllCommand command);

  public CommandStatus handle(EditCommand command);

  public CommandStatus handle(DestroyCommand command);

  public CommandStatus handle(ClearCompletedCommand command);

  public TodosQueryResult handle(TodosQuery query);
}
