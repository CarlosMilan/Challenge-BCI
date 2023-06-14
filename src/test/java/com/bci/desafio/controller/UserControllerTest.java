package com.bci.desafio.controller;

import com.bci.desafio.datos.Data;
import com.bci.desafio.domain.User;
import com.bci.desafio.dto.UserDTO;
import com.bci.desafio.exceptions.ConstraintsException;
import com.bci.desafio.exceptions.UserAlreadyExistException;
import com.bci.desafio.repository.PhoneRepository;
import com.bci.desafio.repository.UserRepository;
import com.bci.desafio.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("User controller tests")
class UserControllerTest {

    private MockMvc mvc;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private PhoneRepository phoneRepository;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new ExceptionHandlerController())
                .build();
        this.mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Sing Up")
    void singU1pTest() throws Exception {

        when(userService.userRegister(any())).thenReturn(Data.createUserResponseDTO());

        UserDTO userDTO = Data.createUserDTO1().get();
        mvc.perform(post("/users/sing-up").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.user.name").value("Charlie"))
                .andExpect(jsonPath("$.user.id").exists())
                .andExpect(jsonPath("$.user.email").value("charlie_01@correo.com"));
    }

    @Test
    @DisplayName("Sing Up - User Already exists")
    void singUp2Test() throws Exception {
        UserDTO userDTO = Data.createUserDTO1().get();
        when(userService.userRegister(any())).thenThrow(
                new UserAlreadyExistException("There is already a user with the email " + userDTO.getEmail()));

        mvc.perform(post("/users/sing-up").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.detail").value("There is already a user with the email " + userDTO.getEmail()));

    }

    @Test
    @DisplayName("Sing Up - ConstraintException 1")
    void singUpException1Test() throws Exception {

        UserDTO userDTO = Data.createUserDTO1().get();
        userDTO.setPassword("nocapsgg12");

        when(userService.userRegister(any())).thenThrow(
                new ConstraintsException("Password must have a capital letter"));

        mvc.perform(post("/users/sing-up").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.detail").value("Password must have a capital letter"));

    }

    @Test
    @DisplayName("Sing Up - ConstraintException 2")
    void singUpException2Test() throws Exception {

        UserDTO userDTO = Data.createUserDTO1().get();

        when(userService.userRegister(any())).thenThrow(
                new ConstraintsException("Password must have two numbers"));

        userDTO.setPassword("Nonumbers");
        mvc.perform(post("/users/sing-up").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.detail").value("Password must have two numbers"));
    }

    @Test
    @DisplayName("Sing Up - MethodArgumentNotValidException 1")
    void singUpException3Test() throws Exception {

        UserDTO userDTO = Data.createUserDTO1().get();
        userDTO.setPassword("Numb12");
        mvc.perform(post("/users/sing-up").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$[0].timestamp").exists())
                .andExpect(jsonPath("$[0].detail").value("password : The password must be between 8 and 12 characters"));

    }

    @Test
    @DisplayName("Sing Up - MethodArgumentNotValidException 2")
    void singUpException4Test() throws Exception {

        UserDTO userDTO = Data.createUserDTO1().get();
        userDTO.setPassword("Numb12");
        userDTO.setEmail("test_correo.com");
        userDTO.getPhones().get(0).setCountryCode(null);
        userDTO.getPhones().get(0).setNumber(null);
        userDTO.getPhones().get(1).setNumber(null);
        userDTO.getPhones().get(1).setCityCode(null);
        mvc.perform(post("/users/sing-up").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$[0].timestamp").exists())
                .andExpect(jsonPath("$[0].detail").exists())
                .andExpect(jsonPath("$[1].code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$[1].timestamp").exists())
                .andExpect(jsonPath("$[1].detail").exists());
    }


    @Test
    @DisplayName("Login")
    void loginTest() throws Exception {
        UUID id = UUID.fromString("4563b561-8b7e-43a4-a918-ac8bcbab8990");
        String token = "Token viejo";
        when(userService.login(token)).thenReturn(Data.createLoginResponse());

        mvc.perform(get("/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Charlie"))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.email").value("charlie_01@correo.com"));

    }

    @Test
    @DisplayName("Login failed")
    void loginFailedTest() throws Exception {
        UUID id = UUID.fromString("4563b561-8b7e-43a4-a918-ac8bcbab8990");
        String token = "Token viejo";
        when(userService.login(token)).thenThrow(new UsernameNotFoundException("Username not found"));

        mvc.perform(get("/users/login").header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.detail").value("Username not found"));

    }
}
