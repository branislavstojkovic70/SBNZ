package com.ftn.sbnz.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import com.ftn.sbnz.model.models.users.User;

public interface UserRepository extends JpaRepository<User, Integer>{
    public User findOneById(Integer id);
    User findByEmailAndPassword(String email, String password);
    Optional<User> findOneByEmail(String email);
    User findOneUserByEmail(String email);
    public List<User> findAll();
}
