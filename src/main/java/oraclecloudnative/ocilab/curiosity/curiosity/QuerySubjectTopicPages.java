package oraclecloudnative.ocilab.curiosity.curiosity;
import java.time.LocalTime;

import lombok.*;
/**
 * This class represents a Curiosity replied by Wikipedia API.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class QuerySubjectTopicPages {
    private String userName;
    private String query;
    private LocalTime queryTime;
}
