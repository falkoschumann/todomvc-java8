/*
 * TodoMVC - Contract
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract.messages.commands;

import de.muspellheim.messages.Command;
import lombok.Data;

@Data
public class ClearCompletedCommand implements Command {}
