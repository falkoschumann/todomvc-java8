/*
 * TodoMVC - Frontend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.ClearCompletedCommand;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.NewTodoCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleAllCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQuery;
import de.muspellheim.todomvc.contract.messages.queries.TodoListQueryResult;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.var;

public class TodoAppView extends VBox {

  @Getter @Setter private Consumer<NewTodoCommand> onNewTodoCommand;
  @Getter @Setter private Consumer<ToggleAllCommand> onToggleAllCommand;
  @Getter @Setter private Consumer<ToggleCommand> onToggleCommand;
  @Getter @Setter private Consumer<EditCommand> onEditCommand;
  @Getter @Setter private Consumer<DestroyCommand> onDestroyCommand;
  @Getter @Setter private Consumer<ClearCompletedCommand> onClearCompletedCommand;
  @Getter @Setter private Consumer<TodoListQuery> onTodoListQuery;

  private final CheckBox toggleAll;

  private final ListView<TodoModel> todoList;

  private final HBox footer;

  private final TextFlow todoCount;

  private final ToggleGroup filterGroup;
  private final ToggleButton allFilter;
  private final ToggleButton activeFilter;
  private final ToggleButton completedFilter;

  private final Button clearCompleted;

  private List<Todo> todos = Collections.emptyList();

  public TodoAppView() {
    var title = new Label("todos");
    title.setStyle("-fx-text-fill: darksalmon;-fx-font-size: 42;");

    toggleAll = new CheckBox();
    toggleAll.setOnAction(
        e -> {
          var checked = toggleAll.isSelected();
          onToggleAllCommand.accept(new ToggleAllCommand(checked));
        });

    var newTodo = new TextField();
    newTodo.setPromptText("What needs to be done?");
    newTodo.setOnAction(
        e -> {
          var text = newTodo.getText().trim();
          if (text.isEmpty()) {
            return;
          }
          onNewTodoCommand.accept(new NewTodoCommand(text));
          newTodo.setText("");
        });
    HBox.setHgrow(newTodo, Priority.ALWAYS);

    HBox newTodoWrapper = new HBox();
    newTodoWrapper.setSpacing(8);
    newTodoWrapper.setAlignment(Pos.CENTER_LEFT);
    newTodoWrapper.getChildren().addAll(toggleAll, newTodo);

    var header = new VBox();
    header.setSpacing(8);
    header.setPadding(new Insets(12, 12, 8, 12));
    header.setAlignment(Pos.CENTER);
    header.getChildren().addAll(title, newTodoWrapper);

    todoList = new ListView<>();
    todoList.setCellFactory(v -> new TodoListCell<>());
    VBox.setVgrow(todoList, Priority.ALWAYS);

    var count = new Text("0");
    count.setStyle("-fx-font-weight: bold;");

    var countSuffix = new Text(" items left");

    todoCount = new TextFlow(count, countSuffix);

    var spacer1 = new Pane();
    HBox.setHgrow(spacer1, Priority.ALWAYS);

    filterGroup = new ToggleGroup();

    allFilter = new ToggleButton("All");
    allFilter.setSelected(true);
    allFilter.setToggleGroup(filterGroup);
    allFilter.setOnAction(e -> updateTodoList());

    activeFilter = new ToggleButton("Active");
    activeFilter.setToggleGroup(filterGroup);
    activeFilter.setOnAction(e -> updateTodoList());

    completedFilter = new ToggleButton("Completed");
    completedFilter.setToggleGroup(filterGroup);
    completedFilter.setOnAction(e -> updateTodoList());

    var spacer2 = new Pane();
    HBox.setHgrow(spacer2, Priority.ALWAYS);

    clearCompleted = new Button("Clear Completed");
    clearCompleted.setOnAction(e -> onClearCompletedCommand.accept(new ClearCompletedCommand()));

    footer = new HBox();
    footer.setSpacing(8);
    footer.setPadding(new Insets(8, 12, 8, 12));
    footer
        .getChildren()
        .addAll(
            todoCount, spacer1, allFilter, activeFilter, completedFilter, spacer2, clearCompleted);

    var helptext = new Label("Double-click to edit a todo");
    helptext.setStyle("-fx-text-fill: darkgrey;");

    var info = new HBox();
    info.setAlignment(Pos.CENTER);
    info.setPadding(new Insets(8, 12, 12, 12));
    info.getChildren().add(helptext);

    setStyle("-fx-font-family: Verdana Arial sans-serif;");
    setPrefSize(500, 400);
    getChildren().addAll(header, todoList, footer, info);
  }

  public void display(@NonNull TodoListQueryResult result) {
    todos = result.getTodos();
    updateTodoList();

    var completedCount = result.getTodos().stream().filter(Todo::isCompleted).count();
    var activeTodoCount = result.getTodos().size() - completedCount;

    var hasTodos = !result.getTodos().isEmpty();
    allFilter.setVisible(hasTodos);
    todoList.setVisible(hasTodos);
    todoList.setManaged(hasTodos);
    footer.setVisible(hasTodos);
    footer.setManaged(hasTodos);

    var allCompleted = result.getTodos().size() == completedCount;
    toggleAll.setSelected(hasTodos && allCompleted);

    var text = new Text(String.valueOf(activeTodoCount));
    text.setStyle("-fx-font-weight: bold");
    todoCount
        .getChildren()
        .setAll(text, new Text(" item" + (completedCount == 1 ? "" : "s") + " left"));

    clearCompleted.setVisible(completedCount > 0);
  }

  private void updateTodoList() {
    var todoModels =
        todos.stream()
            .filter(
                it ->
                    filterGroup.getSelectedToggle() == activeFilter && !it.isCompleted()
                        || filterGroup.getSelectedToggle() == completedFilter && it.isCompleted()
                        || filterGroup.getSelectedToggle() == allFilter)
            .map(it -> new TodoModel(it, onToggleCommand, onEditCommand, onDestroyCommand))
            .collect(Collectors.toList());
    todoList.getItems().setAll(todoModels);
  }
}
