package com.tracker.taskstracker.domain;

import com.tracker.taskstracker.exception.TRException;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "tasks")
public class Task extends IdEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private State state;
    @Column
    private Type type;
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
    @ManyToOne
    @JoinColumn(name = "release_id", referencedColumnName = "id")
    private Release release;

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

    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void addComment(TaskComment taskComment) {
        taskComments.add(taskComment);
    }

    public enum State {

        BACKLOG, SELECTED, IN_PROGRESS, BLOCKED, COMPLETED, RELEASED;

        private static final Map<String, State> NAMES_TO_VALUES = Stream.of(values())
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

    public enum Type {

        BACKLOG_ITEM("backlog-item"), BUG("bug");

        private final String referenceName;

        Type(String referenceName) {
            this.referenceName = referenceName;
        }

        private static final Map<String, Type> NAMES_TO_VALUE = Stream.of(values())
                .collect(Collectors.toMap(value -> value.referenceName, Function.identity()));

        public static Type fromString(String name) {
            Type type = NAMES_TO_VALUE.get(name);
            if (type == null) {
                throw new TRException(MessageFormat.format("Task type not found: \"{0}\"", name));
            }
            return type;
        }
    }

}
