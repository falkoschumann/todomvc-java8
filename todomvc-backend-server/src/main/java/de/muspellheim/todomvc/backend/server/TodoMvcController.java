/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import de.muspellheim.todomvc.backend.MessageHandling;
import de.muspellheim.todomvc.backend.adapters.TodoJsonRepository;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.HttpCommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.nio.file.Paths;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.var;

@Path("/")
@ApplicationScoped
public class TodoMvcController {
  static MessageHandling messageHandling;

  static {
    var file = Paths.get("todos.json");
    var repository = new TodoJsonRepository(file);
    messageHandling = new MessageHandling(repository);
  }

  @Path("newtodocommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Legt ein neues To-Do in der Liste an.")
  @ApiResponse(
      responseCode = "200",
      description = "Status der Ausführung des Commands.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public HttpCommandStatus handleNewTodoCommand(NewTodoCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("togglecommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Schaltet den Erledigt-Zustand eines To-Do in der Liste um.")
  @ApiResponse(
      responseCode = "200",
      description = "Status der Ausführung des Commands.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public HttpCommandStatus handleToggleCommand(ToggleCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("toggleallcommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Setzt den Erledigt-Zustand aller To-Do's in derListe.")
  @ApiResponse(
      responseCode = "200",
      description = "Status der Ausführung des Commands.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public HttpCommandStatus handleToggleAllCommand(ToggleAllCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("editcommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Ändert ein To-Do in der Liste.")
  @ApiResponse(
      responseCode = "200",
      description = "Status der Ausführung des Commands.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public HttpCommandStatus handleEditCommand(EditCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("destroycommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Entfernt ein To-Do aus der Liste.")
  @ApiResponse(
      responseCode = "200",
      description = "Status der Ausführung des Commands.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public HttpCommandStatus handleDestroyCommand(DestroyCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("clearcompletedcommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Entfernt alle erledigten To-Do's aus der Liste.")
  @ApiResponse(
      responseCode = "200",
      description = "Status der Ausführung des Commands.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public HttpCommandStatus handleClearCompletedCommand(ClearCompletedCommand command) {
    var status = messageHandling.handle(command);
    return new HttpCommandStatus(status);
  }

  @Path("todosquery")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Gibt die To-Do-Liste zurück.")
  @ApiResponse(
      responseCode = "200",
      description = "Ergebnis der Query.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = TodosQueryResult.class)))
  public TodosQueryResult handleTodosQuery() {
    return messageHandling.handle(new TodosQuery());
  }
}
