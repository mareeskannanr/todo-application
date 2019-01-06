import React, { Component } from 'react';
import TodoItem from './TodoItem';
import TodoModal from './TodoModal';
import axios from 'axios';
import MessageModal from './MessageModal';

export default class TodoList extends Component {

    todoList = [];

    constructor(props) {
        super(props);

        this.state = {
            isOpen: false,
            messageOpen: false,
            todo: {},
            todoList: [],
            type: '',
            message: ''
        };
    }

    componentDidMount() {
        this.getTodos();
    }

    getTodos() {
        axios.get("http://localhost:8080/api/todos")
        .then(response => {
            this.todoList = response.data.map(todo => (
                <div key={todo.id}>
                    <TodoItem todo={todo} editTodo={() => this.editTodo(todo)} deleteTodo={() => this.deleteConfirmation(todo)} />
                </div>
            ));
            this.setState({todoList: this.todoList});
        }).catch(error => {
            this.setState({
                messageOpen: true,
                type: 'error',
                message: error.response.data
            });
            console.log(error);
        });
    }

    refreshList() {
        this.getTodos();
        this.setState({isOpen: false});
    }

    editTodo(todo) {
        this.setState({
            isOpen: true,
            todo
        })
    }

    deleteConfirmation(todo) {
        this.setState({
            messageOpen: true,
            type : 'deleteConfirmation',
            todo
        });
    }

    deleteTodo(todoId) {
        this.setState({
            messageOpen: false
        });

        axios.delete(`http://localhost:8080/api/todo/${todoId}`)
        .then(response => {
            console.log(response);
            this.setState({
                messageOpen: true,
                type : 'remove'
            });
        }).catch(error => {
            console.log(error);
            this.setState({
                messageOpen: true,
                type : 'remove',
                message : error.response.data 
            });
        });
    }

    closeTodoModal(type, message = '') {
        if(!type) {
            return this.setState({
              isOpen: false
            });
        }

        this.setState({
          isOpen: false,
          messageOpen: true,
          todo : {},
          type,
          message
        });
      }

    render() {
        return (
            <div>
                {this.state.todoList}
                {this.state.isOpen ? <TodoModal isOpen={this.state.isOpen} todo={this.state.todo} closeModal={(type, message) => this.closeTodoModal(type, message)} /> : ''}
                {this.state.messageOpen ? <MessageModal type={this.state.type} todo={this.state.todo} closeModal={() => {this.setState({messageOpen: false});this.getTodos();}} deleteModal={id => this.deleteTodo(id)}  message={this.state.message}  /> : ''}
            </div>

        );
    }

}