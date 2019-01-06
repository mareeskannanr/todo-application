import React, { Component } from 'react';
import { Modal, ModalHeader, ModalBody, ModalFooter } from 'reactstrap';

export default class MessageModal extends Component {

    constructor(props) {
        super(props);
        
        if(['create', 'update', 'remove', 'removeAll'].indexOf(props.type) >= 0) {
            this.displaySuccessMsg(props.type);
        } else if(props.type === 'deleteConfirmation') {
            this.displayDeleteConfirmation();
        } else if(props.type === 'error') {
            this.displayErrorMsg(props.message);            
        }
    }

    displaySuccessMsg(type) {
        let msgMap = new Map();
        msgMap.set('create', 'Todo Created Successfully!');
        msgMap.set('update', 'Todo Updated Successfully!');
        msgMap.set('remove', 'Todo Removed Successfully!');
        msgMap.set('removeAll', 'All Todo Items removed successfully!');
        
        let header = (
            <div>
                <h4><i className="fas fa-thumbs-up"></i> Done!</h4>
            </div>
        );

        let body = (
            <div>
                <p>{msgMap.get(type)}</p>
            </div>
        );

        let footer = (
            <div className='col-12 text-center'>
                <button type='button' className='btn btn-danger' onClick={() => this.props.closeModal()}>
                    <span className='fa fa-times'></span> Close
                </button>
            </div>
        );

        this.state = {header, body, footer};
    }

    displayDeleteConfirmation() {
        let header = (
            <div>
                <h4><span className="fa fa-exclamation-circle"></span> Confirmation</h4>
            </div>
        );

        let body = (
            <div>
                <p>Are You Sure, You want to Delete this item?</p>
            </div>
        );

        let footer = (
            <div className='col-12 text-center'>
                    <button type='button' className='btn btn-primary' onClick={() => this.props.deleteModal(this.props.todo.id)}>
                        <span className='fa fa-check'></span> Yes
                    </button>&nbsp;&nbsp;
                    <button type='button' className='btn btn-danger' onClick={() => this.props.closeModal()}>
                        <span className='fa fa-times'></span> No
                    </button>
            </div>
        );

        this.state = {header, body, footer};
    }

    displayErrorMsg(errors) {
        let header = (
            <div>
                <h4><span className="fa fa-exclamation-triangle"></span> Error</h4>
            </div>
        );

        let body = (
            <div>
                {errors}
            </div>
        );

        let footer = (
            <div className='col-12 text-center'>
                <button type='button' className='btn btn-danger' onClick={() => this.props.closeModal()}>
                    <span className='fa fa-times'></span> Close
                </button>
            </div>
        );
        this.state = {header, body, footer};
    }

    render() {
        let {header, body, footer} = this.state;
        return (
            <div>
                <Modal isOpen={true}>
                    <ModalHeader>
                        {header} 
                    </ModalHeader>
                    <ModalBody>
                        {body}
                    </ModalBody>
                    <ModalFooter>
                        {footer}
                    </ModalFooter>
                </Modal>
            </div>
        );
    }

}