package com.capstone.mind.backend.repositories;

import com.capstone.mind.backend.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailReturnsUser() {
        User u = new User("A","B","repo@example.com","hash");
        userRepository.save(u);

        Optional<User> found = userRepository.findByEmail("repo@example.com");
        assertTrue(found.isPresent());
        assertEquals("repo@example.com", found.get().getEmail());
    }
}