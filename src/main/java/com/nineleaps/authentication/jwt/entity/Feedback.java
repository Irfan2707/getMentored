package com.nineleaps.authentication.jwt.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentee_id")
    private User mentee;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id")
    private User mentor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "engagement_id")
    private Engagement engagement;
    private Double mentorRating;
    private Double menteeRating;
    @Column(length = 500)
    private String mentorFeedback;
    private String menteeFeedback;
    private Double engagementRating;
    @Column(length = 500)
    private String engagementFeedback;
    private String feedbackFromUserName;
    private LocalDateTime createdTime;

}
