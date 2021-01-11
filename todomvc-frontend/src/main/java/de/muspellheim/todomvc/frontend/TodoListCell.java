/*
 * TodoMVC - Frontend
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import de.muspellheim.todomvc.contract.data.Todo;
import de.muspellheim.todomvc.contract.messages.commands.DestroyCommand;
import de.muspellheim.todomvc.contract.messages.commands.EditCommand;
import de.muspellheim.todomvc.contract.messages.commands.ToggleCommand;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import lombok.Getter;
import lombok.Setter;

public class TodoListCell extends ListCell<Todo> {
  @Getter @Setter Consumer<ToggleCommand> onToggleCommand;
  @Getter @Setter Consumer<EditCommand> onEditCommand;
  @Getter @Setter Consumer<DestroyCommand> onDestroyCommand;

  private final HBox container;
  private final CheckBox completed;
  private final Label titleLabel;
  private final Button destroy;
  private final TextField titleTextField;

  public TodoListCell() {
    completed = new CheckBox();

    titleLabel = new Label();
    titleLabel.setMaxWidth(Double.MAX_VALUE);
    titleLabel.setMaxHeight(Double.MAX_VALUE);
    HBox.setHgrow(titleLabel, Priority.ALWAYS);

    titleTextField = new TextField();
    titleTextField.setMaxWidth(Double.MAX_VALUE);
    titleTextField.setMaxHeight(Double.MAX_VALUE);
    HBox.setHgrow(titleTextField, Priority.ALWAYS);

    destroy = new Button("X");
    destroy.setVisible(false);

    container = new HBox(8);
    container.setAlignment(Pos.CENTER_LEFT);
    container.getChildren().setAll(completed, titleLabel, destroy);
    container
        .hoverProperty()
        .addListener(
            (observableValue, oldValue, newValue) ->
                destroy.setVisible(newValue && container.getChildren().contains(titleLabel)));
  }

  @Override
  protected void updateItem(Todo item, boolean empty) {
    super.updateItem(item, empty);

    if (empty || item == null) {
      setText(null);
      setGraphic(null);
    } else {
      setGraphic(container);

      completed.setSelected(item.isCompleted());
      completed.setOnAction(it -> onToggleCommand.accept((new ToggleCommand(item.getId()))));

      titleLabel.setText(item.getTitle());
      titleLabel.setOnMouseClicked(it -> startEdit(it));

      titleTextField.setText(item.getTitle());
      titleTextField.setOnAction(it -> endEdit());
      titleTextField
          .focusedProperty()
          .addListener(
              (observableValue, oldValue, newValue) -> {
                if (oldValue && !newValue) {
                  endEdit();
                }
              });

      destroy.setOnAction(it -> onDestroyCommand.accept((new DestroyCommand(item.getId()))));
    }
  }

  private void startEdit(MouseEvent it) {
    if (it.getButton() == MouseButton.PRIMARY && it.getClickCount() == 2) {
      completed.setVisible(false);
      container.getChildren().set(1, titleTextField);
      titleTextField.requestFocus();
      destroy.setVisible(false);
    }
  }

  private void endEdit() {
    onEditCommand.accept(new EditCommand(getItem().getId(), titleTextField.getText().trim()));
    completed.setVisible(true);
    container.getChildren().set(1, titleLabel);
    destroy.setVisible(container.isHover());
  }
}
