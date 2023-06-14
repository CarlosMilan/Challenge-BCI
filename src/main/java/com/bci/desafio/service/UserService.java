package com.bci.desafio.service;

import com.bci.desafio.domain.User;
import com.bci.desafio.dto.LoginResponseDTO;
import com.bci.desafio.dto.UserDTO;
import com.bci.desafio.dto.UserResponseDTO;

import java.util.UUID;

public interface UserService {
    User getUser(UUID id);
    UserResponseDTO userRegister(UserDTO userDTO);
    LoginResponseDTO login(String token);
}
