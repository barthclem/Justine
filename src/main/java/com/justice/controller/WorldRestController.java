package com.justice.controller;

import com.justice.model.User;
import com.justice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Created by aanu.oyeyemi on 1/4/17.
 * Project name -> demojustice
 */
@RestController
public class WorldRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public HttpEntity<List<User>> getAllLists(){
        List<User> users=userService.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public HttpEntity<User> getUser(@PathVariable long id){
        User user=userService.findById(id);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/user/")
    public HttpEntity<User> createUser(@RequestBody User user, UriComponentsBuilder builder){
        if(userService.isExists(user.getId())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        userService.save(user);

        HttpHeaders headers=new HttpHeaders();
        headers.setLocation(builder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<User>(user,headers,HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public HttpEntity<User> updateUser(@RequestBody User user,@PathVariable long id){

        if(!userService.isExists(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User sUser=userService.findById(id);
        sUser.setName(user.getName());
        sUser.setAge(user.getAge());
        sUser.setSalary(user.getSalary());

        userService.save(sUser);
        return new ResponseEntity<>(sUser,HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public HttpEntity<User> deleteUser(@PathVariable long id){
        User user=userService.findById(id);
        if(user==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/user/")
    public HttpEntity<User> deleteAllUser(@PathVariable long id){
       userService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
