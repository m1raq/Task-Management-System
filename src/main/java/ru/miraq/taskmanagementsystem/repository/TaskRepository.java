package ru.miraq.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByName(String name);

    Optional<List<TaskEntity>> findByAuthorEmailNot(String email);

    Optional<List<TaskEntity>> findByAuthorEmail(String email);

    Optional<List<TaskEntity>> findByExecutorEmail(String email);

}
