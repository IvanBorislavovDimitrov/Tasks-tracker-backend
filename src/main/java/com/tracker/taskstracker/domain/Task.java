package com.tracker.taskstracker.domain;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.*;

import com.tracker.taskstracker.exception.TRException;

@Entity
@Table(name = "tasks")
public class Task extends IdEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private State state;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(length = 10000)
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User assignee;
    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;
    @OneToMany(mappedBy = "task", targetEntity = TaskComment.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> taskComments = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAssignee() {
        return assignee;
    }

    public void setAssignee(User assignee) {
        this.assignee = assignee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Comment> getTaskComments() {
        return taskComments;
    }

    public void setTaskComments(List<Comment> taskComments) {
        this.taskComments = taskComments;
    }

    public void addComment(TaskComment taskComment) {
        taskComments.add(taskComment);
    }

    public enum State {

        BACKLOG, SELECTED, IN_PROGRESS, BLOCKED, COMPLETED, RELEASED;

        private final static Map<String, State> NAMES_TO_VALUES = Stream.of(values())
                                                                        .collect(Collectors.toMap(value -> value.toString()
                                                                                                                .toLowerCase(),
                                                                                                  Function.identity()));

        public static State fromString(String name) {
            State state = NAMES_TO_VALUES.get(name);
            if (state == null) {
                throw new TRException(MessageFormat.format("Task state not found: \"{0}\"", name));
            }
            return state;
        }
    }

}
