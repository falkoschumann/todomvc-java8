/*
 * TodoMVC
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.adapters.TodoJsonRepository;
import de.muspellheim.todomvc.backend.messagehandlers.ClearCompletedCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.DestroyCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.EditCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.NewTodoCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.TodosQueryHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleAllCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleCommandHandler;
import de.muspellheim.todomvc.contract.TodoRepository;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.frontend.TodoAppView;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.var;

public class App extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    TodoRepository repository = createRepository();

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

  protected TodoRepository createRepository() {
    return new TodoJsonRepository(Paths.get("todos.json"));
  }
}
