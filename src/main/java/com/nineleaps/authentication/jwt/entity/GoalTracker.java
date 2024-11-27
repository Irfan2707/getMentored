package com.nineleaps.authentication.jwt.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "goal_tracking")
public class GoalTracker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "engagement_id")
    private Engagement engagement;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "goalTracker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChecklistItem> checklistItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "goal_tracker_start_time")
    private LocalDateTime goalTrackerStartTime;

    @Column(name = "updated_time")
    private LocalDateTime updatedTime;


    @Column(name = "is_deleted")
    private Boolean deleted = false;


}
