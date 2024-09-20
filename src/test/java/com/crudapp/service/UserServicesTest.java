package com.crudapp.service;


import com.crudapp.entity.UserEntity;
import com.crudapp.repo.UserRepo;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServicesTest {

    UserEntity userEntity1;
    UserEntity userEntity2;
    List<UserEntity> Userlist;

    @Mock
    UserRepo userRepo;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setup()
    {
        Userlist=getUserList();
        userEntity1=getUser();
        userEntity2=getUser();
    }

    @Test
    public void saveTest()
    {
        when(userRepo.save(Mockito.any(UserEntity.class))).thenReturn(userEntity1);
        UserEntity actualoutput = userService.saveUser(userEntity1);
        assertEquals(userEntity1.toString(), actualoutput.toString());

    }

    @Test
    public void getAllUsersTest()
    {
        when(userRepo.findAll()).thenReturn(Userlist);
        List<UserEntity> actualUserList = userService.getAllUsers();
        assertEquals(actualUserList.size(), Userlist.size());


    }


    @Test
    public void deleteTest(){

        when(userRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userEntity1));
        int id = Math.toIntExact(userService.delete(userEntity1.getId()));
        assertEquals(userEntity1.getId(), id);


    }

    @Test
    public void getByIdTest()
    {
        when(userRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userEntity1));
        UserEntity actualOutput =userService.getUserById(userEntity1.getId());
        assertEquals(actualOutput.toString(), userEntity1.toString());

    }

    @Test
    public void updateTest()
    {
        when(userRepo.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userEntity2));
        when(userRepo.save(Mockito.any(UserEntity.class))).thenReturn(userEntity1);
        UserEntity actualOutput = userService.update(userEntity1.getId(), userEntity1);
        assertEquals(userEntity1.toString(), actualOutput.toString());

    }


    private List<UserEntity> getUserList() {
        List<UserEntity> userEntityList = new ArrayList<>();

        UserEntity userEntity1=new UserEntity();

        userEntity1.setName("cccc");
        userEntity1.setPassword("ddd");

        UserEntity userEntity2=new UserEntity();
        userEntity1.setName("fff");
        userEntity1.setPassword("eee");

        UserEntity userEntity3=new UserEntity();
        userEntity1.setName("rrr");
        userEntity1.setPassword("ttt");



        Userlist.add(userEntity1);
        Userlist.add(userEntity2);
        Userlist.add(userEntity3);
        return Userlist;
    }

    private UserEntity getUser() {
        UserEntity user=new UserEntity();
        user.setName("aaabbb");
        user.setPassword("bbb");

        return user;
    }

}
