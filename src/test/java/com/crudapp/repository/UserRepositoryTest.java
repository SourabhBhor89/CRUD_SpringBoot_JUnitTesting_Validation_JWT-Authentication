package com.crudapp.repository;

import com.crudapp.entity.UserInfo;
import com.crudapp.repo.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
//@Sql(scripts = "/test-data.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    private UserInfo userInfo;

    @BeforeEach
    public void setUp() {
        userInfo = new UserInfo();
        userInfo.setName("TRH!!@@##");
        userInfo.setEmail("trh@indore.com");
        userInfo.setPassword("password");
        userInfo.setRoles("USER");
    }

    @Test
    public void testSaveUser() {
        UserInfo savedUser = userInfoRepository.save(userInfo);
        assertNotNull(savedUser.getId());
        assertEquals(userInfo.getEmail(), savedUser.getEmail());
    }

    @Test
    public void testFindUserById() {
        UserInfo savedUser = userInfoRepository.save(userInfo);
        Optional<UserInfo> foundUser = userInfoRepository.findById(savedUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    public void testFindAllUsers() {
        userInfoRepository.save(userInfo);
        List<UserInfo> users = userInfoRepository.findAll();
        assertFalse(users.isEmpty());
        assertTrue(users.size() > 0);
    }

    @Test
    public void testDeleteUserById() {
        UserInfo savedUser = userInfoRepository.save(userInfo);
        userInfoRepository.deleteById(savedUser.getId());
        Optional<UserInfo> deletedUser = userInfoRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    public void testFindUserByEmail() {
        userInfoRepository.save(userInfo);
        Optional<UserInfo> foundUser = userInfoRepository.findByEmail(userInfo.getEmail());
        assertTrue(foundUser.isPresent());
        assertEquals(userInfo.getEmail(), foundUser.get().getEmail());
    }
}








//package com.crudapp.repository;
//
//import com.crudapp.entity.UserEntity;
//import com.crudapp.repo.UserRepo;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.annotation.Rollback;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest
//public class UserRepositoryTest {
//
//    @Autowired
//    UserRepo userRepo;
//
//    @BeforeEach
//    @Rollback(value = false) // Set to false if you want the user to persist for other tests
//    public void setUp() {
//        // Create and save a user with ID 11 for testing
//        UserEntity userEntity = new UserEntity(112L, "DSA", "XYZ");
//        userRepo.save(userEntity);
//    }
//
//    @Test
//    @Rollback(value = false)
//    public void saveUserTest() {
//        UserEntity userEntity = new UserEntity(112L, "New User", "Password");
//        UserEntity savedUser = userRepo.save(userEntity);
//
//        // Check if the saved user's ID is not null and matches
//        Assertions.assertThat(savedUser.getId()).isEqualTo(112L);
//    }
//
//    @Test
//    public void getUserTest() {
//        UserEntity userEntity = userRepo.findById(112L)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Assertions.assertThat(userEntity.getId()).isEqualTo(112L);
//    }
//
//    @Test
//    public void getAllUsersTest() {
//        List<UserEntity> users = (List<UserEntity>) userRepo.findAll();
//        Assertions.assertThat(users.size()).isGreaterThan(0);
//    }
//
//    @Test
//    public void updateUserTest() {
//        UserEntity userEntity = userRepo.findById(112L)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        userEntity.setPassword("400");
//        UserEntity userUpdate = userRepo.save(userEntity);
//
//        assertThat(userUpdate.getPassword()).isEqualTo("400");
//    }
//
//    @Test
//    @Rollback(value = false)
//    public void deleteUserTest() {
//        UserEntity userEntity = userRepo.findById(112L)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        userRepo.delete(userEntity);
//
//        Optional<UserEntity> optionalUser = userRepo.findById(112L);
//        Assertions.assertThat(optionalUser).isEmpty();
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
