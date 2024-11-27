package com.nineleaps.authentication.jwt.entity;

import com.nineleaps.authentication.jwt.enums.ConnectionRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_mentee_id", columnList = "mentee_id"),
        @Index(name = "idx_mentor_id", columnList = "mentor_id")
})
public class ConnectionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "connections_id")
    private Long id;

    @NotNull(message = "Mentee must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentee_id", referencedColumnName = "id")
    private User mentee;

    @NotNull(message = "Mentor must not be null")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mentor_id", referencedColumnName = "id")
    private User mentor;

    @NotBlank(message = "Message must not be blank")
    private String message;

    @Enumerated(EnumType.STRING)
    private ConnectionRequestStatus status;

    @NotNull(message = "Request time must not be null")
    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Column(name = "acceptance_time")
    private LocalDateTime acceptanceTime;

    @Column(name = "rejection_time")
    private LocalDateTime rejectionTime;

    @ElementCollection
    @CollectionTable(name = "recommended_mentors",
            joinColumns = @JoinColumn(name = "connection_request_id"))
    @Column(name = "mentor_id")
    private List<Long> recommendedMentors;


}
