package com.bugtracker.api.repository;

import com.bugtracker.api.entity.Project;
import com.bugtracker.api.entity.User;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE owner=:user " +
            "AND (p.owner=?#{ principal?.user } OR ?#{ principal?.user } MEMBER of p.collaborators " +
            "OR p.isPrivate=false OR 1=?#{hasRole('ADMIN')? 1 : 0})")
    Page<Project> findByOwner(User user, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE :user MEMBER of p.collaborators " +
            "AND (p.owner=?#{ principal?.user } OR ?#{ principal?.user } MEMBER of p.collaborators " +
            "OR p.isPrivate=false OR 1=?#{hasRole('ADMIN')? 1 : 0})")
    Page<Project> findByCollaborator(User user, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE " +
            "p.owner=?#{ principal?.user } OR ?#{ principal?.user } MEMBER of p.collaborators " +
            "OR p.isPrivate=false OR 1=?#{hasRole('ADMIN')? 1 : 0}")
    @NonNull Page<Project> findAll(@NonNull Pageable pageable);
}
