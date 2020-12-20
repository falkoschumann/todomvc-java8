/*
 * TodoMVC - Contract
 * Copyright (c) 2020 Falko Schumann <falko.schumann@muspellheim.de>
 */

package de.muspellheim.todomvc.contract.messages;

public interface QueryHandling<Q extends Query, R extends QueryResult>
    extends RequestHandling<Q, R> {}
