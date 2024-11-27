package com.nineleaps.authentication.jwt.repository;

import com.nineleaps.authentication.jwt.entity.ChecklistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {


    List<ChecklistItem> findAllByGoalTrackerId(Long goalTrackerId);

    Optional<ChecklistItem> findById(Long id);

    Page<ChecklistItem> findAllByGoalTrackerId(Long goalTrackerId, Pageable pageable);

}
