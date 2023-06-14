package com.bci.desafio.dto;

import com.bci.desafio.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {
    private User user;
    private String token;
}
