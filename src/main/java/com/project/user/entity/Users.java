package com.project.user.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name ="users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="user_id")
    Long userId;
    @Column(name ="username")
    String username;
    @Column(name = "email")
    String email;
    @Column(name = "password")
    String password;
    @Column(name = "full_name")
    String fullName;
    @Column(name= "balance")
    Double balance;
    @Column(name = "role_id")
    int roleId;
    @Column(name = "created_at", insertable = false, updatable = false)
    Timestamp createdAt;
    @Column(name = "updated_at", insertable = false, updatable = false)
    Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

}
