package com.tracker.taskstracker.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "projects")
public class Project extends IdEntity {

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, length = 10000)
    private String description;
    @Column
    private String pictureName;
    @ManyToMany
    @JoinTable(name = "users_projects", joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> users = new ArrayList<>();
    @OneToMany(mappedBy = "project", targetEntity = Task.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();
    @OneToMany(mappedBy = "project", targetEntity = ProjectComment.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> projectComments = new ArrayList<>();
    @OneToMany(mappedBy = "project", targetEntity = Release.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Release> releases = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Comment> getProjectComments() {
        return projectComments;
    }

    public void setProjectComments(List<Comment> projectComments) {
        this.projectComments = projectComments;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    public List<Task> getCompletedTasks() {
        return getTasks().stream()
                .filter(task -> task.getRelease() == null)
                .filter(task -> task.getState() == Task.State.COMPLETED)
                .collect(Collectors.toList());
    }

    public List<Task> getBacklogTasks() {
        return getTasks().stream()
                .filter(task -> task.getState() == Task.State.BACKLOG)
                .collect(Collectors.toList());
    }

    public List<Task> getSelectedTasks() {
        return getTasks().stream()
                .filter(task -> task.getState() == Task.State.SELECTED)
                .collect(Collectors.toList());
    }

    public List<Task> getInProgressTasks() {
        return getTasks().stream()
                .filter(task -> task.getState() == Task.State.IN_PROGRESS)
                .collect(Collectors.toList());
    }

    public List<Task> getBlockedItems() {
        return getTasks().stream()
                .filter(task -> task.getState() == Task.State.BLOCKED)
                .collect(Collectors.toList());
    }

    public List<Task> getBugs() {
        return getTasks().stream()
                .filter(task -> task.getType() == Task.Type.BUG)
                .collect(Collectors.toList());
    }

    public List<Task> getBacklogs() {
        return getTasks().stream()
                .filter(task -> task.getType() == Task.Type.BACKLOG_ITEM)
                .collect(Collectors.toList());
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getAdmins() {
        return getUsers().stream()
                .filter(User::isAdmin)
                .collect(Collectors.toList());
    }

    public List<User> getOnlyUsers() {
        return getUsers().stream()
                .filter(User::isOnlyUser)
                .collect(Collectors.toList());
    }

    public void addComment(ProjectComment projectComment) {
        projectComments.add(projectComment);
    }

    public void addRelease(Release release) {
        releases.add(release);
    }

}
