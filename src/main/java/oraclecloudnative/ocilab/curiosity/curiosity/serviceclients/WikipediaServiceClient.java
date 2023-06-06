package oraclecloudnative.ocilab.curiosity.curiosity.serviceclients;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import oraclecloudnative.ocilab.curiosity.curiosity.QueryPageDTO;
import oraclecloudnative.ocilab.curiosity.curiosity.pagedetails.Page;




@Slf4j
@Service
public class WikipediaServiceClient {
    private RestTemplate restTemplate;
    private String host;
    private Page page;

    
    public WikipediaServiceClient(final RestTemplateBuilder builder, @Value("${service.wikipedia.host}") final String wikipediaHostUrl) {
        
        restTemplate = builder.build();
        this.host = wikipediaHostUrl;
    }

    /* Returns an object Page with with the wikipedia page*/
    public Page sendQueryPage(QueryPageDTO queryPageDTO){
      log.info("Inside sendQueryPage");
      try {
         page = restTemplate.getForObject(host+"/"+queryPageDTO.getQuery(), Page.class);
         page.setKeyUrl("https://www.wikipedia.org/wiki/"+page.getKey());
         return page;
      } catch (Exception e) { //https://stackoverflow.com/questions/23205213/how-to-extract-http-status-code-from-the-resttemplate-call-to-a-url
          log.error("There was an error trying to reach wikipedia", e);
          return null;
      }
    }
    /* Returns a Json string with all the articles available*/
    public String sendSubjectTopicPages(String gsrSearch){
        log.info("inside sendSubjectTopicPages");
        String gsrlimit = "3";
        String url = "https://en.wikipedia.org/w/api.php?action=query&origin=*&format=json&generator=search&gsrnamespace=0&gsrlimit=" + gsrlimit + "&gsrsearch=" + gsrSearch;    
        RestTemplate restTemplate2 = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<HttpHeaders> entity = new HttpEntity<HttpHeaders>(headers);
        ResponseEntity<String> sResponseEntity = restTemplate2.exchange(url, HttpMethod.GET, entity, String.class );
        System.out.println("HTTP STATUS CODE: " + sResponseEntity.getStatusCode());
        System.out.println("RESPONSE BODY: " +   sResponseEntity.getBody());
        // As I tested if gsroffset is present, we can also try to identify and count pageid in the String body
        // to know how many pages were found.
        if (sResponseEntity.getBody().contains("gsroffset")){
            System.out.println("TRUE");
            String str = sResponseEntity.getBody();
            String strFind = "pageid";
            int count = 0, fromIndex = 0;
            while ((fromIndex = str.indexOf(strFind, fromIndex)) != -1 ){
                System.out.println("Found at index: " + fromIndex + "  counter: " + count);
                count++;
                fromIndex++;
            }
            if (count < 2){ //this means that to respond back to the client, we need at least to have 3 (i.e. count=2) references to "pageid"
                throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR); 
            } 

        }else{
            log.error("gsroffset is not present in %s", HttpStatus.INTERNAL_SERVER_ERROR);
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);  
        }
  
        return sResponseEntity.getBody();
    }
    
}
