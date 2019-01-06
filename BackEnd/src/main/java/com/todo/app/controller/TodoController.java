package com.todo.app.controller;


import com.todo.app.exception.AppException;
import com.todo.app.model.Todo;
import com.todo.app.service.TodoService;
import com.todo.app.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Arrays;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping("/todos")
    public ResponseEntity fetchAllTodos() {
        return ResponseEntity.ok(todoService.getAll());
    }

    @PostMapping("/todos")
    public ResponseEntity createTodo(@Valid @RequestBody Todo todo) {
        return ResponseEntity.ok(todoService.save(todo));
    }

    @PutMapping("/todos")
    public ResponseEntity updteTodo(@Valid @RequestBody Todo todo) {
        if(todo.getId() != null) {
            Todo result = todoService.getById(todo.getId());
            if(result == null) {
                throw new AppException(HttpStatus.NOT_FOUND, Arrays.asList(new String[] {AppConstants.TODO_NOT_EXISTS}));
            }

            todo.setLastUpdatedOn(LocalDateTime.now());
        }

        return ResponseEntity.ok(todoService.save(todo));

    }

    @GetMapping("/todo/{id}")
    public ResponseEntity fetchTodoById(@PathVariable("id") Long id) {
        Todo todo = todoService.getById(id);
        if(todo != null) {
            return ResponseEntity.ok(todo);
        }

        throw new AppException(HttpStatus.NOT_FOUND, Arrays.asList(new String[] {AppConstants.TODO_NOT_EXISTS}));
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity removeTodoById(@PathVariable("id") Long id) {

        Todo todo = todoService.getById(id);
        if(todo == null) {
            throw new AppException(HttpStatus.NOT_FOUND, Arrays.asList(new String[] {AppConstants.TODO_NOT_EXISTS}));
        }

        todoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todos")
    public ResponseEntity removeAllTodos() {
        todoService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}