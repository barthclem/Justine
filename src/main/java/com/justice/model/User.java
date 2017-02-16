package com.justice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by aanu.oyeyemi on 1/4/17.
 * Project name -> demojustice
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;

    private String fname;
    private long age;
    private double salary;

    private User(){}

    public User(String name, long age, double salary) {
        this.fname = name;
        this.age = age;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return fname;
    }

    public void setName(String name) {
        this.fname = name;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + fname + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}
