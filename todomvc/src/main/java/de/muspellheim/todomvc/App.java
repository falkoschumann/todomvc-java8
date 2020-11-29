/*
 * TodoMVC - Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.MessageHandling;
import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.TodoJsonRepository;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.frontend.TodoAppView;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.var;

public class App extends Application {
  private MessageHandling messageHandling;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() {
    TodoRepository repository = createTodoRepository();
    messageHandling = new MessageHandling(repository);
  }

  protected TodoRepository createTodoRepository() {
    var file = Paths.get("todos.json");
    return new TodoJsonRepository(file);
  }

  @Override
  public void start(Stage stage) {
    var view = new TodoAppView();
    view.setOnNewTodoCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          view.display(result);
        });
    view.setOnToggleCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          view.display(result);
        });
    view.setOnToggleAllCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          view.display(result);
        });
    view.setOnEditCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          view.display(result);
        });
    view.setOnDestroyCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          view.display(result);
        });
    view.setOnClearCompletedCommand(
        it -> {
          messageHandling.handle(it);
          var result = messageHandling.handle(new TodosQuery());
          view.display(result);
        });
    view.setOnTodoListQuery(
        it -> {
          var result = messageHandling.handle(new TodosQuery());
          view.display(result);
        });

    var result = messageHandling.handle(new TodosQuery());
    view.display(result);

    Scene scene = new Scene(view);
    stage.setScene(scene);
    stage.setTitle("TodoMVC");
    stage.show();
  }
}
