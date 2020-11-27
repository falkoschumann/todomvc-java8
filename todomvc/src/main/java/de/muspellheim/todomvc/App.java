/*
 * TodoMVC - Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.TodoRepository;
import de.muspellheim.todomvc.backend.adapters.TodoJsonRepository;
import de.muspellheim.todomvc.backend.messagehandlers.ClearCompletedCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.DestroyCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.EditCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.NewTodoCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.TodoListQueryHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleAllCommandHandler;
import de.muspellheim.todomvc.backend.messagehandlers.ToggleCommandHandler;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQuery;
import de.muspellheim.todomvc.frontend.TodoAppView;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.var;

public class App extends Application {
  private NewTodoCommandHandler newTodoCommandHandler;
  private ToggleCommandHandler toggleCommandHandler;
  private ToggleAllCommandHandler toggleAllCommandHandler;
  private EditCommandHandler editCommandHandler;
  private DestroyCommandHandler destroyCommandHandler;
  private ClearCompletedCommandHandler clearCompletedCommandHandler;
  private TodoListQueryHandler todoListQueryHandler;

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void init() {
    TodoRepository repository = createTodoRepository();
    newTodoCommandHandler = new NewTodoCommandHandler(repository);
    toggleCommandHandler = new ToggleCommandHandler(repository);
    toggleAllCommandHandler = new ToggleAllCommandHandler(repository);
    editCommandHandler = new EditCommandHandler(repository);
    destroyCommandHandler = new DestroyCommandHandler(repository);
    clearCompletedCommandHandler = new ClearCompletedCommandHandler(repository);
    todoListQueryHandler = new TodoListQueryHandler(repository);
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
          newTodoCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          view.display(result);
        });
    view.setOnToggleCommand(
        it -> {
          toggleCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          view.display(result);
        });
    view.setOnToggleAllCommand(
        it -> {
          toggleAllCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          view.display(result);
        });
    view.setOnEditCommand(
        it -> {
          editCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          view.display(result);
        });
    view.setOnDestroyCommand(
        it -> {
          destroyCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          view.display(result);
        });
    view.setOnClearCompletedCommand(
        it -> {
          clearCompletedCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          view.display(result);
        });
    view.setOnTodoListQuery(
        it -> {
          var result = todoListQueryHandler.handle(new TodoListQuery());
          view.display(result);
        });

    var result = todoListQueryHandler.handle(new TodoListQuery());
    view.display(result);

    Scene scene = new Scene(view);
    stage.setScene(scene);
    stage.setTitle("TodoMVC");
    stage.show();
  }
}
