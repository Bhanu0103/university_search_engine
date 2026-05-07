package com.university.common.entity;

import com.university.common.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "common_users")
public class UserEntity extends BaseEntity {
    private String username;
    private String password;
    private String email;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
