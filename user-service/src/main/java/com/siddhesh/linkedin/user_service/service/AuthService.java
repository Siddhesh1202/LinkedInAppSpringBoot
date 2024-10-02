package com.siddhesh.linkedin.user_service.service;

import com.siddhesh.linkedin.user_service.dto.LoginRequestDto;
import com.siddhesh.linkedin.user_service.dto.SignupRequestDto;
import com.siddhesh.linkedin.user_service.dto.UserDto;
import com.siddhesh.linkedin.user_service.entity.User;
import com.siddhesh.linkedin.user_service.exceptions.BadRequestException;
import com.siddhesh.linkedin.user_service.exceptions.ResourceNotFoundException;
import com.siddhesh.linkedin.user_service.repository.UserRepository;
import com.siddhesh.linkedin.user_service.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JWTService jwtService;
    public UserDto signup(SignupRequestDto signupRequestDto) {
        boolean exist = userRepository.existsByEmail(signupRequestDto.getEmail());
        if(exist) {
            throw new BadRequestException("Email already exists");
        }
        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }


    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        boolean passwordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());
        if (!passwordMatch) {
            throw new BadRequestException("Invalid Password");
        }

        return jwtService.generateAccessToken(user);
    }
}
