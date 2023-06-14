package com.bci.desafio.repository;

import com.bci.desafio.domain.Phone;
import com.bci.desafio.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {
    List<Phone> findByUser(User user);
}
