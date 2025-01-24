package com.example.banking.service;

import com.example.banking.entity.User;
import com.example.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankingService {

    @Autowired
    private UserRepository userRepository;

    public double getBalance(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.map(User::getBalance).orElse(0.0);
    }

    public void deposit(String username, double amount) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> {
            user.setBalance(user.getBalance() + amount);
            userRepository.save(user);
        });
    }

    public void withdraw(String username, double amount) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        userOptional.ifPresent(user -> {
            user.setBalance(user.getBalance() - amount);
            userRepository.save(user);
        });
    }

    public void transfer(String fromUsername, String toUsername, double amount) {
        Optional<User> fromUserOptional = userRepository.findByUsername(fromUsername);
        Optional<User> toUserOptional = userRepository.findByUsername(toUsername);

        if (fromUserOptional.isPresent() && toUserOptional.isPresent()) {
            User fromUser = fromUserOptional.get();
            User toUser = toUserOptional.get();

            fromUser.setBalance(fromUser.getBalance() - amount);
            toUser.setBalance(toUser.getBalance() + amount);
            userRepository.save(fromUser);
            userRepository.save(toUser);
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }
}
