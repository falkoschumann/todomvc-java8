/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import de.muspellheim.todomvc.backend.MessageHandlingImpl;
import de.muspellheim.todomvc.backend.adapters.TodoJsonRepository;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.Failure;
import de.muspellheim.todomvc.contract.messages.commands.HttpCommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.var;

@Path("/")
@ApplicationScoped
public class TodoMvcController {
  static MessageHandlingImpl messageHandling;

  static {
    var file = Paths.get("todos.json");
    var repository = new TodoJsonRepository(file);
    messageHandling = new MessageHandlingImpl(repository);
  }

  @Path("newtodocommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Legt ein neues To-Do in der Liste an.")
  @ApiResponse(
      responseCode = "200",
      description = "Das Command wurde erfolgreich ausgeführt.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Das Command wurde nicht ausgeführt, weil es fehlerhaft formuliert war.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Beim Ausführen des Commands ist ein Fehler aufgetreten.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public Response handleNewTodoCommand(NewTodoCommand command) {
    if (command.getTitle() == null) {
      return badRequest("Missing property `title` in new todo command.");
    }

    var status = messageHandling.handle(command);
    return checkCommandStatus(status);
  }

  @Path("togglecommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Schaltet den Erledigt-Zustand eines To-Do in der Liste um.")
  @ApiResponse(
      responseCode = "200",
      description = "Das Command wurde erfolgreich ausgeführt.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Das Command wurde nicht ausgeführt, weil es fehlerhaft formuliert war.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Beim Ausführen des Commands ist ein Fehler aufgetreten.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public Response handleToggleCommand(ToggleCommand command) {
    if (command.getId() == null) {
      return badRequest("Missing property `id` in toggle command.");
    }

    var status = messageHandling.handle(command);
    return checkCommandStatus(status);
  }

  @Path("toggleallcommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Setzt den Erledigt-Zustand aller To-Do's in derListe.")
  @ApiResponse(
      responseCode = "200",
      description = "Das Command wurde erfolgreich ausgeführt.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Das Command wurde nicht ausgeführt, weil es fehlerhaft formuliert war.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Beim Ausführen des Commands ist ein Fehler aufgetreten.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public Response handleToggleAllCommand(ToggleAllCommand command) {
    if (command.getCompleted() == null) {
      return badRequest("Missing property `completed` in toggle all command.");
    }

    var status = messageHandling.handle(command);
    return checkCommandStatus(status);
  }

  @Path("editcommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Ändert ein To-Do in der Liste.")
  @ApiResponse(
      responseCode = "200",
      description = "Das Command wurde erfolgreich ausgeführt.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Das Command wurde nicht ausgeführt, weil es fehlerhaft formuliert war.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Beim Ausführen des Commands ist ein Fehler aufgetreten.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public Response handleEditCommand(EditCommand command) {
    if (command.getId() == null) {
      return badRequest("Missing property `id` in edit command.");
    }
    if (command.getTitle() == null) {
      return badRequest("Missing property `title` in edit command.");
    }

    var status = messageHandling.handle(command);
    return checkCommandStatus(status);
  }

  @Path("destroycommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Entfernt ein To-Do aus der Liste.")
  @ApiResponse(
      responseCode = "200",
      description = "Das Command wurde erfolgreich ausgeführt.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Das Command wurde nicht ausgeführt, weil es fehlerhaft formuliert war.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Beim Ausführen des Commands ist ein Fehler aufgetreten.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public Response handleDestroyCommand(DestroyCommand command) {
    if (command.getId() == null) {
      return badRequest("Missing property `id` in edit command.");
    }

    var status = messageHandling.handle(command);
    return checkCommandStatus(status);
  }

  @Path("clearcompletedcommand")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Entfernt alle erledigten To-Do's aus der Liste.")
  @ApiResponse(
      responseCode = "200",
      description = "Das Command wurde erfolgreich ausgeführt.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Das Command wurde nicht ausgeführt, weil es fehlerhaft formuliert war.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Beim Ausführen des Commands ist ein Fehler aufgetreten.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpCommandStatus.class)))
  public Response handleClearCompletedCommand(ClearCompletedCommand command) {
    var status = messageHandling.handle(command);
    return checkCommandStatus(status);
  }

  @Path("todosquery")
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Gibt die To-Do-Liste zurück.")
  @ApiResponse(
      responseCode = "200",
      description = "Das Ergebnis der Query.",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = TodosQueryResult.class)))
  public TodosQueryResult handleTodosQuery() {
    return messageHandling.handle(new TodosQuery());
  }

  private Response badRequest(String errorMessage) {
    return Response.status(Status.BAD_REQUEST)
        .entity(new HttpCommandStatus(new Failure(errorMessage)))
        .build();
  }

  private Response checkCommandStatus(
      de.muspellheim.todomvc.contract.messages.commands.CommandStatus status) {
    if (status instanceof Success) {
      return Response.ok().entity(new HttpCommandStatus(new Success())).build();
    } else {
      var failure = (Failure) status;
      return Response.status(Status.INTERNAL_SERVER_ERROR)
          .entity(new HttpCommandStatus(new Failure(failure.getErrorMessage())))
          .build();
    }
  }
}
