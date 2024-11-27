package com.nineleaps.authentication.jwt.dto;

import com.nineleaps.authentication.jwt.enums.ChecklistitemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistItemDTO {
    private Long id;
    private Long goalTrackerId;
    @NotBlank(message = "Item description must not be blank")
    @Size(max = 255, message = "Item description must not exceed 255 characters")
    private String itemDescription;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private ChecklistitemStatus status;


}
