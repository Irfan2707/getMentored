package com.nineleaps.authentication.jwt.dto;

import com.nineleaps.authentication.jwt.enums.NoteVisibility;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NoteDTO {
    @NotNull
    private Long userId;

    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private NoteVisibility visibility;

    @NotNull
    private Long engagementId;

    @NotNull
    private LocalDateTime createdTime;

    @NotNull
    private LocalDateTime updatedTime;


}