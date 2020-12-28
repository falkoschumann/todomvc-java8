/*
 * TodoMVC - Distrubuted
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import de.muspellheim.todomvc.frontend.TodoAppView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response.Status.Family;
import lombok.var;
import org.apache.johnzon.jaxrs.jsonb.jaxrs.JsonbJaxrsProvider;

public class App extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    var frontend = new TodoAppView();

    frontend.setOnNewTodoCommand(
        it -> {
          var response = createWebTarget().path("newtodocommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            frontend.display(result);
          }
        });
    frontend.setOnToggleCommand(
        it -> {
          var response = createWebTarget().path("togglecommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            frontend.display(result);
          }
        });
    frontend.setOnToggleAllCommand(
        it -> {
          var response = createWebTarget().path("toggleallcommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            frontend.display(result);
          }
        });
    frontend.setOnEditCommand(
        it -> {
          var response = createWebTarget().path("editcommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            frontend.display(result);
          }
        });
    frontend.setOnDestroyCommand(
        it -> {
          var response = createWebTarget().path("destroycommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            frontend.display(result);
          }
        });
    frontend.setOnClearCompletedCommand(
        it -> {
          var response =
              createWebTarget().path("clearcompletedcommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            frontend.display(result);
          }
        });
    frontend.setOnTodosQuery(
        it -> {
          var response = createWebTarget().path("todosquery").request().get();
          var string = response.readEntity(String.class);
          System.out.println(string);

          response = createWebTarget().path("todosquery").request().get();
          System.out.println(response.getHeaders());
          var result = response.readEntity(TodosQueryResult.class);
          frontend.display(result);
        });

    frontend.run();

    Scene scene = new Scene(frontend);
    stage.setScene(scene);
    stage.setTitle("TodoMVC");
    stage.show();
  }

  private WebTarget createWebTarget() {
    return ClientBuilder.newBuilder()
        .register(JsonbJaxrsProvider.class)
        .build()
        .target("http://localhost:8080")
        .path("api");
  }
}
