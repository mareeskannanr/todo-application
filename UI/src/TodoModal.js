import React, { Component } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';
import axios from 'axios';

export default class TodoModal extends Component {

    constructor(props) {
        super(props);

        this.state = {
            todo:  this.props.todo
        }
    }

    onChange(value) {
        let {todo} = this.state;
        todo.content = value;
        this.setState({
            todo
        });
    }

    save(isUpdate) {
        axios[isUpdate ? 'put' : 'post']("http://localhost:8080/api/todos", this.state.todo)
        .then(response => this.props.closeModal(this.state.todo.id ? 'update' : 'create'))
        .catch(error => this.props.closeModal('error', error.response.data));
    }

    render() {
        let {content, id} = this.state.todo;
        return (
            <div>
                <Modal isOpen={true}>
                    <ModalHeader><span className="fa fa-edit"></span> Todo Information</ModalHeader>
                    <ModalBody>
                        <div className="row">
                            <div className="col-8 offset-2 form-group">
                                <input type='text' onChange={e => this.onChange(e.target.value)} value={content || ''}  className="form-control" />
                            </div>
                        </div>
                    </ModalBody>
                    <ModalFooter>
                        <div className="col-12 text-center">
                            <button type="button" className="btn btn-success" onClick={() => this.save(id > 0)} disabled={!content}>
                                <span className="far fa-save"></span> {id > 0 ? 'Update' : 'Save'}
                            </button>&nbsp;&nbsp;
                            <button type="button" className="btn btn-danger" onClick={() => this.props.closeModal()}>
                                <span className="fa fa-times"></span> Cancel
                            </button>
                        </div>
                    </ModalFooter>
                </Modal>
            </div>
        );
    }

}