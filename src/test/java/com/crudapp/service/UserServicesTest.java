package com.crudapp.service;

import com.crudapp.entity.UserInfo;
import com.crudapp.repo.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // This initializes the mocks
public class UserServicesTest {

    UserInfo userInfo;
    List<UserInfo> userList;

    @Mock
    UserInfoRepository userInfoRepository;  // Mock the repository

    @Mock
    PasswordEncoder passwordEncoder; // Mock the PasswordEncoder

    @InjectMocks
    UserInfoService userInfoService;  // Inject mocks into the service

    @BeforeEach
    void setup() {
        userList = getUserList();
        userInfo = getUser();
    }

    @Test
    public void saveTest() {
        when(passwordEncoder.encode(Mockito.any(CharSequence.class))).thenReturn("encodedPassword"); // Mock encoding
        when(userInfoRepository.save(Mockito.any(UserInfo.class))).thenReturn(userInfo);
        String actualOutput = userInfoService.addUser(userInfo);
        assertEquals("User Added Successfully", actualOutput);
    }

    @Test
    public void getAllUsersTest() {
        when(userInfoRepository.findAll()).thenReturn(userList);
        List<UserInfo> actualUserList = userInfoService.getAllUsersInfo();
        assertEquals(actualUserList.size(), userList.size());
    }

    @Test
    public void deleteTest() {
        when(userInfoRepository.existsById(Mockito.anyInt())).thenReturn(true);
        Mockito.doNothing().when(userInfoRepository).deleteById(Mockito.anyInt());
        userInfoService.deleteUser(userInfo.getId());
    }

    @Test
    public void getByIdTest() {
        when(userInfoRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(userInfo));
        UserInfo actualOutput = userInfoService.getUserById(userInfo.getId());
        assertEquals(actualOutput.toString(), userInfo.toString());
    }

    // Helper methods to create mock data
    private List<UserInfo> getUserList() {
        List<UserInfo> userEntityList = new ArrayList<>();
        userEntityList.add(getUser());
        return userEntityList;
    }

    private UserInfo getUser() {
        UserInfo user = new UserInfo();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRoles("USER");
        return user;
    }
}
