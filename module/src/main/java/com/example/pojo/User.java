package com.example.pojo;

public class User {
    public Integer id;
    public Integer age;
    public String name;
    public Integer isDeleted;

    public User(Integer id, Integer age, String name, Integer isDeleted) {
        this.id = id;
        this.age = age;
        this.name = name;
        this.isDeleted = isDeleted;
    }

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
