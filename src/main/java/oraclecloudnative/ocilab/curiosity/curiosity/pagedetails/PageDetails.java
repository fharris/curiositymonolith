package oraclecloudnative.ocilab.curiosity.curiosity.pagedetails;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class PageDetails {
    @JsonProperty("pageid")
    private int pageid;
    @JsonProperty("ns")
    private int ns;
    @JsonProperty("title")
    private String title;
    @JsonProperty("index")
    private int index;
}
