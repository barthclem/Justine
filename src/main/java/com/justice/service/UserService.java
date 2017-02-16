package com.justice.service;

import com.justice.model.User;
import com.justice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aanu.oyeyemi on 1/4/17.
 * Project name -> demojustice
 */
@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        List<User> list=new ArrayList<>();
        userRepository.findAll().forEach(each->list.add(each));
        return list;
    }

    public User findById(long id) {
        return userRepository.findOne(id);
    }

    public boolean isExists(long id) {
        return userRepository.exists(id);
    }

    public void delete(long id) {
        userRepository.delete(id);
    }

    public void deleteAll() {
        userRepository.deleteAll();
    }

    public void save(User user) {userRepository.save(user);}
}
