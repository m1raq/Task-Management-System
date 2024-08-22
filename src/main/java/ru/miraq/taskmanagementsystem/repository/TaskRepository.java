package ru.miraq.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    TaskEntity findByName(String name);

    List<TaskEntity> findByAuthorEmailNot(String email);

    List<TaskEntity> findByAuthorEmail(String email);

    List<TaskEntity> findByExecutorEmail(String email);

}
