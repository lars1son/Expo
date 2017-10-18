package com.service;


import com.entities.UserEntity;

public interface UserService {


    void save(UserEntity user);

    UserEntity findByEmail(String email);

}
