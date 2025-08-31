package org.defesasoft.autservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
public class User {
    @Id
    private Long id;
    private String username;
    private String pasword;
    private String role;

    public Long getId() {
        return id;
    }

    public User(Long id, String username, String pasword, String role) {
        this.id = id;
        this.username = username;
        this.pasword = pasword;
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
