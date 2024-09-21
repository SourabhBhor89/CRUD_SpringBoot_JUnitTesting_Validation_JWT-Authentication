package com.crudapp.service;
import com.crudapp.entity.UserEntity;
import com.crudapp.entity.UserInfo;
import com.crudapp.repo.UserInfoRepository;
import com.crudapp.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class UserInfoService implements UserDetailsService {
    private final UserInfoRepository repository;




    private final PasswordEncoder encoder;
    public UserInfoService(UserInfoRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByName(username);
        // Converting userDetail to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }



    public void updateUser(Integer id, UserInfo userInfo) {
        UserInfo existingUser = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));

        // Update fields as needed
        existingUser.setName(userInfo.getName());
        existingUser.setPassword(encoder.encode(userInfo.getPassword())); // If updating password
        // Add other fields as needed...

        repository.save(existingUser);
    }

    public void deleteUser(Integer id) {
        if (!repository.existsById(id)) {
            throw new UsernameNotFoundException("User not found with ID: " + id);
        }
        repository.deleteById(id);
    }



    public UserInfo getUserById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + id));
    }


    public List<UserInfo> getAllUsersInfo() {
        return repository.findAll(); // Assuming findAll() is defined in UserInfoRepository
    }


    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "User Added Successfully";
    }



}