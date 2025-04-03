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
        try {
            Optional<MUsers> user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                System.out.println("User found: " + user.get().getName()); // Debug line
                return user.get().getName();
            } else {
                System.out.println("User not found for email: " + email); // Debug line
                return "User";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "User";
        }
    }



}
