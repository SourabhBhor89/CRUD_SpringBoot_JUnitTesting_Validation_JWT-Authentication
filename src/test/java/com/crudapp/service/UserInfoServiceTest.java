package com.crudapp.service;

import com.crudapp.entity.UserInfo;
import com.crudapp.repo.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class UserInfoServiceTest {

    @InjectMocks
    private UserInfoService userInfoService;

    @Mock
    private UserInfoRepository repository;

    @Mock
    private PasswordEncoder encoder;

    private UserInfo testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserInfo();
        testUser.setId(1);
        testUser.setName("testUser");
        testUser.setPassword("testPassword");
        testUser.setRoles("USER");
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        when(repository.findByName(testUser.getName())).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userInfoService.loadUserByUsername(testUser.getName());

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(testUser.getName());
        verify(repository).findByName(testUser.getName());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        when(repository.findByName(testUser.getName())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userInfoService.loadUserByUsername(testUser.getName()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found " + testUser.getName());
    }

    @Test
    public void testUpdateUser_UserExists() {
        when(repository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        UserInfo updatedUser = new UserInfo();
        updatedUser.setName("updatedUser");
        updatedUser.setPassword("newPassword");
        updatedUser.setRoles("ADMIN");

        when(encoder.encode(updatedUser.getPassword())).thenReturn("encodedPassword");

        userInfoService.updateUser(testUser.getId(), updatedUser);

        ArgumentCaptor<UserInfo> userInfoCaptor = ArgumentCaptor.forClass(UserInfo.class);
        verify(repository).save(userInfoCaptor.capture());

        UserInfo savedUser = userInfoCaptor.getValue();
        assertThat(savedUser.getName()).isEqualTo("updatedUser");
        assertThat(savedUser.getPassword()).isEqualTo("encodedPassword");
        assertThat(savedUser.getRoles()).isEqualTo("ADMIN");
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        when(repository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userInfoService.updateUser(testUser.getId(), testUser))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with ID: " + testUser.getId());
    }

    @Test
    public void testDeleteUser_UserExists() {
        when(repository.existsById(testUser.getId())).thenReturn(true);

        userInfoService.deleteUser(testUser.getId());

        verify(repository).deleteById(testUser.getId());
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(repository.existsById(testUser.getId())).thenReturn(false);

        assertThatThrownBy(() -> userInfoService.deleteUser(testUser.getId()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with ID: " + testUser.getId());
    }

    @Test
    public void testGetUserById_UserExists() {
        when(repository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        UserInfo userInfo = userInfoService.getUserById(testUser.getId());

        assertThat(userInfo).isNotNull();
        assertThat(userInfo.getId()).isEqualTo(testUser.getId());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        when(repository.findById(testUser.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userInfoService.getUserById(testUser.getId()))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Invalid ID: User not found");
    }

    @Test
    public void testGetAllUsersInfo() {
        when(repository.findAll()).thenReturn(List.of(testUser));

        List<UserInfo> users = userInfoService.getAllUsersInfo();

        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getName()).isEqualTo(testUser.getName());
    }

    @Test
    public void testAddUser() {
        when(encoder.encode(testUser.getPassword())).thenReturn("encodedPassword");

        String response = userInfoService.addUser(testUser);

        verify(repository).save(testUser);
        assertThat(response).isEqualTo("User Added Successfully");
    }
}
