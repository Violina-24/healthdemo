package com.health.healthdemo.services;

import com.health.healthdemo.entity.MUsers;
import com.health.healthdemo.repository.MUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsersService {
    @Autowired
    private MUsersRepository userRepository;

    public MUsers saveUser(MUsers users) {
        return userRepository.save(users);
    }

    public MUsers findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public String getUsernameByEmail(String email) {
        Optional<MUsers> user = userRepository.findByEmail(email);
        return user.map(MUsers::getName).orElse("Unknown User");
    }
}
