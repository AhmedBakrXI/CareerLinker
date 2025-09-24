package com.a7.job.auth.user.service.impl;

import com.a7.job.auth.user.UserRepository;
import com.a7.job.auth.user.model.UserModel;
import com.a7.job.auth.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = "users")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Cacheable(key = "#id")
    public Optional<UserModel> findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    @Cacheable(key = "#email")
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#user.id"),
            @CacheEvict(key = "#user.email")
    })
    public UserModel saveUser(UserModel user) {
        return userRepository.save(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "#user.id"),
            @CacheEvict(key = "#user.email")
    })
    public void deleteUser(UserModel user) {
        userRepository.delete(user);
    }

    @Override
    @Cacheable(key = "#email")
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
