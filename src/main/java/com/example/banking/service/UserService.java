package com.example.banking.service;

import com.example.banking.entity.User;
import com.example.banking.exceptions.*;
import com.example.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User registerUser(String username, String rawPassword, String firstName, String lastName) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        String iban = generateIban();

        User user = new User(username, encodedPassword, firstName, lastName, iban, 0.0);
        return userRepository.save(user);
    }

    public void transfer(String fromUsername, String toUsername, String toIban, double amount) {
        validateAmount(amount);

        User fromUser = userRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new UserNotFoundException("Sender not found."));
        User toUser = userRepository.findByUsername(toUsername)
                .orElseThrow(() -> new UserNotFoundException("Recipient not found."));

        if (!toUser.getIban().equals(toIban)) {
            throw new InvalidIbanException("Recipient IBAN does not match.");
        }

        if (fromUser.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }

        fromUser.setBalance(fromUser.getBalance() - amount);
        toUser.setBalance(toUser.getBalance() + amount);

        userRepository.save(fromUser);
        userRepository.save(toUser);
    }

    public void deposit(String username, double amount) {
        validateAmount(amount);
        if (amount <= 0) {
            throw new InvalidAmountException("You have to deposit an amount greater than 0.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    public void withdraw(String username, double amount) {
        validateAmount(amount);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance.");
        }

        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }
    }

    private String generateIban() {
        return "BC" + (int) (Math.random() * 1_000_000_000);
    }
}
