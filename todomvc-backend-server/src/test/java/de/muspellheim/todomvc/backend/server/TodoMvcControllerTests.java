/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.TodoMemoryRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.Failure;
import de.muspellheim.todomvc.contract.messages.HttpCommandStatus;
import de.muspellheim.todomvc.contract.messages.Success;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import io.quarkus.test.junit.QuarkusTest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import lombok.var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class TodoMvcControllerTests {
  private TodoRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    repository = new TodoMemoryRepository();
    repository.store(createTodos());
    TodoMvcController.repository = repository;
  }

  @Test
  void handleNewTodoCommandWithSuccess() {
    var command = new NewTodoCommand("Foobar");

    var response = createWebTarget().path("newtodocommand").request().post(Entity.json(command));

    assertAll(
        "Handle new todo command with success",
        () -> assertEquals(Status.OK.getStatusCode(), response.getStatus(), "HTTP status is OK"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Success()),
                response.readEntity(HttpCommandStatus.class),
                "Command status is success"),
        () -> assertEquals(3, repository.load().size(), "Todo added to list"),
        () -> assertNotNull(repository.load().get(2).getId(), "New todo id is set"),
        () -> assertEquals("Foobar", repository.load().get(2).getTitle(), "New todo title is set"),
        () -> assertFalse(repository.load().get(2).isCompleted(), "New todo is not completed"));
  }

  @Test
  void handleNewTodoCommandMissingTitleWithFailure() {
    var command = new EmptyMessage();

    var response = createWebTarget().path("newtodocommand").request().post(Entity.json(command));

    assertAll(
        "Handle new todo command missing title with failure",
        () ->
            assertEquals(
                Status.BAD_REQUEST.getStatusCode(),
                response.getStatus(),
                "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `title` in new todo command.")),
                response.readEntity(HttpCommandStatus.class),
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleToggleCommandWithSuccess() {
    var command = new ToggleCommand("d2f7760d-8f03-4cb3-9176-06311cb89993");

    var response = createWebTarget().path("togglecommand").request().post(Entity.json(command));

    assertAll(
        "Handle toggle command with success",
        () -> assertEquals(Status.OK.getStatusCode(), response.getStatus(), "HTTP status is OK"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Success()),
                response.readEntity(HttpCommandStatus.class),
                "Command status is success"),
        () ->
            assertEquals(
                Arrays.asList(
                    new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                    new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
                repository.load(),
                "Todo in list updated"));
  }

  @Test
  void handleToggleCommand_MissingId_Error() {
    var response =
        createWebTarget().path("togglecommand").request().post(Entity.json(new EmptyMessage()));

    assertAll(
        "Handle toggle command missing id with failure",
        () ->
            assertEquals(
                Status.BAD_REQUEST.getStatusCode(),
                response.getStatus(),
                "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `id` in toggle command.")),
                response.readEntity(HttpCommandStatus.class),
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleToggleAllCommandWithSuccess() {
    var command = new ToggleAllCommand(true);

    var response = createWebTarget().path("toggleallcommand").request().post(Entity.json(command));

    assertAll(
        "Handle toggle all command with success",
        () -> assertEquals(Status.OK.getStatusCode(), response.getStatus(), "HTTP status is OK"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Success()),
                response.readEntity(HttpCommandStatus.class),
                "Command status is success"),
        () ->
            assertEquals(
                Arrays.asList(
                    new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                    new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
                repository.load(),
                "Todos in list updated"));
  }

  @Test
  void handleToggleAllCommandMissingCompletedWithFailure() {
    var command = new EmptyMessage();

    var response = createWebTarget().path("toggleallcommand").request().post(Entity.json(command));

    assertAll(
        "Handle toggle all command missing completed with failure",
        () ->
            assertEquals(
                Status.BAD_REQUEST.getStatusCode(),
                response.getStatus(),
                "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(
                    new Failure("Missing property `completed` in toggle all command.")),
                response.readEntity(HttpCommandStatus.class),
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleEditCommandWithSuccess() {
    var command = new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar");

    var response = createWebTarget().path("editcommand").request().post(Entity.json(command));

    assertAll(
        "Handle edit command with success",
        () -> assertEquals(Status.OK.getStatusCode(), response.getStatus(), "HTTP status is OK"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Success()),
                response.readEntity(HttpCommandStatus.class),
                "Command status is success"),
        () ->
            assertEquals(
                Arrays.asList(
                    new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                    new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar", false)),
                repository.load(),
                "Todo in list updated"));
  }

  @Test
  void handleEditCommandMissingIdWithFailure() {
    var command = new EditCommand(null, "Foobar");

    var response = createWebTarget().path("editcommand").request().post(Entity.json(command));

    assertAll(
        "Handle edit command missing id with failure",
        () ->
            assertEquals(
                Status.BAD_REQUEST.getStatusCode(),
                response.getStatus(),
                "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `id` in edit command.")),
                response.readEntity(HttpCommandStatus.class),
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleEditCommandMissingTitleWithFailure() {
    var command = new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", null);

    var response = createWebTarget().path("editcommand").request().post(Entity.json(command));

    assertAll(
        "Handle edit command missing title with failure",
        () ->
            assertEquals(
                Status.BAD_REQUEST.getStatusCode(),
                response.getStatus(),
                "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `title` in edit command.")),
                response.readEntity(HttpCommandStatus.class),
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleDestroyCommandWithSuccess() {
    var command = new DestroyCommand("119e6785-8ffc-42e0-8df6-dbc64881f2b7");

    var response = createWebTarget().path("destroycommand").request().post(Entity.json(command));

    assertAll(
        "Handle destroy command with success",
        () -> assertEquals(Status.OK.getStatusCode(), response.getStatus(), "HTTP status is OK"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Success()),
                response.readEntity(HttpCommandStatus.class),
                "Command status is success"),
        () ->
            assertEquals(
                Collections.singletonList(
                    new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
                repository.load(),
                "Todo removed from list"));
  }

  @Test
  void handleDestroyCommandMissingIdWithFailure() {
    var command = new EmptyMessage();

    var response = createWebTarget().path("destroycommand").request().post(Entity.json(command));

    assertAll(
        "Handle destroy command missing id with failure",
        () ->
            assertEquals(
                Status.BAD_REQUEST.getStatusCode(),
                response.getStatus(),
                "HTTP status is BAD REQUEST"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Failure("Missing property `id` in edit command.")),
                response.readEntity(HttpCommandStatus.class),
                "Command status is failure"),
        () -> assertEquals(createTodos(), repository.load(), "Todo list unchanged"));
  }

  @Test
  void handleClearCompletedCommandWithSuccess() {
    var command = new ClearCompletedCommand();

    var response =
        createWebTarget().path("clearcompletedcommand").request().post(Entity.json(command));

    assertAll(
        "Handle clear completed command with success",
        () -> assertEquals(Status.OK.getStatusCode(), response.getStatus(), "HTTP status is OK"),
        () ->
            assertEquals(
                new HttpCommandStatus(new Success()),
                response.readEntity(HttpCommandStatus.class),
                "Command status is success"),
        () ->
            assertEquals(
                Collections.singletonList(
                    new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
                repository.load(),
                "Todo list updated"));
  }

  @Test
  void handleTodosQuery() {
    var client = ClientBuilder.newClient();
    try {
      var result =
          createWebTarget()
              .path("todosquery")
              .request(MediaType.APPLICATION_JSON_TYPE)
              .get(TodosQueryResult.class);

      assertEquals(new TodosQueryResult(createTodos()), result);
    } finally {
      client.close();
    }
  }

  private static List<Todo> createTodos() {
    return Arrays.asList(
        new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
        new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false));
  }

  private WebTarget createWebTarget() {
    return ClientBuilder.newBuilder()
        .register(JacksonJaxbJsonProvider.class)
        .build()
        .target("http://localhost:8081")
        .path("api");
  }
}
