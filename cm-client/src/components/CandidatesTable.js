import React from 'react';
import {BootstrapTable, TableHeaderColumn} from 'react-bootstrap-table';
import {Tab, Tabs} from 'react-bootstrap';
import {fetchCandidates} from '../utils/api';
import {fetchSkillsForCandidate} from '../utils/api';
import {sortByLastName, sortByFirstName} from '../utils/sorting-comparators';
import EditCandidate from '../components/EditCandidate';
import './CandidatesTable.css';
import {fetchEducationForCandidate, fetchTagForCandidateSkill} from "../utils/api";
import ConfirmationDialog from  '../components/ConfirmationDialog'

class ModifyCandidate extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            candidate: this.props.candidate
        };
    }

    render() {
        return (
            <ul className="list-group">

                <li className="list-group-item">
                    <EditCandidate refreshCandidateTable={this.props.refreshCandidateTable} candidate={this.state.candidate}/>
                </li>
                <li className="list-group-item">
                    <ConfirmationDialog refreshCandidateTable={this.props.refreshCandidateTable} candidateId={this.state.candidate.id}/>
                </li>
            </ul>
        )
    }
}
class EducationList extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            educationData: {}
        };
    }

    componentDidMount() {
        fetchEducationForCandidate(this.props.educationLink).then(response => {
            this.setState({
                educationType: response.educationType,
                provider: response.provider,
                description: response.description
            });
        });
    }

    render() {

        return (
            <ul className="list-group">
                <li className="list-group-item">
                    {this.state.educationType}
                </li>

                <li className="list-group-item">
                    {this.state.provider}
                </li>

                <li className="list-group-item">
                    {this.state.description}
                </li>
            </ul>
        )
    }
}
class SkillItem extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            description: "",
            tagType: "",
            rating: props.rating
        };
    }

    componentDidMount() {
        fetchTagForCandidateSkill(this.props.tagLink).then(data => {
            this.setState({
                description: data.description,
                tagType: data.tagType,
            });
        });
    }

    render() {

        return (
            <li className="list-group-item">
                {this.state.tagType} : {this.state.description} : {this.state.rating}
            </li>
        )
    }
}
class SkillsList extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            tagLinks: []
        };
    }

    componentDidMount() {
        fetchSkillsForCandidate(this.props.skillsUrl).then(data => {
            this.setState({
                tagLinks: data
            });
        });
    }

    render() {

        return (
            <ul className="list-group">{this.state.tagLinks.map((data, index) => {
                return ( < SkillItem key={index} tagLink={data.tagLink} rating={data.rating}/>);
            })}</ul>
        )
    }
}

export default class BasicTable extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            candidates: null,
            detailViewActiveTab: 2
        };


        this.options = {
            defaultSortName: 'lastName', //default sort column name
            defaultSortOrder: 'asc', // default sort order
            paginationPosition: 'both'
        }
        //use bellow if you don't use arrow function
        // this.expandCandidateDetails = this.expandCandidateDetails.bind(this);
    }

    componentDidMount() {
        fetchCandidates().then(candidates => {

            this.setState({
                candidates: candidates,
                detailViewActiveTab: "1"
            });
        });
    }

    handlechange = ()=> {
        fetchCandidates().then(candidates => {

            this.setState({
                candidates: candidates,
                detailViewActiveTab: "1"
            });
        });
    };

    handleSelect = (selectedTab) => {
        // The active tab must be set into the state so that
        // the Tabs component knows about the change and re-renders.
        this.setState({
            activeTab: selectedTab
        });
    };

    expandCandidateDetails = (row) => {
        let candidate = [];
        candidate.push(row);

        return (

            <Tabs onSelect={this.handleSelect} id="controlled-tab-example">
                <Tab eventKey={1} title="Skills"><SkillsList skillsUrl={row._links.candidateSkillsList.href}/></Tab>
                <Tab eventKey={2} title="Education"> <EducationList educationLink={row._links.education.href}/></Tab>
                <Tab eventKey={3} title="Modify"> <ModifyCandidate refreshCandidateTable={this.handlechange} candidate={row}/></Tab>
            </Tabs>
        )
    };

    isExpandableRow(row) {
        return true;
    }

    getFilter(placeHolder) {
        return {
            type: 'RegexFilter',
            placeholder: placeHolder,
            delay: 1000
        }
    }

    render() {
        return (
            <BootstrapTable data={this.state.candidates } options={this.options} pagination exportCSV={true} expandComponent={ this.expandCandidateDetails }
                                       expandableRow={this.isExpandableRow}>
                <TableHeaderColumn dataField='id' filter={this.getFilter('Candidate Id')} isKey={ true }>Candidate ID</TableHeaderColumn>
                <TableHeaderColumn dataField='firstName' filter={this.getFilter('First Name')} dataSort sortFunc={sortByFirstName}>First Name</TableHeaderColumn>
                <TableHeaderColumn dataField='lastName' filter={this.getFilter('Last Name')} dataSort sortFunc={sortByLastName}>Last Name</TableHeaderColumn>
                <TableHeaderColumn dataField='email' filter={this.getFilter('Email')}>Email</TableHeaderColumn>
                <TableHeaderColumn dataField='phone'>Phone</TableHeaderColumn>
            </BootstrapTable>
        );
    }
}
