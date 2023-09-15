class ApiClient {


    // Build FrontEnd
    // npm run build
    // As the frontend is going to run statically in the Curiosity application, no references to Servers are needed. 

    static GET_PAGE = '/wiki/curiosity/page';

    static GET_TOPICS = '/wiki/curiosity/topics'; 

    static GET_STATS = '/wiki/curiosity/stats?userName=';


    static curiosity(user: string,
                     query: string,
                     originalQuery: string): Promise<Response> {

        return fetch(ApiClient.GET_PAGE + "/" + user + "/" + query + "/" + originalQuery ); 
    }

    static getTopics(user: string,
                     query: string): Promise<Response> {

        return fetch(ApiClient.GET_TOPICS + "/" + user + "/" + query); 
    }

    static getStats(userName: string): Promise<Response> {

        console.log("userName for Stats :"+userName);

         return fetch(ApiClient.GET_STATS + userName); 
    }
    

}

export default ApiClient;
