/**
 * Created by blajv on 18.05.2017.
 */
import React from 'react';
import {Button, Modal} from 'react-bootstrap';
import {deleteCandidate} from "../utils/api";
class ConfirmationDialog extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            showModal: false,
            candidateId: this.props.candidateId
        };
        this._open = () => this.open();
        this._close = ()=> this.close();
        this._removeCandidate = () => this.removeCandidate();
    }

    close() {
        this.setState({showModal: false});
    }

    open() {
        this.setState({showModal: true});
    }

    removeCandidate = () => {
        deleteCandidate(this.state.candidateId).then(response => {
            this.props.refreshCandidateTable();
            this.setState({showModal: false});

        });
    };

    render() {

        return (
            <div>
                <Button
                    bsStyle="danger"
                    onClick={this._open}>
                    Delete Candidate
                </Button>

                <Modal show={this.state.showModal} onHide={this._close}>
                    <Modal.Header closeButton>
                        <Modal.Title>Are you sure you want to delete this candidate?</Modal.Title>
                    </Modal.Header>
                    <Modal.Footer>
                        <Button onClick={this._removeCandidate}>Yes</Button>
                        <Button onClick={this._close}>No</Button>
                    </Modal.Footer>
                </Modal>
            </div>
        )
    }
}

export default ConfirmationDialog;