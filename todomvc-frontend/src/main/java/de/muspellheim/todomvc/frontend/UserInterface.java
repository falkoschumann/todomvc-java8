/*
 * TodoMVC - Frontend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.MessageHandling;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.var;

public class UserInterface {
  private final MessageHandling messageHandling;
  private final TodoAppView view;

  public UserInterface(MessageHandling messageHandling) {
    this.messageHandling = messageHandling;

    view = new TodoAppView();
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
  }

  public void run(Stage stage) {
    var result = messageHandling.handle(new TodosQuery());
    view.display(result);

    Scene scene = new Scene(view);
    stage.setScene(scene);
    stage.setTitle("TodoMVC");
    stage.show();
  }
}
