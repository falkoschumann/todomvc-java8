/*
 * TodoMVC - Contract
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract.messages.queries;

import de.muspellheim.messages.QueryResult;
import de.muspellheim.todomvc.contract.data.Todo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodosQueryResult implements QueryResult {
  List<Todo> todos;
}
