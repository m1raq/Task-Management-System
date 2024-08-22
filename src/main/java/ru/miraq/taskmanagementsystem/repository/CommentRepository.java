package ru.miraq.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miraq.taskmanagementsystem.entity.task.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
