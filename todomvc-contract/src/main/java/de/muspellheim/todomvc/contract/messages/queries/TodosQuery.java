/*
 * TodoMVC - Contract
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract.messages.queries;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import de.muspellheim.todomvc.contract.messages.Query;
import lombok.Data;

@Data
@JsonSerialize
public class TodosQuery implements Query {}
