package com.nineleaps.authentication.jwt.entity;

import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "checklist_items", indexes = {
        @Index(name = "idx_goal_tracker_id", columnList = "goal_tracker_id")
})
public class ChecklistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Goal tracker must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "goal_tracker_id")
    private GoalTracker goalTracker;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChecklistitemStatus status;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @NotBlank(message = "Item description must not be blank")
    @Size(max = 255, message = "Item description must not exceed 255 characters")
    @Column(name = "item_description")
    private String itemDescription;

    @OneToMany(mappedBy = "checklistItem", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ActivityLog> activityLogs;

    @Column(name = "is_deleted")
    private Boolean deleted = false;

    public void addActivityLog(ActivityLog activityLog) {
        if (activityLogs == null) {
            activityLogs = new ArrayList<>();
        }
        activityLogs.add(activityLog);
        activityLog.setChecklistItem(this);
    }


}
