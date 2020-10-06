/*
 * TodoMVC - Backend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.backend.adapters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.muspellheim.todomvc.contract.data.Todo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;

public class TodoJsonRepositoryTests {
  private static final Path READ_TEST_FILE = Paths.get("src/test/resources/todos.json");
  private static final Path WRITTEN_TEST_FILE = Paths.get("build/test/todos.json");

  @Test
  void load() throws IOException {
    val repository = new TodoJsonRepository(READ_TEST_FILE);

    val todos = repository.load();

    assertEquals(createTestData(), todos);
  }

  @Test
  void store() throws IOException {
    Files.createDirectories(WRITTEN_TEST_FILE.getParent());
    val repository = new TodoJsonRepository(WRITTEN_TEST_FILE);
    val todos = createTestData();

    repository.store(todos);

    val actualTodos = repository.load();
    assertEquals(createTestData(), actualTodos);
  }

  private static List<Todo> createTestData() {
    return Arrays.asList(
        new Todo("119e6785-8ffc-42e0-8df6-dbc64881f2b7", "Taste JavaScript", true),
        new Todo("d2f7760d-8f03-4cb3-9176-06311cb89993", "Buy a unicorn", false));
  }
}
