/*
 * TodoMVC
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.JsonTodoRepository;
import de.muspellheim.todomvc.backend.adapters.MemoryTodoRepository;
import de.muspellheim.todomvc.backend.messagehandlers.ClearCompletedCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.DestroyCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.EditCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.NewTodoCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.TodosQueryHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleAllCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleCommandHandler;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.frontend.TodoAppView;
import java.nio.file.Paths;
import java.util.Arrays;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.var;

public class App extends Application {
  private TodoRepository repository;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() throws Exception {
    var demo = getParameters().getUnnamed().contains("-demo");
    if (demo) {
      System.out.println("Run in demo mode...");
      repository = new MemoryTodoRepository();
      repository.store(
          Arrays.asList(
              new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
              new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    } else {
      var file = Paths.get("todos.json");
      repository = new JsonTodoRepository(file);
    }
  }

  @Override
  public void start(Stage stage) {
    var newTodoCommandHandler = new NewTodoCommandHandler(repository);
    var toggleCommandHandler = new ToggleCommandHandler(repository);
    var toggleAllCommandHandler = new ToggleAllCommandHandler(repository);
    var editCommandHandler = new EditCommandHandler(repository);
    var destroyCommandHandler = new DestroyCommandHandler(repository);
    var clearCompletedCommandHandler = new ClearCompletedCommandHandler(repository);
    var todosQueryHandler = new TodosQueryHandler(repository);

    var frontend = new TodoAppView();

    frontend.setOnNewTodoCommand(
        it -> {
          newTodoCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          frontend.display(result);
        });
    frontend.setOnToggleCommand(
        it -> {
          toggleCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          frontend.display(result);
        });
    frontend.setOnToggleAllCommand(
        it -> {
          toggleAllCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          frontend.display(result);
        });
    frontend.setOnEditCommand(
        it -> {
          editCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          frontend.display(result);
        });
    frontend.setOnDestroyCommand(
        it -> {
          destroyCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          frontend.display(result);
        });
    frontend.setOnClearCompletedCommand(
        it -> {
          clearCompletedCommandHandler.handle(it);
          var result = todosQueryHandler.handle(new TodosQuery());
          frontend.display(result);
        });
    frontend.setOnTodosQuery(
        it -> {
          var result = todosQueryHandler.handle(new TodosQuery());
          frontend.display(result);
        });

    frontend.run();

    Scene scene = new Scene(frontend);
    stage.setScene(scene);
    stage.setTitle("TodoMVC");
    stage.show();
  }
}
