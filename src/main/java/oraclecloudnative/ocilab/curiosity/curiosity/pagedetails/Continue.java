package oraclecloudnative.ocilab.curiosity.curiosity.pagedetails;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Continue{
    @JsonProperty("gsroffset")
    private int gsroffset;
    @JsonProperty("continue")
    private String continue1;
}