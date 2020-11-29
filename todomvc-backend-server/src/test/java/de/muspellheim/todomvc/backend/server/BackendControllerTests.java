/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import de.muspellheim.todomvc.backend.MessageHandling;
import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.TodoMemoryRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.HttpCommandStatus;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.Success;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.Arrays;
import java.util.Collections;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import lombok.var;
import org.apache.meecrowave.Meecrowave;
import org.apache.meecrowave.junit5.MeecrowaveConfig;
import org.apache.meecrowave.testing.ConfigurationInject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@MeecrowaveConfig
public class BackendControllerTests {
  @ConfigurationInject private Meecrowave.Builder config;

  private TodoRepository repository;

  @BeforeEach
  void setUp() throws Exception {
    repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    BackendController.messageHandling = new MessageHandling(repository);
  }

  @Test
  void handleNewTodoCommand() throws Exception {
    var client = ClientBuilder.newClient();
    try {
      var command = new NewTodoCommand("Foobar");

      var response =
          client
              .target("http://localhost:" + config.getHttpPort())
              .path("api/newtodocommand")
              .request()
              .post(Entity.entity(command, MediaType.APPLICATION_JSON));

      assertEquals(Status.OK.getStatusCode(), response.getStatus());
      assertEquals(
          new HttpCommandStatus(new Success()), response.readEntity(HttpCommandStatus.class));
      assertEquals(3, repository.load().size(), "Todo added to list");
      assertNotNull(repository.load().get(2).getId(), "Todo id is set");
      assertEquals("Foobar", repository.load().get(2).getTitle(), "Todo title is set");
      assertFalse(repository.load().get(2).isCompleted(), "Todo is not completed");
    } finally {
      client.close();
    }
  }

  @Test
  void handleToggleCommand() throws Exception {
    var client = ClientBuilder.newClient();
    try {
      var command = new ToggleCommand("d2f7760d-8f03-4cb3-9176-06311cb89993");

      var response =
          client
              .target("http://localhost:" + config.getHttpPort())
              .path("api/togglecommand")
              .request()
              .post(Entity.entity(command, MediaType.APPLICATION_JSON));

      assertEquals(Status.OK.getStatusCode(), response.getStatus());
      assertEquals(
          new HttpCommandStatus(new Success()), response.readEntity(HttpCommandStatus.class));
      assertEquals(
          Arrays.asList(
              new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
          repository.load(),
          "Todos updated");
    } finally {
      client.close();
    }
  }

  @Test
  void handleToggleAllCommand() throws Exception {
    var client = ClientBuilder.newClient();
    try {
      var command = new ToggleAllCommand(true);

      var response =
          client
              .target("http://localhost:" + config.getHttpPort())
              .path("api/toggleallcommand")
              .request()
              .post(Entity.entity(command, MediaType.APPLICATION_JSON));

      assertEquals(Status.OK.getStatusCode(), response.getStatus());
      assertEquals(
          new HttpCommandStatus(new Success()), response.readEntity(HttpCommandStatus.class));
      assertEquals(
          Arrays.asList(
              new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", true)),
          repository.load(),
          "Todos updated");
    } finally {
      client.close();
    }
  }

  @Test
  void handleEditCommand() throws Exception {
    var client = ClientBuilder.newClient();
    try {
      var command = new EditCommand("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar");

      var response =
          client
              .target("http://localhost:" + config.getHttpPort())
              .path("api/editcommand")
              .request()
              .post(Entity.entity(command, MediaType.APPLICATION_JSON));

      assertEquals(Status.OK.getStatusCode(), response.getStatus());
      assertEquals(
          new HttpCommandStatus(new Success()), response.readEntity(HttpCommandStatus.class));
      assertEquals(
          Arrays.asList(
              new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Foobar", false)),
          repository.load(),
          "Todos updated");
    } finally {
      client.close();
    }
  }

  @Test
  void handleDestroyCommand() throws Exception {
    var client = ClientBuilder.newClient();
    try {
      var command = new DestroyCommand("119e6785-8ffc-42e0-8df6-dbc64881f2b7");

      var response =
          client
              .target("http://localhost:" + config.getHttpPort())
              .path("api/destroycommand")
              .request()
              .post(Entity.entity(command, MediaType.APPLICATION_JSON));

      assertEquals(Status.OK.getStatusCode(), response.getStatus());
      assertEquals(
          new HttpCommandStatus(new Success()), response.readEntity(HttpCommandStatus.class));
      assertEquals(
          Collections.singletonList(
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
          repository.load(),
          "Todos updated");
    } finally {
      client.close();
    }
  }

  @Test
  void handleClearCompletedCommand() throws Exception {
    var client = ClientBuilder.newClient();
    try {
      var command = new ClearCompletedCommand();

      var response =
          client
              .target("http://localhost:" + config.getHttpPort())
              .path("api/clearcompletedcommand")
              .request()
              .post(Entity.entity(command, MediaType.APPLICATION_JSON));

      assertEquals(Status.OK.getStatusCode(), response.getStatus());
      assertEquals(
          new HttpCommandStatus(new Success()), response.readEntity(HttpCommandStatus.class));
      assertEquals(
          Collections.singletonList(
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)),
          repository.load(),
          "Todos updated");
    } finally {
      client.close();
    }
  }

  @Test
  void handleTodosQuery() {
    var client = ClientBuilder.newClient();
    try {
      var query = new TodosQuery();

      var response =
          client
              .target("http://localhost:" + config.getHttpPort())
              .path("api/todosquery")
              .request()
              .post(Entity.entity(query, MediaType.APPLICATION_JSON));

      assertEquals(Status.OK.getStatusCode(), response.getStatus());
      assertEquals(
          new TodosQueryResult(
              Arrays.asList(
                  new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
                  new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false))),
          response.readEntity(TodosQueryResult.class));
    } finally {
      client.close();
    }
  }
}
