package ru.miraq.taskmanagementsystem.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.miraq.taskmanagementsystem.dto.user.UserDTO;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;
import ru.miraq.taskmanagementsystem.exception.CredentialsPatternException;
import ru.miraq.taskmanagementsystem.mapper.UserMapper;
import ru.miraq.taskmanagementsystem.security.dto.AuthRequestDTO;
import ru.miraq.taskmanagementsystem.entity.user.RoleType;
import ru.miraq.taskmanagementsystem.repository.UserRepository;
import ru.miraq.taskmanagementsystem.security.dto.AuthResponseDTO;
import ru.miraq.taskmanagementsystem.security.dto.RefreshTokenRequestDTO;
import ru.miraq.taskmanagementsystem.security.dto.RefreshTokenResponseDTO;
import ru.miraq.taskmanagementsystem.security.entity.RefreshTokenEntity;
import ru.miraq.taskmanagementsystem.security.jwt.JwtUtils;
import ru.miraq.taskmanagementsystem.security.jwt.RefreshTokenService;
import ru.miraq.taskmanagementsystem.security.user.UserDetailsImpl;
import ru.miraq.taskmanagementsystem.service.UserService;
import ru.miraq.taskmanagementsystem.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService, UserRepository userRepository, UserMapper userMapper, UserServiceImpl userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO authenticateUser(AuthRequestDTO authRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestDTO.getEmail(),
                authRequestDTO.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthResponseDTO.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateJwtToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getUsername())
                .roles(roles)
                .build();
    }


    public void register(AuthRequestDTO authRequestDTO, RoleType role) throws CredentialsPatternException {
        log.info("Попытка регистрации под адресом {}", authRequestDTO.getEmail());
        UserDTO user;
        String emailRegex = ".+@[a-zA-Z]+\\.com|.+@[a-zA-Z]+\\.ru";



        if (!authRequestDTO.getEmail().matches(emailRegex)) {
            throw new CredentialsPatternException("Некорректный формат mail");
        } else if(authRequestDTO.getPassword().isEmpty()
                || authRequestDTO.getPassword().isBlank()
                || authRequestDTO.getPassword().length() < 3){
            throw new CredentialsPatternException("Длина пароля должна быть больше 3 символов");
        } else if ((userService.existByEmail(authRequestDTO.getEmail())
                && userService.getUserByEmail(authRequestDTO.getEmail())
                .getRole()
                .contains(role))){
            throw new CredentialsPatternException("Пользователь с таким email уже существует");
        }

        try {
            user = userService.getUserByEmail(authRequestDTO.getEmail());
            try {
                authenticateUser(authRequestDTO);
            } catch (Exception e){
                throw new CredentialsPatternException(e.getMessage());
            }
            List<RoleType> userRoles = user.getRole();
            if (userRoles.contains(role)) {
                return;
            } else {
                userRoles.add(role);
            }
            user.setRole(userRoles);

            userRepository.save(userMapper.toEntity(user));
        } catch (UsernameNotFoundException e) {
            user = new UserDTO();
            if (role.equals(RoleType.ROLE_EXECUTOR)) {
                user.setRole(List.of(RoleType.ROLE_EXECUTOR));
            } else if (role.equals(RoleType.ROLE_CUSTOMER)) {
                user.setRole(List.of(RoleType.ROLE_CUSTOMER));
            }

            user.setEmail(authRequestDTO.getEmail());
            user.setPassword(passwordEncoder.encode(authRequestDTO.getPassword()));
            user.setTask(new ArrayList<>());
            user.setTaskInProgress(new ArrayList<>());

            userRepository.save(userMapper.toEntity(user));
        }
    }

    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO request){
        String requestRefreshToken = request.getRefreshToken();

        try {
            return refreshTokenService.findByRefreshToken(requestRefreshToken)
                    .map(refreshTokenService::checkRefreshToken)
                    .map(RefreshTokenEntity::getUserId)
                    .map(userId -> {
                        try {
                            UserEntity tokenOwner = userRepository.findById(userId).orElseThrow(Exception::new);
                            String token = jwtUtils.generateTokenFromUsername(tokenOwner.getEmail());
                            return new RefreshTokenResponseDTO(token, refreshTokenService.createRefreshToken(userId).getToken());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }).orElseThrow(() -> new Exception("Refresh token not found"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
