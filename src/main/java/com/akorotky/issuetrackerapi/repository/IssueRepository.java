package com.akorotky.issuetrackerapi.repository;

import com.akorotky.issuetrackerapi.entity.Issue;
import com.akorotky.issuetrackerapi.entity.Project;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IssueRepository extends JpaRepository<Issue, Long> {

    @Query("SELECT b FROM Issue b LEFT JOIN b.project p WHERE p=:project " +
            "AND p.owner=?#{ principal?.user } OR ?#{ principal?.user } MEMBER of p.collaborators " +
            "OR p.isPrivate=false OR 1=?#{hasRole('ADMIN')? 1 : 0}")
    Page<Issue> findByProject(Project project, Pageable pageable);

    @Query("SELECT b FROM Issue b LEFT JOIN b.project p WHERE " +
            "p.owner=?#{ principal?.user } OR ?#{ principal?.user } MEMBER of p.collaborators " +
            "OR p.isPrivate=false OR 1=?#{hasRole('ADMIN')? 1 : 0}")
    @NonNull Page<Issue> findAll(@NonNull Pageable pageable);
}
