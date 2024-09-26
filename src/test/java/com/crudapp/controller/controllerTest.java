package com.crudapp.controller;

import com.crudapp.entity.UserInfo;
import com.crudapp.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class controllerTest {

    @Mock
    UserInfoService userInfoService;

    @InjectMocks
    UserInfoController userInfoController;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    UserInfo userInfo;
    List<UserInfo> list;

    @BeforeEach
    public void setUp() throws Exception{
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userInfoController).build();
        userInfo = getUser();
        list = getUserList();
    }

    @Test
    public void saveUserTest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(userInfo);
        when(userInfoService.addUser(Mockito.any(UserInfo.class))).thenReturn("User Added Successfully");
        MvcResult mvcResult = mockMvc
                .perform(post("/auth/register")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated()).andReturn();

        String expectedOutput = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedOutput, "User Added Successfully");
    }

    @Test
    public void updateUserTest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(userInfo);
        Mockito.doNothing().when(userInfoService).updateUser(Mockito.anyInt(), Mockito.any(UserInfo.class));

        MvcResult mvcResult = mockMvc
                .perform(put("/auth/users/" + userInfo.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String expectedOutput = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedOutput, "User updated successfully");
    }

    @Test
    public void deleteUserTest() throws Exception {
        Mockito.doNothing().when(userInfoService).deleteUser(Mockito.anyInt());

        MvcResult mvcResult = mockMvc
                .perform(delete("/auth/users/{id}", userInfo.getId()))
                .andExpect(status().isOk()).andReturn();

        String expectedOutput = mvcResult.getResponse().getContentAsString();
        assertEquals(expectedOutput, "User deleted successfully");
    }

    @Test
    public void getUserByIdTest() throws Exception {
        when(userInfoService.getUserById(Mockito.anyInt())).thenReturn(userInfo);

        MvcResult mvcResult = mockMvc
                .perform(get("/auth/users/{id}", userInfo.getId()))
                .andExpect(status().isOk()).andReturn();

        String expectedOutput = mvcResult.getResponse().getContentAsString();
        UserInfo expectedOutputUser = objectMapper.readValue(expectedOutput, UserInfo.class);
        assertEquals(expectedOutputUser.getEmail(), userInfo.getEmail());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        when(userInfoService.getAllUsersInfo()).thenReturn(list);

        MvcResult mvcResult = mockMvc
                .perform(get("/auth/users"))
                .andExpect(status().isOk()).andReturn();

        String expectedOutput = mvcResult.getResponse().getContentAsString();
        List<UserInfo> expectedUserList = objectMapper.readValue(expectedOutput, List.class);
        assertEquals(expectedUserList.size(), list.size());
    }

    public UserInfo getUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(1);
        userInfo.setName("Test User");
        userInfo.setEmail("test@example.com");
        userInfo.setPassword("password");
        userInfo.setRoles("USER");
        return userInfo;
    }

    public List<UserInfo> getUserList() {
        List<UserInfo> userList = new ArrayList<>();
        userList.add(getUser());
        return userList;
    }
}









//package com.crudapp.controller;
//
//import com.crudapp.entity.UserEntity;
//import com.crudapp.service.UserService;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MockMvcBuilder;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//public class controllerTest {
//
//    @Mock
//    UserService userService;
//
//    @InjectMocks
//    UserController userController;
//
//    private MockMvc mockMvc;
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    UserEntity userEntity;
//    List<UserEntity> list;
//
//    @BeforeEach
//    public void setUp() throws Exception{
//        MockitoAnnotations.initMocks(this);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//        userEntity = getUser();
//        list = getUserList();
//    }
//
//
//    @Test
//    public void saveUserTest() throws Exception {
//        String jsonRequest = objectMapper.writeValueAsString(userEntity);
//        when(userService.saveUser(Mockito.any(UserEntity.class))).thenReturn(userEntity);
//        MvcResult mvcResult = mockMvc
//                .perform(post("/save-user").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk()).andReturn();
//        String expectedOutput = mvcResult.getResponse().getContentAsString();
//        UserEntity expectedOutputUser = objectMapper.readValue(expectedOutput, UserEntity.class);
//        assertEquals(expectedOutputUser.getPassword(), userEntity.getPassword());
//    }
//
//
//
//    @Test
//    public void updateUserTest() throws Exception {
//
//        String jsonRequest = objectMapper.writeValueAsString(userEntity);
//
//        when(userService.update(Mockito.anyLong(), Mockito.any(UserEntity.class))).thenReturn(userEntity);
//        MvcResult mvcResult = mockMvc
//                .perform(put("/updateUser/" + userEntity.getId()).content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        String expectedOutput = mvcResult.getResponse().getContentAsString();
//        UserEntity expectedOutputUser = objectMapper.readValue(expectedOutput, UserEntity.class);
//
//        assertEquals(expectedOutputUser.getPassword(), userEntity.getPassword());
//    }
//
//
//
//
//    @Test
//    public void deleteUserTest() throws Exception {
//        String jsonRequest = String.valueOf(userEntity.getId());
//        when(userService.getUserById(Mockito.anyLong())).thenReturn(userEntity);
//        MvcResult mvcResult = mockMvc
//                .perform(delete("/deleteUser/{userid}",jsonRequest))
//                .andExpect(status().isOk()).andReturn();
//        String expectedOutput = mvcResult.getResponse().getContentAsString();
//        long id=Long.parseLong(expectedOutput);
//        assertEquals(id, userEntity.getId());
//    }
//
//
//    @Test
//    public void getUserByIdTest() throws Exception {
//        String jsonRequest = String.valueOf(userEntity.getId());
//        when(userService.getUserById(Mockito.anyLong())).thenReturn(userEntity);
//        MvcResult mvcResult = mockMvc
//                .perform(get("/getUserById/{userid}",jsonRequest))
//                .andExpect(status().isOk()).andReturn();
//        String expectedOutput = mvcResult.getResponse().getContentAsString();
//        UserEntity expectedOutputUser = objectMapper.readValue(expectedOutput, UserEntity.class);
//        assertEquals(expectedOutputUser.getPassword(), userEntity.getPassword());
//    }
//
//
//    @Test
//    public void getAllUsersTest() throws Exception {
//        when(userService.getAllUsers()).thenReturn(list);
//        MvcResult mvcResult = mockMvc
//                .perform(get("/getAllUsers"))
//                .andExpect(status().isOk()).andReturn();
//        long status=mvcResult.getResponse().getStatus();
//        assertEquals(200, status);
//        String expectedOutput = mvcResult.getResponse().getContentAsString();
//        JSONAssert.assertEquals(expectedOutput,"\r\n"
//                + "[{\"id\":11,\"username\":\"cccc\",\"password\":\"ddd\"},{\"id\":15,\"username\":\"ccdfdvfdv\",\"password\":\"ggg\"}]" ,true);
//
//    }
//
//
//    public UserEntity getUser()
//    {
//        UserEntity userEntity=new UserEntity();
//        userEntity.setName("hhh");
//        userEntity.setPassword("lbnjsd");
//        return userEntity;
//    }
//
//
//    public List<UserEntity> getUserList()
//    {
//        List<UserEntity> bookList = new ArrayList<>();
//
//        UserEntity userEntity1=new UserEntity();
//        userEntity1.setName("hhojbd");
//        userEntity1.setPassword("lbvcdnjsd");
//
//        UserEntity userEntity2=new UserEntity();
//        userEntity2.setName("hhojbd");
//        userEntity2.setPassword("lbvcdnjsd");
//
//        bookList.add(userEntity1);
//        bookList.add(userEntity2);
//        return bookList;
//
//    }
//
//}
