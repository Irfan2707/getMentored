package com.nineleaps.authentication.jwt.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nineleaps.authentication.jwt.enums.NoteVisibility;
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
@Table(name = "notes")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(length = 5000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoteVisibility visibility;
    @JsonIgnoreProperties("notes")

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "engagement_id")
    private Engagement engagement;

    private LocalDateTime createdTime;


    @Column(name = "updated_time")
    private LocalDateTime updatedTime;
    @Column(name = "is_deleted")
    private Boolean deleted = false;
}
		   
		   
	

