package com.bci.desafio.mapper;

import com.bci.desafio.domain.Phone;
import com.bci.desafio.domain.Role;
import com.bci.desafio.domain.User;
import com.bci.desafio.dto.LoginResponseDTO;
import com.bci.desafio.dto.PhoneDTO;
import com.bci.desafio.dto.UserDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public User toUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        user.setRole(Role.ROLE_USER.getValue());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return user;
    }

    public Phone toPhone(PhoneDTO phoneDTO, User user) {
        Phone phone = modelMapper.map(phoneDTO, Phone.class);
        phone.setUser(user);
        return phone;
    }

    public LoginResponseDTO createLoginResponse(User user, List<Phone> phones, String token) {

        return LoginResponseDTO.builder()
                .id(user.getId())
                .created(user.getCreateAt())
                .lastLogin(user.getLastLogin())
                .token(token)
                .phones(phones.stream().map(p -> modelMapper.map(p, PhoneDTO.class)).collect(Collectors.toList()))
                .isActive(user.isActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
