package oraclecloudnative.ocilab.curiosity.curiosity;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface QueryPageRepository extends CrudRepository<QueryPage, Long>{
    /** 
     * @return the last 10 curiosities for a given user, identified by their userName
    */
    List<QueryPage> findTop10ByUserUserNameOrderByIdDesc(String userName);
}
