package oraclecloudnative.ocilab.curiosity.curiosity;
import javax.validation.constraints.*;

import lombok.Value;


/**
 * Attempt/Query coming from the user to get a page
 */
@Value
public class QueryPageDTO {
    @NotBlank
    private String userName;
    @NotBlank
    private String originalQuery;
    @NotBlank
    private String query;
    
}
