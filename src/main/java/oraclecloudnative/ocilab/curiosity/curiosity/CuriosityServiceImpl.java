package oraclecloudnative.ocilab.curiosity.curiosity;


import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import oraclecloudnative.ocilab.curiosity.curiosity.pagedetails.Page;
import oraclecloudnative.ocilab.curiosity.curiosity.serviceclients.WikipediaServiceClient;
import oraclecloudnative.ocilab.curiosity.user.User;
import oraclecloudnative.ocilab.curiosity.user.UserRepository;


@Slf4j
@RequiredArgsConstructor
@Service
public class CuriosityServiceImpl implements CuriosityService{

    private Page page;
    private final WikipediaServiceClient wikipediaServiceClient;
    private final UserRepository userRepository;
    private final QueryPageRepository queryPageRepository;
 


    @Override
    public Page getPage(QueryPageDTO queryPageDTO) {
        
        log.info("preparing to call wiki client to get a page");
        try {
            page = wikipediaServiceClient.sendQueryPage(queryPageDTO);
            log.info(page.getKeyUrl());
            log.info(page.getTitle());
            
            log.info(queryPageDTO.getUserName());
            
            log.info(queryPageDTO.getOriginalQuery());  
            //Curiosity curiosity = new Curiosity(null, queryPage.getUserName(), queryPage.getQuery());


            User user = userRepository.findByUserName(queryPageDTO.getUserName()).orElseGet(() -> {
                log.info("Creating new user with user_name {}", 
                                queryPageDTO.getUserName());
                return userRepository.save(new User(null, queryPageDTO.getUserName()));
            });
            
            QueryPage queryPage = new QueryPage(null, user, queryPageDTO.getOriginalQuery(), queryPageDTO.getQuery());

            //QueryPage curiosityPage = 
            queryPageRepository.save(queryPage);
            
            log.info("If everything is OK then persist in DB a queryPage object");
            return page;

        } catch (Exception e) {
            log.error("something went wrong", e);
            return null;
        }

    }


    @Override
    public String getTopicsPages(QuerySubjectTopicPages querySubjectTopicPages) {
        log.info("preparing to call wiki client to get a pages for a subject");
        log.info(querySubjectTopicPages.getQueryTime().toString());
        log.info(querySubjectTopicPages.getQuery());
        log.info(querySubjectTopicPages.getUserName());

        try {
           String response = wikipediaServiceClient.sendSubjectTopicPages(querySubjectTopicPages.getQuery());
            log.info("gsroffset :" + response);
            log.info("If everything is OK then persist in DB a the topics as a string in a new table to be created");
            return response;

        } catch (Exception e) {
            log.error("The wikipedia service client is reporting a problem", e);
            return "null-empty response";
        }
    }


    @Override
    public List<QueryPage> getStatsforUser(final String userName) {
        return queryPageRepository.findTop10ByUserUserNameOrderByIdDesc(userName);
    }

    
    
}
