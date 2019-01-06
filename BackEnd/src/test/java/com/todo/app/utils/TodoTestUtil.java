package com.todo.app.utils;

import com.todo.app.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class TodoTestUtil {

    public static List<Todo> createTodoList(int count) {
        List<Todo> todoList = new ArrayList<>();
        for(int i=1; i<=count; i++) {
            Todo todo = new Todo();
            todo.setId((long) i);
            todo.setContent("Content " + i);
            todoList.add(todo);
        }

        return todoList;
    }


}
