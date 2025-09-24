package com.a7.job.auth.user.service;

import com.a7.job.auth.user.model.UserModel;

import java.util.Optional;

public interface UserService {
    Optional<UserModel> findById(long id);
    Optional<UserModel> findByEmail(String email);
    UserModel saveUser(UserModel user);
    void deleteUser(UserModel user);
    boolean existsByEmail(String email);
}
