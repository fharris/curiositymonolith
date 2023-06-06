package oraclecloudnative.ocilab.curiosity.curiosity;

import java.util.List;

import oraclecloudnative.ocilab.curiosity.curiosity.pagedetails.Page;

public interface CuriosityService {


     /**
     * Returns the topics/pages for a subject 
     *
     * @return the resulting SubjectTopicPages string
     */
    String getTopicsPages(QuerySubjectTopicPages querySubjectTopicPages);

    /**
     * Returns a Page entity
     *
     * @return the resulting Page object
     */
    Page getPage(QueryPageDTO queryPageDTO);
    
    /**
     * Returns the stats for a user 
     * 
     * @param userName the user nickname 
     * @return a list of the last 10 {@link QueryPage} 
     * objects created by the yser
     */
    
    List<QueryPage> getStatsforUser(String userName);

    
}
