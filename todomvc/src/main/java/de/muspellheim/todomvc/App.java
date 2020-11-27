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
import de.muspellheim.todomvc.frontend.TodoAppViewController;
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

  private TodoAppViewController controller;

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
    System.out.println("Foobar!");
    var file = Paths.get("todos.json");
    return new TodoJsonRepository(file);
  }

  @Override
  public void start(Stage stage) {
    var root = TodoAppViewController.load();
    var view = root.getKey();
    controller = root.getValue();
    controller.setOnNewTodoCommand(
        it -> {
          newTodoCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          controller.display(result);
        });
    controller.setOnToggleCommand(
        it -> {
          toggleCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          controller.display(result);
        });
    controller.setOnToggleAllCommand(
        it -> {
          toggleAllCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          controller.display(result);
        });
    controller.setOnEditCommand(
        it -> {
          editCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          controller.display(result);
        });
    controller.setOnDestroyCommand(
        it -> {
          destroyCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          controller.display(result);
        });
    controller.setOnClearCompletedCommand(
        it -> {
          clearCompletedCommandHandler.handle(it);
          var result = todoListQueryHandler.handle(new TodoListQuery());
          controller.display(result);
        });
    controller.setOnTodoListQuery(
        it -> {
          var result = todoListQueryHandler.handle(new TodoListQuery());
          controller.display(result);
        });

    var result = todoListQueryHandler.handle(new TodoListQuery());
    controller.display(result);

    Scene scene = new Scene(view);
    stage.setScene(scene);
    stage.setTitle("TodoMVC");
    stage.show();
  }
}
