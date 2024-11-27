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
@Table(name = "activity_logs", indexes = {
        @Index(name = "idx_checklist_item_id", columnList = "checklist_item_id")
})
public class ActivityLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_type")
    private String activityType;

    @Column(name = "activity_time")
    private LocalDateTime activityTime;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "checklist_item_id")
    private ChecklistItem checklistItem;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


}

