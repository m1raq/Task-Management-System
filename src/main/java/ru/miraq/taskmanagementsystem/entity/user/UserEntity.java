package ru.miraq.taskmanagementsystem.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.miraq.taskmanagementsystem.entity.task.CommentEntity;
import ru.miraq.taskmanagementsystem.entity.task.TaskEntity;

import java.util.List;

@Setter
@Getter
@Table(name = "users")
@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @CollectionTable(name = "user_role")
    @ElementCollection(targetClass = RoleType.class)
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.JOIN)
    @Column(name = "role")
    private List<RoleType> role;

    @OneToMany(mappedBy = "author")
    private List<TaskEntity> task;

    @OneToMany(mappedBy = "executor")
    private List<TaskEntity> taskInProgress;

    @OneToMany(mappedBy = "author")
    private List<CommentEntity> comments;
}
