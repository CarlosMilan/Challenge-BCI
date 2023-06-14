package com.bci.desafio.controller;

import com.bci.desafio.dto.LoginResponseDTO;
import com.bci.desafio.dto.UserDTO;
import com.bci.desafio.dto.UserResponseDTO;
import com.bci.desafio.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/sing-up", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDTO> userRegister(@Valid @RequestBody UserDTO userDTO) {
        log.info("User to save: {}", userDTO);
        return new ResponseEntity<>(userService.userRegister(userDTO), HttpStatus.CREATED);
    }

    @GetMapping(value = "/login")
    public ResponseEntity<LoginResponseDTO> login(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        LoginResponseDTO responseDTO = userService.login(token);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
