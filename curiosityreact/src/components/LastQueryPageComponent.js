import * as React from 'react';


class LastQueryPageComponent extends React.Component {

    render() {
        return (
        <div>
            <table class="center">
                <thead>
                <tr>
                    <th>#</th>
                    <th>Query</th>
                    <th>Article</th>
                </tr>
                </thead>
                <tbody>
                 {this.props.queryPageList.map(a =>
                    <tr key={a.id}>
                        <td>{a.id}</td>
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