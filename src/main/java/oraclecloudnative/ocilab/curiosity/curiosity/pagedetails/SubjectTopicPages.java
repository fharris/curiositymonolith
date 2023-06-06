package oraclecloudnative.ocilab.curiosity.curiosity.pagedetails;


import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * This class represents a page topic replied by Wikipedia API.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectTopicPages {
    @JsonProperty("query")
    private Query query;
}



