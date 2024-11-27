package com.nineleaps.authentication.jwt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Engagement_handling")
public class Engagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eng_id")
    private Long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "connection_request_id")
    private ConnectionRequest connectionRequest;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "duration_hours")
    private int durationHours;

    @OneToOne(mappedBy = "engagement", fetch = FetchType.EAGER)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private GoalTracker goalTracker;

    @OneToMany(mappedBy = "engagement", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EngagementStatus> engagementStatuses = new ArrayList<>();


}
