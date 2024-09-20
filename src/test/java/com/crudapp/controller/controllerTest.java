package com.crudapp.controller;

import com.crudapp.entity.UserEntity;
import com.crudapp.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
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
    UserService userService;

    @InjectMocks
    UserController userController;

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    UserEntity userEntity;
    List<UserEntity> list;

    @BeforeEach
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userEntity = getUser();
        list = getUserList();
    }


    @Test
    public void saveUserTest() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(userEntity);
        when(userService.saveUser(Mockito.any(UserEntity.class))).thenReturn(userEntity);
        MvcResult mvcResult = mockMvc
                .perform(post("/save-user").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk()).andReturn();
        String expectedOutput = mvcResult.getResponse().getContentAsString();
        UserEntity expectedOutputUser = objectMapper.readValue(expectedOutput, UserEntity.class);
        assertEquals(expectedOutputUser.getPassword(), userEntity.getPassword());
    }



    @Test
    public void updateUserTest() throws Exception {

        String jsonRequest = objectMapper.writeValueAsString(userEntity);

        when(userService.update(Mockito.anyLong(), Mockito.any(UserEntity.class))).thenReturn(userEntity);
        MvcResult mvcResult = mockMvc
                .perform(put("/updateUser/" + userEntity.getId()).content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String expectedOutput = mvcResult.getResponse().getContentAsString();
        UserEntity expectedOutputUser = objectMapper.readValue(expectedOutput, UserEntity.class);

        assertEquals(expectedOutputUser.getPassword(), userEntity.getPassword());
    }


//    @Test
//    public void updateUserTest() throws Exception {
//        String jsonRequest = objectMapper.writeValueAsString(userEntity);
//        when(userService.update(Mockito.anyLong(),Mockito.any(UserEntity.class).getId())).thenReturn(userEntity);
//        MvcResult mvcResult = mockMvc
//                .perform(put("/updateUser").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk()).andReturn();
//        String expectedOutput = mvcResult.getResponse().getContentAsString();
//        UserEntity expectedOutputUser = objectMapper.readValue(expectedOutput, UserEntity.class);
//        assertEquals(expectedOutputUser.getPassword(), userEntity.getPassword());
//    }



    @Test
    public void deleteUserTest() throws Exception {
        String jsonRequest = String.valueOf(userEntity.getId());
        when(userService.getUserById(Mockito.anyLong())).thenReturn(userEntity);
        MvcResult mvcResult = mockMvc
                .perform(delete("/deleteUser/{userid}",jsonRequest))
                .andExpect(status().isOk()).andReturn();
        String expectedOutput = mvcResult.getResponse().getContentAsString();
        long id=Long.parseLong(expectedOutput);
        assertEquals(id, userEntity.getId());
    }


    @Test
    public void getUserByIdTest() throws Exception {
        String jsonRequest = String.valueOf(userEntity.getId());
        when(userService.getUserById(Mockito.anyLong())).thenReturn(userEntity);
        MvcResult mvcResult = mockMvc
                .perform(get("/getUserById/{userid}",jsonRequest))
                .andExpect(status().isOk()).andReturn();
        String expectedOutput = mvcResult.getResponse().getContentAsString();
        UserEntity expectedOutputUser = objectMapper.readValue(expectedOutput, UserEntity.class);
        assertEquals(expectedOutputUser.getPassword(), userEntity.getPassword());
    }


    @Test
    public void getAllUsersTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(list);
        MvcResult mvcResult = mockMvc
                .perform(get("/getAllUsers"))
                .andExpect(status().isOk()).andReturn();
        long status=mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String expectedOutput = mvcResult.getResponse().getContentAsString();
        JSONAssert.assertEquals(expectedOutput,"\r\n"
                + "[{\"id\":11,\"username\":\"cccc\",\"password\":\"ddd\"},{\"id\":15,\"username\":\"ccdfdvfdv\",\"password\":\"ggg\"}]" ,true);

    }


    public UserEntity getUser()
    {
        UserEntity userEntity=new UserEntity();
        userEntity.setName("hhh");
        userEntity.setPassword("lbnjsd");
        return userEntity;
    }


    public List<UserEntity> getUserList()
    {
        List<UserEntity> bookList = new ArrayList<>();

        UserEntity userEntity1=new UserEntity();
        userEntity1.setName("hhojbd");
        userEntity1.setPassword("lbvcdnjsd");

        UserEntity userEntity2=new UserEntity();
        userEntity2.setName("hhojbd");
        userEntity2.setPassword("lbvcdnjsd");

        bookList.add(userEntity1);
        bookList.add(userEntity2);
        return bookList;

    }

}
