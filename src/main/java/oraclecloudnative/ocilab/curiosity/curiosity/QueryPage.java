package oraclecloudnative.ocilab.curiosity.curiosity;

import lombok.*;
import oraclecloudnative.ocilab.curiosity.user.User;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * This class represents a Query to be send to the Wikipedia API - besides the
 * query, saves the user sending it and the time
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryPage { //ChallengeAttempt
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    private String originalQuery;
    private String query;
}
