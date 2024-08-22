package ru.miraq.taskmanagementsystem.entity.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "task")
@Entity
public class TaskEntity {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private TaskPriority priority;

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @ManyToOne()
    @JoinColumn(name = "executor_id")
    private UserEntity executor;

    @OneToMany(mappedBy = "task")
    @Column(name = "comments")
    private List<CommentEntity> comments;



}
