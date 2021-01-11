/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodosQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodosQueryResult;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

public class TodosViewController {
  @Getter @Setter private Runnable onOpenAbout;
  @Getter @Setter private Consumer<NewTodoCommand> onNewTodoCommand;
  @Getter @Setter private Consumer<ToggleAllCommand> onToggleAllCommand;
  @Getter @Setter private Consumer<ToggleCommand> onToggleCommand;
  @Getter @Setter private Consumer<EditCommand> onEditCommand;
  @Getter @Setter private Consumer<DestroyCommand> onDestroyCommand;
  @Getter @Setter private Consumer<ClearCompletedCommand> onClearCompletedCommand;
  @Getter @Setter private Consumer<TodosQuery> onTodosQuery;

  @FXML private MenuBar menuBar;
  @FXML private CheckBox toggleAll;
  @FXML private TextField newTodo;
  @FXML private ListView<Todo> todoList;
  @FXML private HBox footer;
  @FXML private TextFlow todoCount;
  @FXML private ToggleGroup filterGroup;
  @FXML private ToggleButton allFilter;
  @FXML private ToggleButton activeFilter;
  @FXML private ToggleButton completedFilter;
  @FXML private Button clearCompleted;

  private final ReadOnlyBooleanWrapper todosAvailable = new ReadOnlyBooleanWrapper(false);
  private List<Todo> todos = Collections.emptyList();

  public static TodosViewController create(Stage stage, boolean useSystemMenuBar) {
    var factory = new ViewControllerFactory(TodosViewController.class);

    var scene = new Scene(factory.getView());
    stage.setScene(scene);
    stage.setTitle("Todos");
    stage.setMinWidth(480);
    stage.setMinHeight(400);

    TodosViewController controller = factory.getController();
    controller.menuBar.setUseSystemMenuBar(useSystemMenuBar);
    return controller;
  }

  private Stage getWindow() {
    return (Stage) menuBar.getScene().getWindow();
  }

  public void run() {
    getWindow().show();
    onTodosQuery.accept(new TodosQuery());
  }

  public void display(TodosQueryResult result) {
    todos = result.getTodos();
    updateTodoList();

    var completedCount = result.getTodos().stream().filter(Todo::isCompleted).count();
    var activeTodoCount = result.getTodos().stream().filter(Todo::isActive).count();

    todosAvailable.set(!result.getTodos().isEmpty());

    boolean allCompleted = result.getTodos().size() == completedCount;
    toggleAll.setSelected(todosAvailable.get() && allCompleted);

    var text = new Text(String.valueOf(activeTodoCount));
    text.setStyle("-fx-font-weight: bold");
    todoCount
        .getChildren()
        .setAll(text, new Text(" item" + (completedCount == 1 ? "" : "s") + " left"));

    clearCompleted.setVisible(completedCount > 0);
  }

  @FXML
  private void initialize() {
    toggleAll.visibleProperty().bind(todosAvailable);
    todoList.setCellFactory(
        view -> {
          var cell = new TodoListCell();
          cell.setOnToggleCommand(onToggleCommand);
          cell.setOnEditCommand(onEditCommand);
          cell.setOnDestroyCommand(onDestroyCommand);
          return cell;
        });
    todoList.visibleProperty().bind(todosAvailable);
    todoList.managedProperty().bind(todosAvailable);
    footer.visibleProperty().bind(todosAvailable);
    footer.managedProperty().bind(todosAvailable);
  }

  @FXML
  private void handleExit() {
    Platform.exit();
  }

  @FXML
  private void handleAbout() {
    onOpenAbout.run();
  }

  @FXML
  private void handleToggleAll() {
    var checked = toggleAll.isSelected();
    onToggleAllCommand.accept(new ToggleAllCommand(checked));
  }

  @FXML
  private void handleNewTodo() {
    var text = newTodo.getText();
    if (text.trim().isEmpty()) {
      return;
    }

    onNewTodoCommand.accept(new NewTodoCommand(text));
    newTodo.setText("");
  }

  @FXML
  private void handleChangeFilter() {
    updateTodoList();
  }

  @FXML
  private void handleClearCompleted() {
    onClearCompletedCommand.accept(new ClearCompletedCommand());
  }

  private void updateTodoList() {
    var filteredTodos =
        todos.stream()
            .filter(
                it ->
                    filterGroup.getSelectedToggle() == activeFilter && it.isActive()
                        || filterGroup.getSelectedToggle() == completedFilter && it.isCompleted()
                        || filterGroup.getSelectedToggle() == allFilter)
            .collect(Collectors.toList());
    todoList.getItems().setAll(filteredTodos);
  }
}
