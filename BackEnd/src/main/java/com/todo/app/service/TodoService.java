package com.todo.app.service;

import com.todo.app.model.Todo;

import java.util.List;

public interface TodoService {

    Todo save(Todo todo);

    List<Todo> getAll();

    Todo getById(Long id);

    void deleteById(Long id);

    void deleteAll();

}
