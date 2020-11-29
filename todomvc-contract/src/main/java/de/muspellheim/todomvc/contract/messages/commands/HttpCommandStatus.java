/*
 * TodoMVC - Contract
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract.messages.commands;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HttpCommandStatus {
  boolean success;
  String errorMessage;

  public HttpCommandStatus(CommandStatus status) {
    success = status instanceof Success;
    if (!success) errorMessage = ((Failure) status).getErrorMessage();
  }

  public CommandStatus commandStatus() {
    if (success) {
      return new Success();
    } else {
      return new Failure(errorMessage);
    }
  }
}
