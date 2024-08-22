package ru.miraq.taskmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;
import ru.miraq.taskmanagementsystem.mapper.UserMapper;
import ru.miraq.taskmanagementsystem.repository.UserRepository;



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
                .orElseThrow(() -> new UsernameNotFoundException(email)));
    }

}
