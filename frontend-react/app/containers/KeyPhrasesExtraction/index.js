import React from 'react';
import axios from 'axios';
import H1 from 'components/H1';
import H3 from 'components/H3';
import TodoItems from '../TodoItems';

export default class KeyPhrasesExtraction extends React.Component {
  constructor(props) {
    super(props);
    localStorage.removeItem("id1");
    this.state = {result: localStorage.getItem("id1"), items: [], languageCode: "en",  isIncludeStats: false, modelVersion: "latest"}; 
    this.input = React.createRef();
    this.languageCodeInput = React.createRef();
    this.isIncludeStatsInput = React.createRef();
  }
  
  // set state dynamically
  inputDocumentHandler = (event) => {
    this.setState({items: [inputDocument]});
  }

  // url builder
  urlBuilder = () => {
    var url = "http://localhost:8080/extractKeyPhrasesBatchString?";
  
    this.state.items.forEach(item => {
      if (item.text == "") {
        url += "documents=" + item.text + "&"
      } else {
        url += "documents=\"" + item.text + "\"" + "&"
      }  
    });

    url += "languageCode=" + this.state.languageCode + "&";
    url += "isIncludeStats=" + this.state.isIncludeStats;
    return url;
  }

  retrieveResult = (event) => {
    event.preventDefault();

    console.log("input document: " + this.state.inputDocument);
    // async call to get the response value
    const res = new Promise((resolve, reject) => {
      resolve(
        axios.get(this.urlBuilder(), {
          headers: {"Access-Control-Allow-Origin": "*"}
        }).then(function(response){
          console.log("Key Phrases extraction response:" + response.data);
          localStorage.setItem("id1", response.data);
        }).catch(function(error){
            console.log("Error: " + error)
        })
      )})

    // set the state in order to get the "id1"
    res.then(() => {
      console.log("state: ", this.state);
      this.setState({result : localStorage.getItem("id1")});
    })

  }

  // add input documnt handler
  addItem = (event) => {
    event.preventDefault();
    var newItem = {
      text: this.input.current.value,
      key: Date.now()
    };
    this.setState((prevState) => {
      return {
        items: prevState.items.concat(newItem),
        languageCode: this.languageCodeInput.current.value,
        isIncludeStats: this.isIncludeStatsInput.current.value
      };
    });
    this.input.current.value = "";
    this.forceUpdate();
  }

  render() {
    return (
      <div>
        <H1>Key Phrases Extraction</H1>
        <form onSubmit={this.addItem}> 
          <input ref={this.input} placeholder="Add document input"></input>
          <input ref={this.languageCodeInput} placeholder="Language Code, ex: en"></input>
          <input ref={this.isIncludeStatsInput} placeholder="Batch stats: true or false"></input>
          <button type="submit">Add</button>
        </form>
        <TodoItems entries={this.state.items}/>

        <button type="submit" onClick={this.retrieveResult}>Process</button>

        <div dangerouslySetInnerHTML={{__html: this.state.result}} />
      </div>
    );
  }
}