package oraclecloudnative.ocilab.curiosity.curiosity.pagedetails;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Query {
    @JsonProperty("pages")
    private Pages pages;
}
