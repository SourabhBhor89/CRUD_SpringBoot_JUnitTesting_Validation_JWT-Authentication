package com.crudapp.repository;

import com.crudapp.entity.UserEntity;
import com.crudapp.repo.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    @Rollback(value = false) // Set to false if you want the user to persist for other tests
    public void setUp() {
        // Create and save a user with ID 11 for testing
        UserEntity userEntity = new UserEntity(11L, "DSA", "XYZ");
        userRepo.save(userEntity);
    }

    @Test
    @Rollback(value = false)
    public void saveUserTest() {
        UserEntity userEntity = new UserEntity(12L, "New User", "Password");
        UserEntity savedUser = userRepo.save(userEntity);

        // Check if the saved user's ID is not null and matches
        Assertions.assertThat(savedUser.getId()).isEqualTo(703L);
    }

    @Test
    public void getUserTest() {
        UserEntity userEntity = userRepo.findById(703L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Assertions.assertThat(userEntity.getId()).isEqualTo(703L);
    }

    @Test
    public void getAllUsersTest() {
        List<UserEntity> users = (List<UserEntity>) userRepo.findAll();
        Assertions.assertThat(users.size()).isGreaterThan(0);
    }

    @Test
    public void updateUserTest() {
        UserEntity userEntity = userRepo.findById(703L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userEntity.setPassword("400");
        UserEntity userUpdate = userRepo.save(userEntity);

        assertThat(userUpdate.getPassword()).isEqualTo("400");
    }

    @Test
    @Rollback(value = false)
    public void deleteUserTest() {
        UserEntity userEntity = userRepo.findById(703L)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepo.delete(userEntity);

        Optional<UserEntity> optionalUser = userRepo.findById(703L);
        Assertions.assertThat(optionalUser).isEmpty();
    }
}












