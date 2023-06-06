package oraclecloudnative.ocilab.curiosity.curiosity.pagedetails;
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
public class Page {
    private String id;
    private String key;
    private String title;
    private String source;
    private String keyUrl;
}
