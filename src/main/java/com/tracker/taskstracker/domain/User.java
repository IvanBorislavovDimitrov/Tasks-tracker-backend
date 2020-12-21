package com.tracker.taskstracker.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User extends IdEntity implements UserDetails {

    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @ManyToMany(mappedBy = "users", targetEntity = Role.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Role> roles = new ArrayList<>();
    @ManyToMany(mappedBy = "users", targetEntity = Project.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Project> projects = new ArrayList<>();
    @OneToMany(mappedBy = "assignee", targetEntity = Task.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
    @OneToMany(mappedBy = "author", targetEntity = Comment.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    @Column(name = "account_verification_code")
    private String accountVerificationCode;
    @Column(name = "is_active")
    private boolean isEnabled;
    @Column(name = "profile_picture_name")
    private String profilePictureName;
    @ElementCollection
    @CollectionTable(name = "login_records", joinColumns = @JoinColumn(name = "id"))
    private List<LoginRecord> loginRecords = new LinkedList<>();
    @Column(name = "forgotten_password_token")
    private String forgottenPasswordToken;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getParsedRole()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getAccountVerificationCode() {
        return accountVerificationCode;
    }

    public void setAccountVerificationCode(String accountVerificationCode) {
        this.accountVerificationCode = accountVerificationCode;
    }

    public String getProfilePictureName() {
        return profilePictureName;
    }

    public void setProfilePictureName(String profilePictureName) {
        this.profilePictureName = profilePictureName;
    }

    public List<LoginRecord> getLoginRecords() {
        return loginRecords;
    }

    public void setLoginRecords(List<LoginRecord> loginRecords) {
        this.loginRecords = loginRecords;
    }

    public String getForgottenPasswordToken() {
        return forgottenPasswordToken;
    }

    public void setForgottenPasswordToken(String forgottenPasswordToken) {
        this.forgottenPasswordToken = forgottenPasswordToken;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public void addProject(Project project) {
        projects.add(project);
    }

    public void addLoginRecord(LoginRecord loginRecord) {
        loginRecords.add(loginRecord);
    }

    public LoginRecord removeOldestLoginRecord() {
        if (loginRecords.size() == 0) {
            throw new IllegalStateException("There are no login records!");
        }
        return loginRecords.remove(0);
    }

    @Embeddable
    public static class LoginRecord {

        @Column(name = "created_at", nullable = false)
        private Date createdAt;

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
    }
}
