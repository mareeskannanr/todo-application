import React, { Component, createRef } from 'react';
import TodoList from './TodoList';
import TodoModal from './TodoModal';
import axios from 'axios';
import MessageModal from './MessageModal';

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
      isOpen: false,
      messageOpen: false,
      todo: {},
      type: '',
      message: ''
    };

    this.todoListRef = createRef();
  }

  removeAll() {
    axios.delete("http://localhost:8080/api/todos")
    .then(res => {
      this.setState({
        messageOpen: true,
        type: 'removeAll',
        message: ''
      });  
    })
    .catch(error => {
      console.log(error);
      this.setState({
        messageOpen: true,
        type: 'error',
        message: error.response.data
      });
    });
  }

  closeTodoModal(type, message = '') {
    if(!type) {
      return this.setState({
        isOpen: false,
        todo: {}
      });
    }

    this.setState({
      isOpen: false,
      messageOpen: true,
      todo: {},
      type,
      message
    });
  }
  
  render() {
    return (
      <div className="container" style={{"marginTop": "30px"}}>
        <div className="row">
          <div className="col-12">
            <div className="col-8 offset-2">
              <div className="row card-header clearfix">
                <div className="col-8">
                  <h4><span className="fas fa-tasks"></span> Todo List</h4>
                </div>
                <div className="col-4">
                  <p className="text-right">
                    <button type="button" className="btn btn-success" onClick={() => this.setState({isOpen: !this.state.isOpen})}>
                        <span className="fa fa-plus"></span> Todo
                    </button>&nbsp;&nbsp;
                    <button type="button" className="btn btn-danger" onClick={() => this.removeAll()}>
                        <span className="fa fa-trash"></span> All 
                    </button>
                  </p>
                  
                  {this.state.isOpen ? <TodoModal todo={this.state.todo} closeModal={(type, message) => this.closeTodoModal(type, message)} /> : ''}
                  {this.state.messageOpen ? <MessageModal type={this.state.type} message={this.state.message} closeModal={() => {this.setState({messageOpen: false});this.todoListRef.current.getTodos();}}  /> : ''}
                </div>
              </div>
            </div>
          </div>
          <div className="col-12">
            <div className="col-8 offset-2">
              <TodoList ref={this.todoListRef} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default App;