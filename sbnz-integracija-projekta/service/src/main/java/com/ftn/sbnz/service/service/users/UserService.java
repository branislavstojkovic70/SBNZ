package com.ftn.sbnz.service.service.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.repository.users.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User authenticateUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, passwordEncoder.encode(password));
    }

    public User findUserByEmail(String email){
        return userRepository.findOneUserByEmail(email);
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
