package com.crudapp.controller;


import com.crudapp.entity.UserEntity;
import com.crudapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/save-user")
    public UserEntity saveUser(@RequestBody UserEntity userEntity)
    {
        return userService.saveUser(userEntity);
    }

    @DeleteMapping("/deleteUser/{userid}")
    public long deleteUser(@PathVariable("userid") long userid)
    {
        return userService.delete(userid);

    }



    @GetMapping("/getUserById/{userid}")
    public UserEntity getUser(@PathVariable("userid") long userid)
    {
        return userService.getUserById(userid);
    }



    @PatchMapping("/updateUser/{userid}")
    public UserEntity update(@RequestBody UserEntity userEntity,@PathVariable long userid)
    {
        userService.update(userid,userEntity);
        return userEntity;
    }

//    @GetMapping("/getUserById/{userid}")
//    public UserEntity getuser(@PathVariable("userid") long userid)
//    {
//        return userService.getUserById(userid);
//    }


    @GetMapping("/getAllUsers")
    public List<UserEntity> getAllUsers()
    {
        return userService.getAllUsers();
    }


}
