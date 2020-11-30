/*
 * TodoMVC - Backend Server
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.server;

import de.muspellheim.todomvc.backend.MessageHandling;
import de.muspellheim.todomvc.backend.adapters.TodoJsonRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import java.nio.file.Paths;
import javax.enterprise.context.Dependent;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import lombok.var;
import org.apache.meecrowave.runner.Cli;

@Dependent
@ApplicationPath("api")
@OpenAPIDefinition(info = @Info(version = "1.0", title = "TodoMVC API"))
public class App extends Application {
  public static void main(String[] args) {
    var file = Paths.get("todos.json");
    var repository = new TodoJsonRepository(file);
    BackendController.messageHandling = new MessageHandling(repository);

    Cli.main(args);
  }
}
