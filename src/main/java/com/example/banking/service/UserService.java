package com.example.banking.service;

import com.example.banking.entity.User;
import com.example.banking.exceptions.*;
import com.example.banking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String generateIban() {
        String prefix = "BC";
        Random random = new Random();
        StringBuilder ibanBuilder = new StringBuilder(prefix);
        for (int i = 0; i < 16; i++) {
            ibanBuilder.append(random.nextInt(10));
        }
        return ibanBuilder.toString();
    }

    public void registerUser(String username, String password, String firstName, String lastName) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with username " + username + " already exists.");
        }

        String iban = generateIban();

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setBalance(0.0);
        newUser.setIban(iban);

        userRepository.save(newUser);
    }



    @Transactional
    public void deposit(String username, double amount) {
        if (amount <=0) {
            throw new InvalidAmountException("Deposit amount must be greater than 0.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setBalance(user.getBalance() + amount);
        userRepository.save(user);
    }

    @Transactional
    public void withdraw(String username, double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than 0.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (user.getBalance() < amount) {
            throw new InsufficientBalanceException("You cannot withdraw more than your available balance.");
        }

        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);
    }

    @Transactional
    public void transfer(String fromUsername, String toUsername, double amount, String toIban) {
        if (amount <= 0) {
            throw new InvalidAmountException("Transfer amount must be greater than 0.");
        }

        User sender = userRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new UserNotFoundException("Sender not found."));
        User receiver = userRepository.findByUsername(toUsername)
                .orElseThrow(() -> new UserNotFoundException("Receiver not found."));

        if (!receiver.getIban().equals(toIban)) {
            throw new InvalidIbanException("The provided IBAN does not match the recipient.");
        }

        if (sender.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance for transfer.");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
