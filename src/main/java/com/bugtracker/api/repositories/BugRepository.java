package com.bugtracker.api.repositories;

import com.bugtracker.api.entities.Bug;
import com.bugtracker.api.entities.Project;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BugRepository extends JpaRepository<Bug, Long> {

    @Query("SELECT b FROM Bug b LEFT JOIN b.project p WHERE p=:project " +
            "AND p.owner=?#{ principal?.user } OR ?#{ principal?.user } MEMBER of p.collaborators " +
            "OR p.isPrivate=false OR 1=?#{hasRole('ADMIN')? 1 : 0}")
    Page<Bug> findByProject(Project project, Pageable pageable);

    @Query("SELECT b FROM Bug b LEFT JOIN b.project p WHERE " +
            "p.owner=?#{ principal?.user } OR ?#{ principal?.user } MEMBER of p.collaborators " +
            "OR p.isPrivate=false OR 1=?#{hasRole('ADMIN')? 1 : 0}")
    @NonNull Page<Bug> findAll(@NonNull Pageable pageable);
}
