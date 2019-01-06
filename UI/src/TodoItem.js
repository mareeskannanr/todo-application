import React from 'react';

export default function TodoItem(props) {
    return (
        <div className="card" style={{"margin": "10px", "height": "75px"}}>
            <div className="card-body">
                <div className="row">
                    <div className="col-10">
                        {props.todo.content}
                    </div>
                    <div className="col-1">
                        <button type="button" className="btn btn-info" onClick={() => props.editTodo()}>
                            <span className="fa fa-edit"></span>
                        </button>
                    </div>
                    <div className="col-1">
                        <button type="button" className="btn btn-danger" onClick={() => props.deleteTodo()}>
                            <span className="fa fa-trash"></span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};