/*
 * TodoMVC - Distrubuted
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.distributed;

import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import de.muspellheim.todomvc.frontend.AboutViewController;
import de.muspellheim.todomvc.frontend.TodosViewController;
import java.io.InputStream;
import java.util.Properties;
import javafx.application.Application;
import javafx.stage.Stage;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response.Status.Family;
import lombok.var;
import org.apache.johnzon.jaxrs.jsonb.jaxrs.JsonbJaxrsProvider;

public class App extends Application {
  private boolean useSystemMenuBar;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() {
    useSystemMenuBar = getParameters().getUnnamed().contains("--useSystemMenuBar");
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    //
    // Build
    //

    var todosViewController = TodosViewController.create(primaryStage, useSystemMenuBar);

    var aboutStage = new Stage();
    aboutStage.initOwner(primaryStage);
    var aboutViewController = AboutViewController.create(aboutStage);
    var appIcon = getClass().getResource("/app.png");
    aboutViewController.setIcon(appIcon.toString());
    try (InputStream in = getClass().getResourceAsStream("/app.properties")) {
      var appProperties = new Properties();
      appProperties.load(in);
      aboutViewController.setTitle(appProperties.getProperty("title"));
      aboutViewController.setVersion(appProperties.getProperty("version"));
      aboutViewController.setCopyright(appProperties.getProperty("copyright"));
    }

    //
    // Bind
    //

    todosViewController.setOnOpenAbout(() -> aboutStage.show());
    todosViewController.setOnNewTodoCommand(
        it -> {
          var response = createWebTarget().path("newtodocommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            todosViewController.display(result);
          }
        });
    todosViewController.setOnToggleCommand(
        it -> {
          var response = createWebTarget().path("togglecommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            todosViewController.display(result);
          }
        });
    todosViewController.setOnToggleAllCommand(
        it -> {
          var response = createWebTarget().path("toggleallcommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            todosViewController.display(result);
          }
        });
    todosViewController.setOnEditCommand(
        it -> {
          var response = createWebTarget().path("editcommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            todosViewController.display(result);
          }
        });
    todosViewController.setOnDestroyCommand(
        it -> {
          var response = createWebTarget().path("destroycommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            todosViewController.display(result);
          }
        });
    todosViewController.setOnClearCompletedCommand(
        it -> {
          var response =
              createWebTarget().path("clearcompletedcommand").request().post(Entity.json(it));
          if (response.getStatusInfo().getFamily() == Family.SUCCESSFUL) {
            response = createWebTarget().path("todosquery").request().get();
            TodosQueryResult result = response.readEntity(TodosQueryResult.class);
            todosViewController.display(result);
          }
        });
    todosViewController.setOnTodosQuery(
        it -> {
          var response = createWebTarget().path("todosquery").request().get();
          var string = response.readEntity(String.class);
          System.out.println(string);

          response = createWebTarget().path("todosquery").request().get();
          System.out.println(response.getHeaders());
          var result = response.readEntity(TodosQueryResult.class);
          todosViewController.display(result);
        });

    //
    // Run
    //

    todosViewController.run();
  }

  private WebTarget createWebTarget() {
    return ClientBuilder.newBuilder()
        .register(JsonbJaxrsProvider.class)
        .build()
        .target("http://localhost:8080")
        .path("api");
  }
}
