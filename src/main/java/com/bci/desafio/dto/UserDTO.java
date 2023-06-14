package com.bci.desafio.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UserDTO {

    private String name;

    @NotNull(message = "The email cannot be empty")
    @Email(regexp = ".+@.+\\..+", message = "The email has an incorrect format")
    private String email;

    @NotNull(message = "The password cannot be empty")
    @Size(min = 8, max = 12, message = "The password must be between 8 and 12 characters")
    private String password;

    private List<@Valid PhoneDTO> phones;
}
