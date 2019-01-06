package com.todo.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.app.model.Todo;
import com.todo.app.repository.TodoRepository;
import com.todo.app.utils.AppConstants;
import com.todo.app.utils.TodoTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TodoAppApplicationTests {

	private static final String TODOS_URL = "/api/todos";
	private static final String TODO_URL = "/api/todo/1";
	private static final String CONTENT = "Test Content";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private TodoRepository todoRepository;

	@Test
	public void validateTodo() throws Exception {
		Todo todo = new Todo();
		String todoString = objectMapper.writeValueAsString(todo);
		MvcResult result = mockMvc.perform(post(TODOS_URL)
				.content(todoString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
		assertNotNull(result.getResponse().getContentAsString());
		String[] errorArray = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
		assertEquals(errorArray.length, 1);

		todo = new Todo();
		todo.setContent("");
		todoString =  objectMapper.writeValueAsString(todo);
		result = mockMvc.perform(post(TODOS_URL)
				.content(todoString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
		assertNotNull(result.getResponse().getContentAsString());
		errorArray = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
		assertEquals(errorArray.length, 1);

		todo = new Todo();
		todo.setContent("       ");
		todoString =  objectMapper.writeValueAsString(todo);
		result = mockMvc.perform(post(TODOS_URL)
				.content(todoString)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
		assertNotNull(result.getResponse().getContentAsString());
		errorArray = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
		assertEquals(errorArray.length, 1);
	}

	@Test
	public void saveDuplicateTodo() throws Exception {
		when(todoRepository.countByContent(any(String.class))).thenReturn(1L);

		Todo todoData = new Todo();
		todoData.setContent("Content : 1");

		String todo = objectMapper.writeValueAsString(todoData);
		MvcResult result = mockMvc.perform(post(TODOS_URL)
				.content(todo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.BAD_REQUEST.value());
		assertNotNull(result.getResponse().getContentAsString());
		String[] errorArray = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
		assertEquals(errorArray.length, 1);
		assertEquals(errorArray[0], AppConstants.CONTENT_ALREADY_EXISTS);
	}

	@Test
	public void saveTodo() throws Exception {
		Todo mockedTodo = new Todo();
		mockedTodo.setId(1L);
		mockedTodo.setContent(CONTENT);

		when(todoRepository.countByContent(any(String.class))).thenReturn(0L);
		when(todoRepository.save(any(Todo.class))).thenReturn(mockedTodo);

		Todo todoData = new Todo();
		todoData.setContent(CONTENT);

		String todo = objectMapper.writeValueAsString(todoData);
		MvcResult result = mockMvc.perform(post(TODOS_URL)
				.content(todo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
		assertNotNull(result.getResponse().getContentAsString());
		Todo response = objectMapper.readValue(result.getResponse().getContentAsString(), Todo.class);
		assertNotNull(response.getId());
		assertEquals(response.getContent(), CONTENT);
	}

	@Test
	public void fetchAllTodos() throws Exception {
		List<Todo> todoList = TodoTestUtil.createTodoList(3);
		when(todoRepository.findAll()).thenReturn(todoList);

		MvcResult result = mockMvc.perform(get(TODOS_URL)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
		List<Todo> todoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		assertEquals(todoResponse.size(), 3);
	}

	@Test
	public void findTodoByIdWithNotFound() throws Exception {
		when(todoRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));

		MvcResult result = mockMvc.perform(get(TODO_URL)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
		assertNotNull(result.getResponse().getContentAsString());
		String[] errorArray = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
		assertEquals(errorArray.length, 1);
		assertEquals(errorArray[0], AppConstants.TODO_NOT_EXISTS);
	}

	@Test
	public void findTodoByIdWithResult() throws Exception {
		List<Todo> todoList = TodoTestUtil.createTodoList(1);
		when(todoRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(todoList.get(0)));

		MvcResult result = mockMvc.perform(get(TODO_URL)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
		assertNotNull(result.getResponse().getContentAsString());
		Todo todoResponse = objectMapper.readValue(result.getResponse().getContentAsString(), Todo.class);
		assertEquals(todoResponse.getId().longValue(), 1L);
	}

	@Test
	public void removeTodoWitInvalidId() throws Exception {
		doReturn(Optional.ofNullable(null)).when(todoRepository).findById(anyLong());

		MvcResult result = mockMvc.perform(delete(TODO_URL)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
		String[] errorArray = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
		assertEquals(errorArray.length, 1);
		assertEquals(errorArray[0], AppConstants.TODO_NOT_EXISTS);
	}

	@Test
	public void removeTodoById() throws Exception {
		Todo todo = TodoTestUtil.createTodoList(1).get(0);

		doReturn(Optional.ofNullable(todo)).when(todoRepository).findById(anyLong());
		doNothing().when(todoRepository).deleteById(any(Long.class));

		MvcResult result = mockMvc.perform(delete(TODO_URL)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void removeAllTodos() throws Exception {
		doNothing().when(todoRepository).deleteAll();

		MvcResult result = mockMvc.perform(delete(TODOS_URL)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.NO_CONTENT.value());
	}

	@Test
	public void updateTodoWithNullId() throws Exception {
		Todo todoResponse = TodoTestUtil.createTodoList(1).get(0);
		doReturn(todoResponse).when(todoRepository).save(any(Todo.class));
		doReturn(0L).when(todoRepository).countByContent(anyString());

		final String CONTENT_1 = "Content 1";

		Todo todoObject = new Todo();
		todoObject.setContent(CONTENT_1);
		String todo = objectMapper.writeValueAsString(todoObject);
		MvcResult result = mockMvc.perform(put(TODOS_URL)
				.content(todo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();
		assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
		assertNotNull(result.getResponse().getContentAsString());
		Todo response = objectMapper.readValue(result.getResponse().getContentAsString(), Todo.class);
		assertEquals(response.getId().longValue(), 1L);
		assertEquals(response.getContent(), CONTENT_1);
	}

	@Test
	public void updateTodoWithInvalidId() throws Exception {
		Todo todoObject = TodoTestUtil.createTodoList(1).get(0);

		when(todoRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(null));
		String todo = objectMapper.writeValueAsString(todoObject);
		MvcResult result = mockMvc.perform(put(TODOS_URL)
				.content(todo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), HttpStatus.NOT_FOUND.value());
		assertNotNull(result.getResponse().getContentAsString());
		String[] errorArray = objectMapper.readValue(result.getResponse().getContentAsString(), String[].class);
		assertEquals(errorArray.length, 1);
		assertEquals(errorArray[0], AppConstants.TODO_NOT_EXISTS);
	}

	@Test
	public void updateTodo() throws Exception {
		Todo todoObject = TodoTestUtil.createTodoList(1).get(0);
		final String UPDATED_CONTENT = "Updated Content";

		when(todoRepository.findById(any(Long.class))).thenReturn(Optional.of(todoObject));
		when(todoRepository.countByContentAndIdNot(anyString(), anyLong())).thenReturn(0L);
		doReturn(todoObject).when(todoRepository).save(any(Todo.class));

		todoObject.setContent(UPDATED_CONTENT);
		String todo = objectMapper.writeValueAsString(todoObject);
		MvcResult result = mockMvc.perform(put(TODOS_URL)
				.content(todo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andReturn();

		assertEquals(result.getResponse().getStatus(), HttpStatus.OK.value());
		assertNotNull(result.getResponse().getContentAsString());
		Todo response = objectMapper.readValue(result.getResponse().getContentAsString(), Todo.class);
		assertEquals(response.getId().longValue(), 1L);
		assertEquals(response.getContent(), UPDATED_CONTENT);
	}

}
