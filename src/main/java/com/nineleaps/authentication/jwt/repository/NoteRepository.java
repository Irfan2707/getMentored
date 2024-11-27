package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.entity.Note;
import com.nineleaps.authentication.jwt.enums.NoteVisibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {


    List<Note> findByUserIdAndEngagementIdAndVisibility(Long userId, Long engagementId, NoteVisibility visibility);


    List<Note> findByEngagementIdAndVisibility(Long engagementId, NoteVisibility visibility);

    List<Note> findByEngagementIdAndVisibilityAndDeletedFalse(Long engagementId, NoteVisibility visibility);


}
