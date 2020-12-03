/*
 * TodoMVC - Application
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc;

import de.muspellheim.todomvc.backend.MessageHandlingImpl;
import de.muspellheim.todomvc.backend.adapters.TodoJsonRepository;
import de.muspellheim.todomvc.contract.TodoRepository;
import de.muspellheim.todomvc.frontend.UserInterface;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.var;

public class App extends Application {
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage stage) {
    var file = Paths.get("todos.json");
    TodoRepository repository = new TodoJsonRepository(file);
    var backend = new MessageHandlingImpl(repository);
    var frontend = new UserInterface(backend);
    frontend.run(stage);
  }
}
