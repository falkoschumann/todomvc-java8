/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import de.muspellheim.todomvc.backend.MessageHandling;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.HttpCommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.var;

@Path("/")
@ApplicationScoped
public class BackendController {
  static MessageHandling messageHandling;

  @Path("newtodocommand")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public HttpCommandStatus handle(NewTodoCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("togglecommand")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public HttpCommandStatus handle(ToggleCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("toggleallcommand")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public HttpCommandStatus handle(ToggleAllCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("editcommand")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public HttpCommandStatus handle(EditCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("destroycommand")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public HttpCommandStatus handle(DestroyCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("clearcompletedcommand")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public HttpCommandStatus handle(ClearCompletedCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("todosquery")
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public TodosQueryResult handle(TodosQuery query) {
    return messageHandling.handle(query);
  }
}
