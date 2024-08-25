package ru.miraq.taskmanagementsystem.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.miraq.taskmanagementsystem.dto.user.GetUserDTO;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;
import ru.miraq.taskmanagementsystem.entity.user.RoleType;
import ru.miraq.taskmanagementsystem.exception.InputParamException;
import ru.miraq.taskmanagementsystem.mapper.UserMapper;
import ru.miraq.taskmanagementsystem.repository.UserRepository;

import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userMapper.toDTO(userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь " + email + " не найден")));
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<GetUserDTO> getUsersBySortType(String sortType) {
        return switch (sortType) {
            case "0" -> userRepository.findAll()
                    .stream()
                    .map(user -> GetUserDTO.builder()
                            .role(user.getRole())
                            .email(user.getEmail())
                            .build())
                    .toList();
            case "1" -> userRepository.findAll()
                    .stream()
                    .filter(user -> user.getRole().contains(RoleType.ROLE_CUSTOMER))
                    .map(user -> GetUserDTO.builder()
                            .role(user.getRole())
                            .email(user.getEmail())
                            .build())
                    .toList();
            case "2" -> userRepository.findAll()
                    .stream()
                    .filter(user -> user.getRole().contains(RoleType.ROLE_EXECUTOR))
                    .map(user -> GetUserDTO.builder()
                            .role(user.getRole())
                            .email(user.getEmail())
                            .build())
                    .toList();
            default -> throw new InputParamException("Неверный тип сортировки");
        };

    }

}
