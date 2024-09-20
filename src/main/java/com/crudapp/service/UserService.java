package com.crudapp.service;

import com.crudapp.entity.UserEntity;
import com.crudapp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {


    @Autowired
    private UserRepo userRepo;

    public UserEntity saveUser(UserEntity userEntity)
    {
        return userRepo.save(userEntity);
}


    public List<UserEntity> getAllUsers()
    {
        List<UserEntity> users = new ArrayList<UserEntity>();
        userRepo.findAll().forEach(users1 -> users.add(users1));
        return users;
    }


    public long delete(long id)
    {
         userRepo.deleteById(id);
        return id;
    }


    public UserEntity getUserById(long id){
        return userRepo.getById(id);
    }

    public UserEntity update(Long userId, UserEntity updatedUserEntity) {
        UserEntity user = userRepo.findById(userId).orElse(null);
        System.out.println(user);
        if(user != null ){
           user.setName(updatedUserEntity.getName());
           user.setPassword(updatedUserEntity.getPassword());
           return userRepo.save(user);
        }
        return null;
    }

}
