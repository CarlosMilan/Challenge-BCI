package com.bci.desafio.service;

import com.bci.desafio.dto.LoginResponseDTO;
import com.bci.desafio.dto.UserDTO;
import com.bci.desafio.dto.UserResponseDTO;


public interface UserService {
    UserResponseDTO userRegister(UserDTO userDTO);
    LoginResponseDTO login(String token);
}
