/*
 * TodoMVC - Contract
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract.messages.commands;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
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
