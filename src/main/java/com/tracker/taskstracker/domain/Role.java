package com.tracker.taskstracker.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends IdEntity {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users = new ArrayList<>();

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType role) {
        this.roleType = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        users.add(user);
    }

    @Override
    public String toString() {
        return roleType.toString();
    }

    public enum RoleType {

        ADMIN, USER

    }
}
