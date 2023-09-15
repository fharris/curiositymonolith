import * as React from 'react';


class LastQueryPageComponent extends React.Component {

    render() {
        return (
        <div>
            <table class="center">
                <thead>
                <tr>
                 
                    <th>Original Query</th>
                    <th>Query</th>
                </tr>
                </thead>
                <tbody>
                 {this.props.queryPageList.map(a =>
                    <tr key={a.id}>
         
                        <td>{a.originalQuery}</td>
                        <td>{a.query}</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
           
        );
    }
}

export default LastQueryPageComponent;