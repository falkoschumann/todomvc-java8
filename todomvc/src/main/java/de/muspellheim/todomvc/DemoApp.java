/*
 * TodoMVC - Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.MessageHandlingImpl;
import de.muspellheim.todomvc.backend.adapters.TodoMemoryRepository;
import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.frontend.UserInterface;
import java.util.Arrays;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.var;

public class DemoApp extends App {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    var repository = new TodoMemoryRepository();
    repository.store(
        Arrays.asList(
            new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
            new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false)));
    var backend = new MessageHandlingImpl(repository);
    var frontend = new UserInterface(backend);
    frontend.run(stage);
  }
}
