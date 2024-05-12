package com.ftn.sbnz.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.sbnz.model.models.users.Role;
import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(Integer id) {
        return userRepository.findOneById(id);
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findOneUserByEmail(email);
    }

    @Transactional(readOnly = true)
    public User login(String email, String password) {
        return userRepository.findOneByEmailAndPassword(email, password);
    }

    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // @Transactional(readOnly = true)
    // public Optional<User> findUserByEmail(String email) {
    //     return userRepository.findOneByEmail(email);
    // }

    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    public User findUserByEmail(String email){
        User user = new User();
        user.setEmail(email);
        user.setPassword("Password123");
        user.setDateOfBirth(LocalDateTime.now());
        user.setIme("FirstName");
        user.setPrezime("LastName");
        user.setRole(Role.Admin);
        userRepository.save(user);
        return user;
    }
}
