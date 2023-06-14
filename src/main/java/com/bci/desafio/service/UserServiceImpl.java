package com.bci.desafio.service;

import com.bci.desafio.domain.Phone;
import com.bci.desafio.domain.User;
import com.bci.desafio.dto.LoginResponseDTO;
import com.bci.desafio.dto.UserDTO;
import com.bci.desafio.dto.UserResponseDTO;
import com.bci.desafio.exceptions.ConstraintsException;
import com.bci.desafio.exceptions.UserAlreadyExistException;
import com.bci.desafio.mapper.UserMapper;
import com.bci.desafio.repository.PhoneRepository;
import com.bci.desafio.repository.UserRepository;
import com.bci.desafio.security.service.UserDetailsServiceImpl;
import com.bci.desafio.security.utils.JWTUtils;
import com.bci.desafio.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;

    public UserResponseDTO userRegister(UserDTO userDTO) {
        log.info("Registering user {}", userDTO);
        UserResponseDTO response = new UserResponseDTO();
        checkPassword(userDTO.getPassword());
        User user = saveUser(userDTO);
        log.info("User registered: {}", user);
        response.setUser(user);
        response.setToken(createToken(user));
        return response;
    }

    private User saveUser(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistException("There is already a user with the email " + user.getEmail());
        }
        List<Phone> phones = userDTO.getPhones().stream()
                .map(p -> userMapper.toPhone(p, user))
                        .collect(Collectors.toList());
        User saveUser = userRepository.save(user);
        phoneRepository.saveAll(phones);
        return saveUser;
    }

    private void checkPassword(String password) {
        if (!checkCapitalsAmount(password)) {
            throw new ConstraintsException("Password must have a capital letter");
        }
        if (!checkNumbersAmount(password)) {
            throw new ConstraintsException("Password must have two numbers");
        }
    }

    private boolean checkCapitalsAmount(String password) {
        Matcher matcher = evaluateRegex(password, Constants.CAPITAL_REGEX);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count == 1;
    }

    private boolean checkNumbersAmount(String password) {
        Matcher matcher = evaluateRegex(password, Constants.NUMBER_REGEX);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count == 2;
    }
    private Matcher evaluateRegex(String content, String regex) {
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(content);
    }

    public LoginResponseDTO login(String token) {
        String username = jwtUtils.getUsername(token);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        List<Phone> phones = phoneRepository.findByUser(user);
        String newToken = createToken(user);
        user.setLastLogin(LocalDateTime.now());
        return userMapper.createLoginResponse(userRepository.save(user), phones, newToken);
    }

    private String createToken(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return jwtUtils.generateToken(userDetails);
    }

}
