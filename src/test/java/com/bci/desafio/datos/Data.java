package com.bci.desafio.datos;

import com.bci.desafio.domain.Phone;
import com.bci.desafio.domain.User;
import com.bci.desafio.dto.LoginResponseDTO;
import com.bci.desafio.dto.PhoneDTO;
import com.bci.desafio.dto.UserDTO;
import com.bci.desafio.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Data {

    public static Optional<User> createUser1() {
        User user = new User();
        user.setName("Charlie");
        user.setLastLogin(LocalDateTime.now());
        user.setCreateAt(LocalDateTime.now());
        user.setEmail("charlie_01@correo.com");
        user.setPassword("Jajaja57b");
        user.setRole("USER");
        user.setActive(true);
        return Optional.of(user);
    }

    public static List<Phone> createPhoneList(User user) {
        Phone phone1 = new Phone();
        phone1.setUser(user);
        phone1.setNumber(345790145L);
        phone1.setCityCode(261);
        phone1.setCountryCode("+54");

        Phone phone2 = new Phone();
        phone2.setUser(user);
        phone2.setNumber(155789456L);
        phone2.setCityCode(262);
        phone2.setCountryCode("+54");

        return Arrays.asList(phone1, phone2);
    }

    public static List<PhoneDTO> createPhoneDTOList() {
        PhoneDTO phone1 = new PhoneDTO();
        phone1.setNumber(345790145L);
        phone1.setCityCode(261);
        phone1.setCountryCode("+54");

        PhoneDTO phone2 = new PhoneDTO();
        phone2.setNumber(155789456L);
        phone2.setCityCode(262);
        phone2.setCountryCode("+54");

        return Arrays.asList(phone1, phone2);
    }

    public static Optional<UserDTO> createUserDTO1() {
        UserDTO user = new UserDTO();
        user.setName("Charlie");

        user.setEmail("charlie_01@correo.com");
        user.setPassword("Jajaja57b");

        PhoneDTO phone1 = new PhoneDTO();

        phone1.setNumber(345790145L);
        phone1.setCityCode(261);
        phone1.setCountryCode("+54");

        PhoneDTO phone2 = new PhoneDTO();
        phone2.setNumber(155789456L);
        phone2.setCityCode(262);
        phone2.setCountryCode("+54");

        user.setPhones(Arrays.asList(phone1, phone2));
        return Optional.of(user);
    }

    public static UserResponseDTO createUserResponseDTO() {
        User user = createUser1().get();
        UUID id = UUID.fromString("4563b561-8b7e-43a4-a918-ac8bcbab8990");
        user.setId(id);
        return new UserResponseDTO(user, "token");
    }

    public static LoginResponseDTO createLoginResponse() {
        User user = createUser1().get();
        UUID id = UUID.fromString("4563b561-8b7e-43a4-a918-ac8bcbab8990");
        return LoginResponseDTO.builder()
                .id(id)
                .created(user.getCreateAt())
                .lastLogin(LocalDateTime.now())
                .token("Token nuevo")
                .isActive(user.isActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phones(createPhoneDTOList())
                .build();
    }
}

