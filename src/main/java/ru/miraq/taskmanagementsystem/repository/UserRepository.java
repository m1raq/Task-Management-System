package ru.miraq.taskmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    void deleteByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

}
