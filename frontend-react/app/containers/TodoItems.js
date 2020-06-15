import React, { Component } from "react"

export default class TodoItems extends Component {
    createItems = (event) => {
        return <li key={event.key}>{event.text}</li>
    }

    render() {
        var todoEnries = this.props.entries;
        var listItems = todoEnries.map(this.createItems);

        return (
            <ul>
                {listItems}
            </ul>
        );
    }
}