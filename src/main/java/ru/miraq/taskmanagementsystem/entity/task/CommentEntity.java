package ru.miraq.taskmanagementsystem.entity.task;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;

@Setter
@Getter
@Table(name = "comment")
@Entity
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "author_id")
    private UserEntity author;

    @Column(name = "text")
    private String text;

    @ManyToOne()
    @JoinColumn(name = "task_id")
    private TaskEntity task;

}
