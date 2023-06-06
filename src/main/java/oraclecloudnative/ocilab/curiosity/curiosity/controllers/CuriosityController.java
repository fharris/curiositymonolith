package oraclecloudnative.ocilab.curiosity.curiosity.controllers;

import java.time.LocalTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oraclecloudnative.ocilab.curiosity.curiosity.CuriosityService;
import oraclecloudnative.ocilab.curiosity.curiosity.QueryPage;
import oraclecloudnative.ocilab.curiosity.curiosity.QueryPageDTO;
import oraclecloudnative.ocilab.curiosity.curiosity.QuerySubjectTopicPages;
import oraclecloudnative.ocilab.curiosity.curiosity.pagedetails.Page;
import oraclecloudnative.ocilab.curiosity.user.User;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/wiki")
public class CuriosityController {

   private final CuriosityService curiosityService;
  
 

    @GetMapping("/curiosity/topics/{userName}/{query}")
    String getTopicsPages(@PathVariable String query, @PathVariable String userName){
        
        log.info("inside /curiosity/topics/"+userName+"/"+query);
        User curiuser = new User(null, userName);
        System.out.println("CURIUSER :" + curiuser.getUserName());
        QuerySubjectTopicPages querySubjectTopicPages = new QuerySubjectTopicPages(userName, query, LocalTime.now());
        return curiosityService.getTopicsPages(querySubjectTopicPages); 
    }

    @GetMapping("/curiosity/page/{userName}/{query}/{originalQuery}")
    Page getPage(@PathVariable String query, @PathVariable String userName, @PathVariable String originalQuery){
        
        log.info("inside /curiosity/page/"+userName+"/"+query+"/"+originalQuery);

        QueryPageDTO queryPageDTO = new QueryPageDTO(userName, originalQuery, query);

        return curiosityService.getPage(queryPageDTO);
    }

    @GetMapping("/curiosity/stats")
    ResponseEntity<List<QueryPage>> getStats(@RequestParam("userName") String userName){
        return ResponseEntity.ok(
                    curiosityService.getStatsforUser(userName)
        );
    }


    
}
