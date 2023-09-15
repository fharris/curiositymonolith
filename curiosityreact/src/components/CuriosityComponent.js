import * as React from "react";
import styled from "styled-components";
import ApiClient from "../services/ApiClient";
import LastQueryPageComponent from './LastQueryPageComponent';



class ChallengeComponent extends React.Component {
  
    

    constructor(props) {
        super(props);
        this.state = {
          
            user: '',
            message: 'I want to know about...',
            query: '?',
            queryTopics: '',
            originalQuery: 'OldValueDude',
            curiosityUrl: 'https://www.wikipedia.org/',
            topic1: '',
            topic2: '',
            topic3: '',
            queryPageList: []
        };

        this.handleSubmitGetCuriosity = this.handleSubmitGetCuriosity.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmitGetPages = this.handleSubmitGetPages.bind(this);
    }


    
    componentDidMount(): void {
   
    }


    handleSubmitGetCuriosity(topic: string, user: string, originalQuery: string) {
        //get page
        //this.state.originalQuery = this.state.query;
         ApiClient.curiosity(user, topic, originalQuery).then(
            res => {
                if (res.ok) {
                    this.updateMessage("Oki! Here you have all you need to know about...");
                    res.json().then(json => {
                        this.setState({
                            //originalQuery: this.state.query,
                            query: json.title, 
                            curiosityUrl: json.keyUrl             
                        });
                    });
                    this.updateLastQueryPages(this.state.user);
                    } else {
                        alert("Ups! I have a little problem!");
                        this.updateMessage("Can't reach the server - Res in NoK! Make sure you have nickname!");
                }
            }
        ); 
        
    }

    handleChange(event) {
        const name = event.target.name;
        this.setState({
            [name]: event.target.value
        });
    }


    updateMessage(m: string) {
        this.setState({
          message: m
        });
    }

    handleSubmitGetPages(event) {
        event.preventDefault();
        ApiClient.getTopics(this.state.user,this.state.query).then(
            res => {
                if (res.ok) {
                    this.updateMessage("There are a few articles available for your curiosity");
                    res.json().then(json => {
                        console.log(Object.keys(json.query.pages));
                        const pagesIdArray = Object.keys(json.query.pages);
                        this.setState({
                            originalQuery: this.state.query,
                            topic1: json.query.pages[pagesIdArray[0]].title, 
                            topic2: json.query.pages[pagesIdArray[1]].title,
                            topic3: json.query.pages[pagesIdArray[2]].title
                        });
                    });
                    this.updateLastQueryPages(this.state.user);
                    } else {
                        alert("Ups! I have a little problem!");
                        this.updateMessage("Please make sure you have nickname or a proper query");
                  
                }

            }
        ); 
    }

    updateLastQueryPages(userName: string) {
        ApiClient.getStats(userName).then(res => {
            if (res.ok) {
                let querypage: QueryPage[] = [];
                res.json().then(data => {
                    data.forEach(item => {
                        querypage.push(item);
                    });
                    this.setState({
                        queryPageList: querypage
                    });
                })
            }
        })
    }

    render() {
        
    const theme = {
        blue: {
            default: "#3f51b5",
            hover: "#283593"
        },
        green: {
            default: "#d4e157",
            hover: "#616b57"
        },
        pink: {
            default: "#e91e63",
            hover: "#ad1457"
        }
    };

    const Button = styled.button`
    background-color: ${(props) => theme[props.theme].default};
    color: white;
    padding: 5px 15px;
    border-radius: 5px;
    outline: 0;
    text-transform: uppercase;
    margin: 10px 0px;
    cursor: pointer;
    box-shadow: 0px 2px 2px lightgray;
    transition: ease background-color 250ms;
    &:hover {
        background-color: ${(props) => theme[props.theme].hover};
    }
    &:disabled {
        cursor: default;
        opacity: 0.7;
    }
    `;

    Button.defaultProps = {
    theme: "blue"
    };

        
        return (
           
            <div class="center">
     <header>
     <h1>WikipediA CuriositieS</h1>
    </header>

               
                <h5>{this.state.message}</h5>

         
                
                <div>
                    <h2>
                        {this.state.query}
                    </h2>
                </div>

                <div>
                <LastQueryPageComponent queryPageList={this.state.queryPageList}/>
                </div>

                <form onSubmit={this.handleSubmitGetPages}>
                    <label>
                        Name:
                        <input type="text" maxLength="24"
                               name="user"
                               value={this.state.user}
                               onChange={this.handleChange}/>
                    </label>
                    <br/>
                    <label>

                        Query:
                        <input type="text" maxLength="24"
                               name="query"
                               value={this.state.query}
                               onChange={this.handleChange}/>
                    </label>
                    <br/>
                    
                    <input type="submit" value="Submit"/>
                </form>

                <div>
                    <h3>Available Articles</h3>
                    <h3>
                     <Button onClick={() => this.handleSubmitGetCuriosity(this.state.topic1, this.state.user, this.state.originalQuery)}> {this.state.topic1} </Button>              
                     <Button theme="pink" onClick={() => this.handleSubmitGetCuriosity(this.state.topic2, this.state.user, this.state.originalQuery)}> {this.state.topic2} </Button>
                     <Button theme="green" onClick={() => this.handleSubmitGetCuriosity(this.state.topic3, this.state.user, this.state.originalQuery)}> {this.state.topic3} </Button>
                      </h3>
                </div>
              
              
                   <div>   
                    <iframe width="100%" height="400" src={this.state.curiosityUrl} title="Wikipedia Page"></iframe>
                </div>
      

            </div>
            
            
        );
    
        
    
    }
    
}


export default ChallengeComponent;