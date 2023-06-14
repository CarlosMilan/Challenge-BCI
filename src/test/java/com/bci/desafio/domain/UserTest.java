package com.bci.desafio.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("User Tests")
class UserTest {

    @Test
    @DisplayName("Test de creacion de usuarios y telefonos")
    void userCreationTest() {

        LocalDateTime now = LocalDateTime.now();

        User user = new User();
        user.setName("Charlie");
        user.setEmail("asd");
        user.setLastLogin(now);
        user.setCreateAt(now);
        user.setEmail("charlie_01@correo.com");
        user.setPassword("Jajaja57b");
        user.setRole("USER");
        user.setActive(true);

        Phone phone1 = new Phone();
        phone1.setId(UUID.randomUUID());
        phone1.setUser(user);
        phone1.setNumber(345790145L);
        phone1.setCityCode(261);
        phone1.setCountryCode("+54");

        //user.setPhones(Arrays.asList(phone1, phone2));

        user.init();

        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals("charlie_01@correo.com", user.getEmail()),
                () -> assertEquals("Charlie", user.getName()),
                () -> assertEquals("Jajaja57b", user.getPassword()),
                () -> assertEquals("USER", user.getRole()),
                () -> assertNotNull(user.getCreateAt()),
                () -> assertNotNull(user.getLastLogin()),
                () -> assertTrue(user.isActive()),
                () -> assertEquals(345790145L, phone1.getNumber()),
                () -> assertEquals(261, phone1.getCityCode()),
                () -> assertEquals("+54", phone1.getCountryCode()),
                () -> assertEquals(user, phone1.getUser()),
                () -> assertNotNull(phone1.getId())
        );

    }
}
