/*
 * TodoMVC - Frontend
 * Copyright (c) 2021 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.frontend;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.var;

public class AboutViewController {
  @FXML private ImageView icon;
  @FXML private Label title;
  @FXML private Label version;
  @FXML private Label copyright;

  public static AboutViewController create(Stage stage) {
    var factory = new ViewControllerFactory(AboutViewController.class);
    var scene = new Scene(factory.getView());
    stage.setScene(scene);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initStyle(StageStyle.UTILITY);
    stage.setResizable(false);
    return factory.getController();
  }

  public void setIcon(String url) {
    this.icon.setImage(new Image(url, true));
  }

  public void setTitle(String name) {
    getWindow().setTitle("About " + name);
    title.setText(name);
  }

  public void setVersion(String version) {
    this.version.setText("Version " + version);
  }

  public void setCopyright(String copyright) {
    this.copyright.setText(copyright);
  }

  private Stage getWindow() {
    return (Stage) icon.getScene().getWindow();
  }
}
