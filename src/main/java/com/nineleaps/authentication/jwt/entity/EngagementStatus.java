package com.nineleaps.authentication.jwt.entity;

import com.nineleaps.authentication.jwt.enums.EngStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "engagement_status")
public class EngagementStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "engagement_status_id")
    private Long engagementStatusId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "engagement_id")
    private Engagement engagement;
    @Enumerated(EnumType.STRING)
    private EngStatus mentorEngStatus;
    @Enumerated(EnumType.STRING)
    private EngStatus menteeEngStatus;
    @Column(name = "mentor_status_timestamp")
    private LocalDateTime mentorStatusTimestamp;

    @Column(name = "mentee_status_timestamp")
    private LocalDateTime menteeStatusTimestamp;

    @Column(name = "Completed_Eng_Timestamp")
    private LocalDateTime completedEngStatusTimestamp;


    public void setMentorEngStatus(EngStatus mentorEngStatus) {
        if (this.mentorEngStatus != mentorEngStatus) {
            this.mentorEngStatus = mentorEngStatus;
            this.mentorStatusTimestamp = LocalDateTime.now();
        }
    }


    public void setMenteeEngStatus(EngStatus menteeEngStatus) {
        if (this.menteeEngStatus != menteeEngStatus) {
            this.menteeEngStatus = menteeEngStatus;
            this.menteeStatusTimestamp = LocalDateTime.now();

        }
    }
}
	










