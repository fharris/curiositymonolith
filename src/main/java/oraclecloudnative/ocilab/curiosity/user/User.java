package oraclecloudnative.ocilab.curiosity.user;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.*;
/**
 * This class represents a User.
 */
//@Getter
//@ToString
//@EqualsAndHashCode
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String userName;

    
}
