package ru.miraq.taskmanagementsystem.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.miraq.taskmanagementsystem.dto.UserDTO;
import ru.miraq.taskmanagementsystem.entity.user.RoleType;
import ru.miraq.taskmanagementsystem.entity.user.UserEntity;
import ru.miraq.taskmanagementsystem.repository.UserRepository;
import ru.miraq.taskmanagementsystem.security.dto.AuthResponseDTO;
import ru.miraq.taskmanagementsystem.security.dto.RefreshTokenRequestDTO;
import ru.miraq.taskmanagementsystem.security.dto.RefreshTokenResponseDTO;
import ru.miraq.taskmanagementsystem.security.entity.RefreshTokenEntity;
import ru.miraq.taskmanagementsystem.security.jwt.JwtUtils;
import ru.miraq.taskmanagementsystem.security.jwt.RefreshTokenService;
import ru.miraq.taskmanagementsystem.security.user.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    public AuthResponseDTO authenticateUser(UserDTO loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDto.getEmail(),
                loginRequestDto.getPassword()
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

    public void register(UserDTO createUserRequest){

        UserEntity user = new UserEntity();
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        user.setRole(List.of(RoleType.ROLE_USER));
        user.setTask(new ArrayList<>());
        user.setTaskInProgress(new ArrayList<>());

        userRepository.save(user);
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

    public void logout(){
        if (SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal() instanceof UserDetailsImpl userDetails){
            Long userId = userDetails.getId();

            refreshTokenService.deleteByUserId(userId);
        }
    }
}
