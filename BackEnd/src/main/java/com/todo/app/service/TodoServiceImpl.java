package com.todo.app.service;

import com.todo.app.exception.AppException;
import com.todo.app.model.Todo;
import com.todo.app.repository.TodoRepository;
import com.todo.app.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public Todo save(Todo todo) {
        long count = 0;
        if(todo.getId() != null) {
          count = todoRepository.countByContentAndIdNot(todo.getContent(), todo.getId());
        } else {
            count = todoRepository.countByContent(todo.getContent());
        }

        if(count > 0) {
            throw new AppException(HttpStatus.BAD_REQUEST, Arrays.asList(new String[] {AppConstants.CONTENT_ALREADY_EXISTS}));
        }

        return todoRepository.save(todo);
    }

    @Override
    public List<Todo> getAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo getById(Long id) {
        Optional<Todo> tempResult = todoRepository.findById(id);

        Todo result = null;
        if(tempResult.isPresent()) {
            result = tempResult.get();
        }

        return result;
    }

    @Override
    public void deleteById(Long id) {
        todoRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        todoRepository.deleteAll();
    }

}
